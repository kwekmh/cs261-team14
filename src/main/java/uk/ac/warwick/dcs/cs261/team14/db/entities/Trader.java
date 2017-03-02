package uk.ac.warwick.dcs.cs261.team14.db.entities;

import com.esotericsoftware.kryo.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by kwekmh on 28/02/17.
 */
@Entity
@Table(name = "trader")
public class Trader {
    @Id
    private int traderId;

    @NotNull
    private String traderEmailAddress;

    public Trader() {

    }

    public Trader(int traderId) {
        this.traderId = traderId;
    }

    public Trader(String traderEmailAddress) {
        this.traderEmailAddress = traderEmailAddress;
    }

    public int getTraderId() {
        return traderId;
    }

    public void setTraderId(int traderId) {
        this.traderId = traderId;
    }

    public String getTraderEmailAddress() {
        return traderEmailAddress;
    }

    public void setTraderEmailAddress(String traderEmailAddress) {
        this.traderEmailAddress = traderEmailAddress;
    }
}
