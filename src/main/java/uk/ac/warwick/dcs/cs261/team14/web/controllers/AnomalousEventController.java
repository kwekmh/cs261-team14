package uk.ac.warwick.dcs.cs261.team14.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.warwick.dcs.cs261.team14.db.entities.*;
import uk.ac.warwick.dcs.cs261.team14.web.helpers.GraphHelper;
import uk.ac.warwick.dcs.cs261.team14.web.helpers.Pair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
}
