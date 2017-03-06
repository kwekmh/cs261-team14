package uk.ac.warwick.dcs.cs261.team14.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.warwick.dcs.cs261.team14.db.entities.*;
import uk.ac.warwick.dcs.cs261.team14.web.helpers.AnomalousEventJSONObject;
import uk.ac.warwick.dcs.cs261.team14.web.helpers.GraphHelper;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by kwekmh on 27/02/17.
 */

@Controller
public class AnomalousEventController {
    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private AggregateDataRepository aggregateDataRepository;

    @Autowired
    private TraderStatisticsRepository traderStatisticsRepository;

    @Autowired
    private SymbolRepository symbolRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private DashboardController dashboardController;

    @Autowired
    private GraphHelper graphHelper;

    @RequestMapping(value = "/details/{type}/{id}", method = RequestMethod.GET)
    public ModelAndView details(@PathVariable int type, @PathVariable int id) {
        ModelAndView mv = new ModelAndView("details/main");

        AnomalousEvent anomalousEvent = null;

        if (type == 1) { // Individual Trade
            anomalousEvent = tradeRepository.findOne(id);
        } else if (type == 2) { // EMA over 5 periods
            anomalousEvent = aggregateDataRepository.findOne(id);
        }

        // TODO: Redirect user to error page if the id or type is wrong

        mv.addObject("anomalousEvent", anomalousEvent);
        mv.addObject("symbolRepository", symbolRepository);
        mv.addObject("sectorRepository", sectorRepository);
        mv.addObject("currencyRepository", currencyRepository);

        return mv;
    }

    @RequestMapping(value = "/markAsFalsePositive/{type}/{id}", method = RequestMethod.GET)
    public ModelAndView markAsFalsePositive(@PathVariable int type, @PathVariable int id) {
        boolean success = false;
        if (type == 1) {
            Trade trade = tradeRepository.findOne(id);
            trade.setIsAnomalous(0);
            tradeRepository.save(trade);
            success = true;
        } else if (type == 2) {
            AggregateData aggregateData = aggregateDataRepository.findOne(id);
            aggregateData.setIsAnomalous(0);
            aggregateDataRepository.save(aggregateData);
            success = true;
        }

        ModelAndView mv;

        if (success) {
            mv = dashboardController.main();
            mv.addObject("message", "The event has been marked as a false positive successfully!");
        } else {
            mv = details(type, id);
            mv.addObject("message", "There was an error while attempting to process your request!");
        }

        return mv;
    }

    @RequestMapping("/allAlerts")
    public ModelAndView allAlerts() {
        ModelAndView mv = new ModelAndView("allAlerts/main");

        return mv;
    }

    @RequestMapping(value = "/api/anomalousEvents/{page}", method = RequestMethod.GET)
    public @ResponseBody AnomalousEventJSONObject[] getAnomalousEvents(@PathVariable int page) {
        int rowCount = 24;
        int total = rowCount * page;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // TODO: Optimise code in this section. Potentially requires restructuring of the SQL schema and consequently other methods

        ArrayList<AnomalousEvent> anomalousEventsList = new ArrayList<>();

        for (Trade trade : tradeRepository.findByIsAnomalousGreaterThanOrderByTimeDesc(0, new PageRequest(0, total))) {
            anomalousEventsList.add(trade);
        }

        for (AggregateData aggregateData : aggregateDataRepository.findByIsAnomalousGreaterThanOrderByGeneratedDateDesc(0, new PageRequest(0, total))) {
            anomalousEventsList.add(aggregateData);
        }

        for (TraderStatistics traderStatistics : traderStatisticsRepository.findByIsAnomalousGreaterThanOrderByGeneratedDatetime(0, new PageRequest(0, total))) {
            anomalousEventsList.add(traderStatistics);
        }

        Collections.sort(anomalousEventsList, Comparator.comparing(AnomalousEvent::getTime).reversed());

        AnomalousEventJSONObject[] anomalousEvents = new AnomalousEventJSONObject[anomalousEventsList.size() >= total ? rowCount : (anomalousEventsList.size() > (total - rowCount) ? anomalousEventsList.size() % rowCount : 0)];

        for (int i = 0, j = (page - 1) * rowCount; i < anomalousEvents.length && j < anomalousEventsList.size(); i++, j++) {
            AnomalousEvent anomalousEvent = anomalousEventsList.get(j);
            AnomalousEventJSONObject obj = new AnomalousEventJSONObject();

            Symbol symbol = symbolRepository.findOne(anomalousEvent.getSymbolId());

            String currency = "GBX";

            if (anomalousEvent instanceof Trade) {
                currency = currencyRepository.findOne(((Trade) anomalousEvent).getCurrencyId()).getCurrencyName();
            }

            obj.setId(anomalousEvent.getId());
            obj.setTime(formatter.format(anomalousEvent.getTime().toLocalDateTime()));
            obj.setSymbol(symbol.getSymbolName());
            obj.setSector(sectorRepository.findOne(symbol.getSectorId()).getSectorName());
            obj.setCurrency(currency);
            obj.setType(anomalousEvent.getAnomalousEventType());

            anomalousEvents[i] = obj;
        }

        return anomalousEvents;
    }
}
