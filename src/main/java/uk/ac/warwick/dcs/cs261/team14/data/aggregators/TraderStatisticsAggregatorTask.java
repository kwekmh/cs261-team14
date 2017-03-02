package uk.ac.warwick.dcs.cs261.team14.data.aggregators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.ac.warwick.dcs.cs261.team14.db.entities.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kwekmh on 28/02/17.
 */

@Component
public class TraderStatisticsAggregatorTask {

    @Autowired
    TraderRepository traderRepository;

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    TraderStatisticsRepository traderStatisticsRepository;

    @Async
    @Scheduled(cron = "0 0 * * * *", zone = "Europe/London")
    public void run() {
        LocalDateTime now = LocalDateTime.now().minusHours(1);
        aggregateHourly(now);
    }

    public void aggregateHourly(LocalDateTime time) {
        for (Trader trader : traderRepository.findAll()) {
            int traderId = trader.getTraderId();

            LocalDateTime start = time.withMinute(0).withSecond(0).withNano(0);
            LocalDateTime end = start.withMinute(59).withSecond(59).withNano(999999999);

            HashMap<Integer, Integer> sizeBought = new HashMap<>();
            HashMap<Integer, Integer> sizeSold = new HashMap<>();

            HashMap<Integer, Double> valueBought = new HashMap<>();
            HashMap<Integer, Double> valueSold = new HashMap();

            for (Trade trade : tradeRepository.findByBuyerIdOrSellerIdAndTimeBetween(trader.getTraderId(), trader.getTraderId(), Timestamp.valueOf(start), Timestamp.valueOf(end))) {
                if (trade.getBuyerId() == traderId) {
                    sizeBought.put(trade.getSymbolId(), sizeBought.get(trade.getSymbolId()) + trade.getSize());
                    valueBought.put(trade.getSymbolId(), valueBought.get(trade.getSymbolId()) + trade.getPrice());
                } else {
                    sizeSold.put(trade.getSymbolId(), sizeSold.get(trade.getSymbolId()) + trade.getSize());
                    valueSold.put(trade.getSymbolId(), valueSold.get(trade.getSymbolId()) + trade.getPrice());
                }
            }

            int isAnomalous = 0;

            // TODO: Predict status of aggregated data

            ArrayList<TraderStatistics> traderStatisticsList = new ArrayList<>();

            for (int symbolId : sizeBought.keySet()) {
                TraderStatistics traderStatistics = new TraderStatistics(traderId, symbolId, Timestamp.valueOf(start), sizeBought.get(symbolId), sizeSold.get(symbolId), valueBought.get(symbolId), valueSold.get(symbolId), isAnomalous);
                traderStatisticsList.add(traderStatistics);
            }

            traderStatisticsRepository.save(traderStatisticsList);
        }
    }
}
