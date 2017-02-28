package uk.ac.warwick.dcs.cs261.team14.data.aggregators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.ac.warwick.dcs.cs261.team14.Application;
import uk.ac.warwick.dcs.cs261.team14.db.entities.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by Ming on 2/23/2017.
 */
@Component
public class EMAAggregatorTask {
    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    AggregateDataRepository aggregateDataRepository;

    @Autowired
    SymbolRepository symbolRepository;

    private final Logger logger = LoggerFactory.getLogger(Application.class);

    @Async
    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/London")
    public void aggregate() {
        LocalDateTime ldtNow= LocalDateTime.now();
        Timestamp now = Timestamp.valueOf(ldtNow);

        logger.info("Starting aggregation task for EMA over 5 periods at " + ldtNow);

        int n = 5;
        double k = 2.0 / (n + 1);

        ArrayList<AggregateData> newEMAs = new ArrayList<AggregateData>();


        for (Symbol sym : symbolRepository.findAll()) {
            AggregateData lastEMA = aggregateDataRepository.findTop1ByTypeIdAndSymbolIdOrderByGeneratedDateDesc(1, sym.getSymbolId());
            Trade trade = tradeRepository.findTop1BySymbolIdOrderByTimeDesc(sym.getSymbolId());

            AggregateData ema;

            if (trade != null) {
                if (lastEMA != null) {

                    double newEMAValue = (trade.getPrice() * k) + (lastEMA.getValue() * (1.0 - k));

                    ema = new AggregateData(1, sym.getSymbolId(), newEMAValue, 0, now);

                } else {
                    ema = new AggregateData(1, sym.getSymbolId(), trade.getPrice(), 0, now);
                }

                newEMAs.add(ema);

                // TODO: Remove once learning is implemented
                aggregateDataRepository.save(ema);
            }


        }

        // TODO: Implement learning here and update database with predicted rows instead of raw rows

    }
}
