package uk.ac.warwick.dcs.cs261.team14.data;

import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.ac.warwick.dcs.cs261.team14.data.pipeline.InputController;
import uk.ac.warwick.dcs.cs261.team14.data.transformers.DataTransformerMapping;
import uk.ac.warwick.dcs.cs261.team14.db.entities.Trade;
import uk.ac.warwick.dcs.cs261.team14.db.entities.TradeRepository;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Ming on 2/12/2017.
 */
@Component
public class LiveStreamTask {
    @Autowired
    InputController inputController;
    @Autowired
    DataTransformerMapping dataTransformerMapping;
    @Autowired
    TradeRepository tradeRepository;

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @PostConstruct
    public void onStartup() {
        taskExecutor().execute(() -> this.run());
    }

    @Async
    @Scheduled(cron = "0 10 1 * * *", zone = "Europe/London")
    public void run() {
        try {
            dataTransformerMapping.init();
            Socket socket = new Socket("cs261.dcs.warwick.ac.uk", 80);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

            while ((line = in.readLine()) != null) {
                Row row = inputController.processLine(line);
                if (row != null) {
                    Trade trade = new Trade();
                    trade.setTime(Timestamp.valueOf(LocalDateTime.parse(row.get(11).toString(), formatter)));
                    trade.setBuyerId(row.getInt(1));
                    trade.setSellerId(row.getInt(2));
                    trade.setPrice(row.getDouble(3));
                    trade.setSize(Integer.parseInt(row.get(4).toString()));
                    trade.setCurrencyId(row.getInt(5));
                    trade.setSymbolId(row.getInt(6));
                    trade.setBidPrice(row.getDouble(8));
                    trade.setAskPrice(row.getDouble(9));
                    trade.setPctPriceChange(row.getDouble(10));
                    trade.setCategoryId(1);
                    trade.setIsAnomalous((int) row.getDouble(13));
                    tradeRepository.save(trade);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
