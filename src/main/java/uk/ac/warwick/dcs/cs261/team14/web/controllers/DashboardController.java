package uk.ac.warwick.dcs.cs261.team14.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.warwick.dcs.cs261.team14.db.entities.*;
import uk.ac.warwick.dcs.cs261.team14.web.helpers.GraphHelper;
import uk.ac.warwick.dcs.cs261.team14.web.helpers.Pair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Ming on 2/11/2017.
 */

@Controller
public class DashboardController {

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
    private GraphHelper graphHelper;

    @RequestMapping("/")
    public ModelAndView main() {
        ModelAndView mv = new ModelAndView("dashboard/main");

        int isAnomalous = 1;

        ArrayList<AnomalousEvent> anomalousEventsList = new ArrayList<AnomalousEvent>();

        for (Trade trade : tradeRepository.findTop10ByIsAnomalousOrderByTimeDesc(isAnomalous)) {
            anomalousEventsList.add(trade);
        }

        for (AggregateData aggregateData : aggregateDataRepository.findTop10ByIsAnomalousOrderByGeneratedDateDesc(isAnomalous)) {
            anomalousEventsList.add(aggregateData);
        }

        Collections.sort(anomalousEventsList, Comparator.comparing(AnomalousEvent::getTime).reversed());

        int size = anomalousEventsList.size() < 10 ? anomalousEventsList.size() : 10;

        AnomalousEvent[] anomalousEvents = new AnomalousEvent[size];

        // Only take up to the top 10 results of the interspersed events
        for (int i = 0; i < size; i++) {
            anomalousEvents[i] = anomalousEventsList.get(i);
        }

        // Preparation of values of the graphs

        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        ArrayList<Pair<LocalDateTime, Double>> graphArrayList = graphHelper.generateHourlyAverageRollingPctPriceChange(now);

        String[] rollingX = new String[graphArrayList.size()];
        double[] rollingY = new double[graphArrayList.size()];

        for (int i = 0; i < graphArrayList.size(); i++) {
            Pair<LocalDateTime, Double> pair = graphArrayList.get(i);
            rollingX[i] = formatter.format(pair.getFirst());
            rollingY[i] = pair.getSecond();
        }

        ArrayList<Pair<LocalDateTime, Trade>> anomalousArrayList = graphHelper.getAnomalousTradesBetweenForGraphWithHour(now);

        String[] anomalousX = new String[anomalousArrayList.size()];
        double[] anomalousY = new double[anomalousArrayList.size()];


        for (int i = 0; i < anomalousArrayList.size(); i++) {
            Pair<LocalDateTime, Trade> pair = anomalousArrayList.get(i);
            anomalousX[i] = formatter.format(pair.getFirst());
            anomalousY[i] = pair.getSecond().getPctPriceChange();
        }

        mv.addObject("anomalousEvents", anomalousEvents);
        mv.addObject("symbolRepository", symbolRepository);
        mv.addObject("sectorRepository", sectorRepository);
        mv.addObject("currencyRepository", currencyRepository);

        mv.addObject("rollingX", rollingX);
        mv.addObject("rollingY", rollingY);
        mv.addObject("anomalousX", anomalousX);
        mv.addObject("anomalousY", anomalousY);

        return mv;
    }

    @RequestMapping("/allTrades")
    public ModelAndView allTrades() {
        ModelAndView mv = new ModelAndView("allTrades/main");

        return mv;
    }
}
