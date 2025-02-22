package uk.ac.warwick.dcs.cs261.team14.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.warwick.dcs.cs261.team14.data.FileInputTask;
import uk.ac.warwick.dcs.cs261.team14.db.entities.*;
import uk.ac.warwick.dcs.cs261.team14.web.helpers.AnomalousEventJSONObject;
import uk.ac.warwick.dcs.cs261.team14.web.helpers.GraphData;
import uk.ac.warwick.dcs.cs261.team14.web.helpers.GraphHelper;
import uk.ac.warwick.dcs.cs261.team14.web.helpers.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

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
    private TraderStatisticsRepository traderStatisticsRepository;

    @Autowired
    private SymbolRepository symbolRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private FileInputTask fileInputTask;

    @Autowired
    private GraphHelper graphHelper;

    @Value("${cs261.uploads.directory}")
    private String uploadsDirectory;

    @RequestMapping("/")
    public ModelAndView main() {
        ModelAndView mv = new ModelAndView("dashboard/main");

        ArrayList<AnomalousEvent> anomalousEventsList = new ArrayList<AnomalousEvent>();

        for (Trade trade : tradeRepository.findTop10ByIsAnomalousGreaterThanOrderByTimeDesc(0)) {
            anomalousEventsList.add(trade);
        }

        for (AggregateData aggregateData : aggregateDataRepository.findTop10ByIsAnomalousGreaterThanOrderByGeneratedDateDesc(0)) {
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
        mv.addObject("currencyRepository", currencyRepository);

        return mv;
    }

    @RequestMapping(value = "/api/getLatestAnomalousEvents", method = RequestMethod.GET)
    public @ResponseBody AnomalousEventJSONObject[] getLatestAnomalousEvents() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ArrayList<AnomalousEvent> anomalousEventsList = new ArrayList<AnomalousEvent>();

        for (Trade trade : tradeRepository.findTop10ByIsAnomalousGreaterThanOrderByTimeDesc(0)) {
            anomalousEventsList.add(trade);
        }

        for (AggregateData aggregateData : aggregateDataRepository.findTop10ByIsAnomalousGreaterThanOrderByGeneratedDateDesc(0)) {
            anomalousEventsList.add(aggregateData);
        }

        for (TraderStatistics traderStatistics : traderStatisticsRepository.findTop10ByIsAnomalousGreaterThanOrderByGeneratedDatetime(0)) {
            anomalousEventsList.add(traderStatistics);
        }

        Collections.sort(anomalousEventsList, Comparator.comparing(AnomalousEvent::getTime).reversed());

        int size = anomalousEventsList.size() < 10 ? anomalousEventsList.size() : 10;

        AnomalousEventJSONObject[] anomalousEvents = new AnomalousEventJSONObject[size];

        // Only take up to the top 10 results of the interspersed events
        for (int i = 0; i < size; i++) {
            AnomalousEventJSONObject jsonObject = new AnomalousEventJSONObject();

            AnomalousEvent event = anomalousEventsList.get(i);

            Symbol symbol = symbolRepository.findOne(event.getSymbolId());

            jsonObject.setId(event.getId());
            jsonObject.setTime(formatter.format(event.getTime().toLocalDateTime()));
            jsonObject.setSymbol(symbol.getSymbolName());
            jsonObject.setSector(sectorRepository.findOne(symbol.getSectorId()).getSectorName());
            jsonObject.setCurrency("GBX");
            jsonObject.setType(event.getAnomalousEventType());

            anomalousEvents[i] = jsonObject;
        }

        return anomalousEvents;
    }

    @RequestMapping(value = "/api/getSymbolPriceData/{symbolId}", method = RequestMethod.GET)
    public @ResponseBody GraphData getSymbolPriceData(@PathVariable int symbolId) {
        // Preparation of values of the graphs

        Symbol symbol = symbolRepository.findOne(symbolId);

        if (symbol != null) {
            LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            ArrayList<Pair<LocalDateTime, Double>> graphArrayList = graphHelper.generateHourlyAverageRollingPriceBySymbol(symbolId, now);

            String[] rollingX = new String[graphArrayList.size()];
            double[] rollingY = new double[graphArrayList.size()];

            for (int i = 0; i < graphArrayList.size(); i++) {
                Pair<LocalDateTime, Double> pair = graphArrayList.get(i);
                rollingX[i] = formatter.format(pair.getFirst());
                rollingY[i] = pair.getSecond();
            }

            ArrayList<Pair<LocalDateTime, Trade>> anomalousArrayList = graphHelper.getAnomalousTradesBetweenForGraphWithHourBySymbol(symbolId, now);

            String[] anomalousX = new String[anomalousArrayList.size()];
            double[] anomalousY = new double[anomalousArrayList.size()];


            for (int i = 0; i < anomalousArrayList.size(); i++) {
                Pair<LocalDateTime, Trade> pair = anomalousArrayList.get(i);
                anomalousX[i] = formatter.format(pair.getFirst());
                anomalousY[i] = pair.getSecond().getPrice();
            }

            GraphData graphData = new GraphData(rollingX, rollingY, anomalousX, anomalousY);

            return graphData;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/api/getSymbolSizeData/{symbolId}", method = RequestMethod.GET)
    public @ResponseBody GraphData getSymbolSizeData(@PathVariable int symbolId) {
        // Preparation of values of the graphs

        Symbol symbol = symbolRepository.findOne(symbolId);

        if (symbol != null) {
            LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            ArrayList<Pair<LocalDateTime, Double>> graphArrayList = graphHelper.generateHourlyAverageRollingSizeBySymbol(symbolId, now);

            String[] rollingX = new String[graphArrayList.size()];
            double[] rollingY = new double[graphArrayList.size()];

            for (int i = 0; i < graphArrayList.size(); i++) {
                Pair<LocalDateTime, Double> pair = graphArrayList.get(i);
                rollingX[i] = formatter.format(pair.getFirst());
                rollingY[i] = pair.getSecond();
            }

            ArrayList<Pair<LocalDateTime, Trade>> anomalousArrayList = graphHelper.getAnomalousTradesBetweenForGraphWithHourBySymbol(symbolId, now);

            String[] anomalousX = new String[anomalousArrayList.size()];
            double[] anomalousY = new double[anomalousArrayList.size()];


            for (int i = 0; i < anomalousArrayList.size(); i++) {
                Pair<LocalDateTime, Trade> pair = anomalousArrayList.get(i);
                anomalousX[i] = formatter.format(pair.getFirst());
                anomalousY[i] = pair.getSecond().getSize();
            }

            GraphData graphData = new GraphData(rollingX, rollingY, anomalousX, anomalousY);

            return graphData;
        } else {
            return null;
        }
    }

    @RequestMapping("/api/getSymbols")
    public @ResponseBody ArrayList<HashMap<String, String>> getSymbols() {
        ArrayList<HashMap<String, String>> symbols = new ArrayList<>();
        for (Symbol symbol : symbolRepository.findAll()) {
            HashMap<String, String> values = new HashMap<>();
            values.put("symbolId", Integer.toString(symbol.getSymbolId()));
            values.put("symbolName", symbol.getSymbolName());
            symbols.add(values);
        }

        return symbols;
    }

    @PostMapping("/upload")
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            ModelAndView mv = main();
            mv.addObject("message", "There was an error while uploading the file!");

            return mv;
        }

        try {
            byte[] bytes = file.getBytes();

            long now = System.currentTimeMillis();

            Path path = Paths.get(getUploadsDirectory() + "upload_" + now + ".csv");

            int count = 0;

            while (Files.exists(path)) {
               count++;
            }

            if (count > 0) {
                path = Paths.get(getUploadsDirectory() + "upload_" + now + "_" + count + ".csv");
            }

            Files.write(path, bytes);

            fileInputTask.processFile(path.toFile());

            ModelAndView mv = main();
            mv.addObject("message", "Your file has been uploaded successfully!");

            return mv;
        } catch (IOException e) {
            ModelAndView mv = main();
            mv.addObject("message", "There was an error while uploading the file!");

            return mv;
        }
    }

    public String getUploadsDirectory() {
       if (uploadsDirectory.indexOf(uploadsDirectory.length() - 1) != File.separatorChar) {
            return uploadsDirectory + File.separator;
        } else {
            return uploadsDirectory;
        }
    }

    //Not sure how necessayr this is rather than a javascript update to span text
    @GetMapping("/updateMessageForAnomaly")
    public ModelAndView updateMessageForAnomaly() {
        ModelAndView mv = main();
        mv.addObject("message", "A new anomaly has been found!");

        return mv;
    }
}
