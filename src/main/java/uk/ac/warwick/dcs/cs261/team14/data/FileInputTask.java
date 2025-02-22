package uk.ac.warwick.dcs.cs261.team14.data;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import uk.ac.warwick.dcs.cs261.team14.Application;
import uk.ac.warwick.dcs.cs261.team14.data.aggregators.TraderStatisticsAggregatorTask;
import uk.ac.warwick.dcs.cs261.team14.data.pipeline.InputController;
import uk.ac.warwick.dcs.cs261.team14.data.transformers.DataTransformerMapping;
import uk.ac.warwick.dcs.cs261.team14.db.entities.Trade;
import uk.ac.warwick.dcs.cs261.team14.db.entities.TradeRepository;
import uk.ac.warwick.dcs.cs261.team14.learning.IndividualTradeLearningModel;

import javax.annotation.PostConstruct;
import java.io.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by kwekmh on 02/03/17.
 */
@Component
public class FileInputTask {
    @Autowired
    InputController inputController;

    @Autowired
    DataTransformerMapping dataTransformerMapping;

    @Autowired
    TraderStatisticsAggregatorTask traderStatisticsAggregatorTask;

    @Autowired
    IndividualTradeLearningModel individualTradeLearningModel;

    @Autowired
    private TradeRepository tradeRepository;

    private HashSet<LocalDateTime> times;

    private final Logger logger = LoggerFactory.getLogger(Application.class);

    public FileInputTask() {
        times = new HashSet<>();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    public void processFile(File file) {
        taskExecutor().execute(() -> execute(file));
    }

    public void execute(File file) {
        logger.info("Processing file " + file);
        dataTransformerMapping.init();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            String line;

            DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
            DateTimeFormatter secondaryFormatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            ArrayList<Row> rows = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                Row raw = inputController.getAsRow(line);

                if (raw != null) {
                    rows.add(raw);
                }
            }

            Dataset<Row> predictions = individualTradeLearningModel.predictList(rows);

            logger.info("Saving predicted records to database");

            for (Row row : predictions.collectAsList()) {
                try {
                    LocalDateTime ldt;

                    try {
                        ldt = LocalDateTime.parse(row.get(10).toString(), formatter);
                    } catch (DateTimeParseException e) {
                        ldt = LocalDateTime.parse(row.get(10).toString(), secondaryFormatter);
                    }

                    Trade trade = new Trade();
                    trade.setTime(Timestamp.valueOf(ldt));
                    trade.setBuyerId(row.getInt(1));
                    trade.setSellerId(row.getInt(2));
                    trade.setPrice(row.getDouble(3));
                    trade.setSize(Integer.parseInt(row.get(4).toString()));
                    trade.setCurrencyId(row.getInt(5));
                    trade.setSymbolId(row.getInt(6));
                    trade.setBidPrice(row.getDouble(8));
                    trade.setAskPrice(row.getDouble(9));
                    trade.setCategoryId(1);
                    trade.setIsAnomalous((int) row.getDouble(12));
                    tradeRepository.save(trade);

                    LocalDateTime time = ldt.withMinute(0).withSecond(0).withNano(0);
                    times.add(time);
                } catch (Exception e) { // In case we encounter any problems with this row, we want to continue with the remaining rows
                    e.printStackTrace();
                }
            }

            logger.info("Predicted records written to database successfully");

            logger.info("Aggregating traders' statistics");
            for (LocalDateTime time : times) {
                traderStatisticsAggregatorTask.aggregateHourly(time);
            }
            logger.info("Traders' statistics aggregated and saved to the database");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}