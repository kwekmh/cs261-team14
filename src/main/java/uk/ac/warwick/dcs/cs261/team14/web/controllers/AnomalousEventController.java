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
    private GraphHelper graphHelper;

    @RequestMapping(value = "/details/{type}/{id}", method = RequestMethod.GET)
    public ModelAndView details(@PathVariable int id, @PathVariable int type) {
        ModelAndView mv = new ModelAndView("details/main");

        AnomalousEvent anomalousEvent = null;

        if (type == 1) { // Individual Trade
            anomalousEvent = tradeRepository.findOne(id);
        } else if (type == 2) { // EMA over 5 periods
            anomalousEvent = aggregateDataRepository.findOne(id);
        }

        // TODO: Redirect user to error page if the id or type is wrong

        // Preparation of values of the graphs

        int symbolId = anomalousEvent.getSymbolId();

        LocalDateTime date = anomalousEvent.getTime().toLocalDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        ArrayList<Pair<LocalDateTime, Double>> graphArrayList = graphHelper.generateHourlyAverageRollingPctPriceChangeBySymbol(symbolId, date);

        String[] rollingX = new String[graphArrayList.size()];
        double[] rollingY = new double[graphArrayList.size()];

        for (int i = 0; i < graphArrayList.size(); i++) {
            Pair<LocalDateTime, Double> pair = graphArrayList.get(i);
            rollingX[i] = formatter.format(pair.getFirst());
            rollingY[i] = pair.getSecond();
        }

        ArrayList<Pair<LocalDateTime, Trade>> anomalousArrayList = graphHelper.getAnomalousTradesBetweenForGraphWithHourBySymbol(symbolId, date);

        String[] anomalousX = new String[anomalousArrayList.size()];
        double[] anomalousY = new double[anomalousArrayList.size()];


        for (int i = 0; i < anomalousArrayList.size(); i++) {
            Pair<LocalDateTime, Trade> pair = anomalousArrayList.get(i);
            anomalousX[i] = formatter.format(pair.getFirst());
            anomalousY[i] = pair.getSecond().getPctPriceChange();
        }

        mv.addObject("anomalousEvent", anomalousEvent);
        mv.addObject("symbolRepository", symbolRepository);
        mv.addObject("sectorRepository", sectorRepository);
        mv.addObject("currencyRepository", currencyRepository);

        mv.addObject("rollingX", rollingX);
        mv.addObject("rollingY", rollingY);
        mv.addObject("anomalousX", anomalousX);
        mv.addObject("anomalousY", anomalousY);

        return mv;
    }
}
