package uk.ac.warwick.dcs.cs261.team14.db.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Created by Ming on 2/12/2017.
 */

@Entity
@Table(name = "trade")
public class Trade {
    @Id
    private int tradeId;

    @NotNull
    private Timestamp time;

    @NotNull
    private String buyer;

    @NotNull
    private String seller;

    @NotNull
    private double price;

    @NotNull
    private int size;

    @NotNull
    private int currencyId;

    @NotNull
    private int symbolId;

    @NotNull
    private double bidPrice;

    @NotNull
    private double askPrice;

    @NotNull
    private double pctPriceChange;

    @NotNull
    private int categoryId;

    @NotNull
    private int isAnomalous;

    public Trade() {

    }

    public Trade(int tradeId) {
        this.tradeId = tradeId;
    }

    public Trade(Timestamp time, String buyer, String seller, double price, int size, int currencyId, int symbolId, double bidPrice, double askPrice, double pctPriceChange, int categoryId, int isAnomalous) {
        this.time = time;
        this.buyer = buyer;
        this.seller = seller;
        this.price = price;
        this.size = size;
        this.currencyId = currencyId;
        this.symbolId = symbolId;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.pctPriceChange = pctPriceChange;
        this.categoryId = categoryId;
        this.isAnomalous = isAnomalous;
    }

    public int getTradeId() {
        return tradeId;
    }

    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public int getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(int symbolId) {
        this.symbolId = symbolId;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(double askPrice) {
        this.askPrice = askPrice;
    }

    public double getPctPriceChange() {
        return pctPriceChange;
    }

    public void setPctPriceChange(double pctPriceChange) {
        this.pctPriceChange = pctPriceChange;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getIsAnomalous() {
        return isAnomalous;
    }

    public void setIsAnomalous(int isAnomalous) {
        this.isAnomalous = isAnomalous;
    }
}
