package uk.ac.warwick.dcs.cs261.team14.db.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by Ming on 2/12/2017.
 */

@Entity
@Table(name = "currency")
public class Currency {
    @Id
    @GeneratedValue
    private int currencyId;

    @NotNull
    private String currencyName;

    public Currency() {

    }

    public Currency(int currencyId) {
        this.currencyId = currencyId;
    }

    public Currency(String currencyName) {
        this.currencyName = currencyName;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }
}
