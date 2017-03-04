package uk.ac.warwick.dcs.cs261.team14.db.entities;

import com.esotericsoftware.kryo.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by kwekmh on 28/02/17.
 */
@Entity
@Table(name = "trader_statistics")
public class TraderStatistics implements AnomalousEvent {
    @Id
    @GeneratedValue
    private int traderStatisticsId;

    @NotNull
    private int traderId;

    @NotNull
    private int symbolId;

    @NotNull
    private Timestamp generatedDatetime;

    @NotNull
    private int aggregatedSizeBought;

    @NotNull
    private int aggregatedSizeSold;

    @NotNull
    private double aggregatedValueBought;

    @NotNull
    private double aggregatedValueSold;

    @NotNull
    private int isAnomalous;

    public TraderStatistics() {

    }

    public TraderStatistics(int traderStatisticsId) {
        this.traderStatisticsId = traderStatisticsId;
    }

    public TraderStatistics(int traderId, int symbolId, Timestamp generatedDatetime, int aggregatedSizeBought, int aggregatedSizeSold, double aggregatedValueBought, double aggregatedValueSold, int isAnomalous) {
        this.traderId = traderId;
        this.symbolId = symbolId;
        this.generatedDatetime = generatedDatetime;
        this.aggregatedSizeBought = aggregatedSizeBought;
        this.aggregatedSizeSold = aggregatedSizeSold;
        this.aggregatedValueBought = aggregatedValueBought;
        this.aggregatedValueSold = aggregatedValueSold;
        this.isAnomalous = isAnomalous;
    }

    public int getTraderStatisticsId() {
        return traderStatisticsId;
    }

    public void setTraderStatisticsId(int traderStatisticsId) {
        this.traderStatisticsId = traderStatisticsId;
    }

    public int getTraderId() {
        return traderId;
    }

    public void setTraderId(int traderId) {
        this.traderId = traderId;
    }

    public int getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(int symbolId) {
        this.symbolId = symbolId;
    }

    public Timestamp getGeneratedDatetime() {
        return generatedDatetime;
    }

    public void setGeneratedDatetime(Timestamp generatedDatetime) {
        this.generatedDatetime = generatedDatetime;
    }

    public int getAggregatedSizeBought() {
        return aggregatedSizeBought;
    }

    public void setAggregatedSizeBought(int aggregatedSizeBought) {
        this.aggregatedSizeBought = aggregatedSizeBought;
    }

    public int getAggregatedSizeSold() {
        return aggregatedSizeSold;
    }

    public void setAggregatedSizeSold(int aggregatedSizeSold) {
        this.aggregatedSizeSold = aggregatedSizeSold;
    }

    public double getAggregatedValueBought() {
        return aggregatedValueBought;
    }

    public void setAggregatedValueBought(double aggregatedValueBought) {
        this.aggregatedValueBought = aggregatedValueBought;
    }

    public double getAggregatedValueSold() {
        return aggregatedValueSold;
    }

    public void setAggregatedValueSold(double aggregatedValueSold) {
        this.aggregatedValueSold = aggregatedValueSold;
    }

    public int getIsAnomalous() {
        return isAnomalous;
    }

    public void setIsAnomalous(int isAnomalous) {
        this.isAnomalous = isAnomalous;
    }

    public int getId() {
        return getTraderStatisticsId();
    }

    public Timestamp getTime() {
        return getGeneratedDatetime();
    }

    public int getAnomalousEventType() {
        return 3;
    }
}
