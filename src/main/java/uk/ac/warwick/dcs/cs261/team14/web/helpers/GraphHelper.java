package uk.ac.warwick.dcs.cs261.team14.web.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.warwick.dcs.cs261.team14.db.entities.AggregateDataRepository;
import uk.ac.warwick.dcs.cs261.team14.db.entities.Trade;
import uk.ac.warwick.dcs.cs261.team14.db.entities.TradeRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by kwekmh on 27/02/17.
 */
@Component
public class GraphHelper {
    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private AggregateDataRepository aggregateDataRepository;

    public ArrayList<Pair<LocalDateTime, Double>> generateDailyAverageRollingPrice(LocalDateTime start, LocalDateTime end) {
        LocalDateTime current = start.withHour(0).withMinute(0).withSecond(0).withNano(0);

        LocalDateTime next = current.plusDays(1).withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        ArrayList<Pair<LocalDateTime, Double>> resultList = new ArrayList<>();

        double sum = 0.0;
        int count = 0;

        double average;

        while (current.compareTo(end) <= 0) {
            for (Trade trade : tradeRepository.findByTimeBetween(Timestamp.valueOf(current), Timestamp.valueOf(next))) {
                sum += trade.getPrice();
                count++;
            }

            if (count > 0) {
                average = sum / count;

                resultList.add(new Pair<LocalDateTime, Double>(current, average));

                sum = 0.0;
                count = 0;
            }

            current = current.plusDays(1);
            next = next.plusDays(1);
        }

        return resultList;
    }

    public ArrayList<Pair<LocalDateTime, Trade>> getAnomalousTradesBetweenForGraphWithHour(LocalDateTime date) {
        LocalDateTime start = date.withHour(0).withMinute(0).withSecond(0).withNano(0);

        LocalDateTime end = date.withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        ArrayList<Pair<LocalDateTime, Trade>> resultList = new ArrayList<>();

        for (Trade trade : tradeRepository.findByIsAnomalousGreaterThanAndTimeBetween(0, Timestamp.valueOf(start), Timestamp.valueOf(end))) {
            LocalDateTime time = trade.getTime().toLocalDateTime().withMinute(0).withSecond(0).withNano(0);
            resultList.add(new Pair<LocalDateTime, Trade>(time, trade));
        }

        return resultList;
    }

    public ArrayList<Pair<LocalDateTime, Trade>> getAnomalousTradesBetweenForGraph(LocalDateTime start, LocalDateTime end) {
        start = start.withHour(0).withMinute(0).withSecond(0).withNano(0);

        end = end.withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        ArrayList<Pair<LocalDateTime, Trade>> resultList = new ArrayList<Pair<LocalDateTime, Trade>>();

        for (Trade trade : tradeRepository.findByIsAnomalousGreaterThanAndTimeBetween(0, Timestamp.valueOf(start), Timestamp.valueOf(end))) {
            LocalDateTime time = trade.getTime().toLocalDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
            resultList.add(new Pair<LocalDateTime, Trade>(time, trade));
        }

        return resultList;
    }

    public ArrayList<Pair<LocalDateTime, Double>> generateHourlyAverageRollingPrice(LocalDateTime date) {
        LocalDateTime current = date.withHour(0).withMinute(0).withSecond(0).withNano(0);

        LocalDateTime end = current.plusDays(1);

        LocalDateTime next = current.plusHours(1);

        ArrayList<Pair<LocalDateTime, Double>> resultList = new ArrayList<>();

        double sum = 0.0;
        int count = 0;

        double average;

        while (current.compareTo(end) <= 0) {
            for (Trade trade : tradeRepository.findByTimeBetween(Timestamp.valueOf(current), Timestamp.valueOf(next))) {
                sum += trade.getPrice();
                count++;
            }

            if (count > 0) {
                average = sum / count;

                resultList.add(new Pair<LocalDateTime, Double>(current, average));

                sum = 0.0;
                count = 0;
            }

            current = current.plusHours(1);
            next = next.plusHours(1);
        }

        return resultList;
    }

        public ArrayList<Pair<LocalDateTime, Trade>> getAnomalousTradesBetweenForGraphWithHourBySymbol(int symbolId, LocalDateTime date) {
        LocalDateTime start = date.withHour(0).withMinute(0).withSecond(0).withNano(0);

        LocalDateTime end = date.withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        ArrayList<Pair<LocalDateTime, Trade>> resultList = new ArrayList<Pair<LocalDateTime, Trade>>();

        for (Trade trade : tradeRepository.findByIsAnomalousGreaterThanAndSymbolIdAndTimeBetween(0, symbolId, Timestamp.valueOf(start), Timestamp.valueOf(end))) {
            LocalDateTime time = trade.getTime().toLocalDateTime().withMinute(0).withSecond(0).withNano(0);
            resultList.add(new Pair<LocalDateTime, Trade>(time, trade));
        }

        return resultList;
    }

    public ArrayList<Pair<LocalDateTime, Double>> generateHourlyAverageRollingPriceBySymbol(int symbolId, LocalDateTime date) {
        LocalDateTime current = date.withHour(0).withMinute(0).withSecond(0).withNano(0);

        LocalDateTime end = current.plusDays(1);

        LocalDateTime next = current.plusHours(1);

        ArrayList<Pair<LocalDateTime, Double>> resultList = new ArrayList<>();

        double sum = 0.0;
        int count = 0;

        double average;

        while (current.compareTo(end) <= 0) {
            for (Trade trade : tradeRepository.findBySymbolIdAndTimeBetween(symbolId, Timestamp.valueOf(current), Timestamp.valueOf(next))) {
                sum += trade.getPrice();
                count++;
            }

            if (count > 0) {
                average = sum / count;

                resultList.add(new Pair<LocalDateTime, Double>(current, average));

                sum = 0.0;
                count = 0;
            }

            current = current.plusHours(1);
            next = next.plusHours(1);
        }

            return resultList;
        }

    public ArrayList<Pair<LocalDateTime, Double>> generateHourlyAverageRollingSizeBySymbol(int symbolId, LocalDateTime date) {
        LocalDateTime current = date.withHour(0).withMinute(0).withSecond(0).withNano(0);

        LocalDateTime end = current.plusDays(1);

        LocalDateTime next = current.plusHours(1);

        ArrayList<Pair<LocalDateTime, Double>> resultList = new ArrayList<>();

        double sum = 0.0;
        int count = 0;

        double average;

        while (current.compareTo(end) <= 0) {
            for (Trade trade : tradeRepository.findBySymbolIdAndTimeBetween(symbolId, Timestamp.valueOf(current), Timestamp.valueOf(next))) {
                sum += trade.getSize();
                count++;
            }

            if (count > 0) {
                average = sum / count;

                resultList.add(new Pair<LocalDateTime, Double>(current, average));

                sum = 0.0;
                count = 0;
            }

            current = current.plusHours(1);
            next = next.plusHours(1);
        }

            return resultList;
        }
}
