package uk.ac.warwick.dcs.cs261.team14.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.warwick.dcs.cs261.team14.db.entities.*;

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

        mv.addObject("anomalousEvents", anomalousEvents);
        mv.addObject("symbolRepository", symbolRepository);
        mv.addObject("sectorRepository", sectorRepository);

        return mv;
    }

    @RequestMapping("/allTrades")
    public ModelAndView allTrades() {
        ModelAndView mv = new ModelAndView("allTrades/main");

        return mv;
    }

    @RequestMapping("/details")
    public ModelAndView details() {
        ModelAndView mv = new ModelAndView("details/main");

        return mv;
    }
}
