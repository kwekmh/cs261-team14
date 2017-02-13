package uk.ac.warwick.dcs.cs261.team14.db.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by Ming on 2/12/2017.
 */

@Entity
@Table(name = "symbol")
public class Symbol {
    @Id
    private int symbolId;

    @NotNull
    private String symbolName;

    @NotNull
    private int sectorId;

    public Symbol() {

    }

    public Symbol(int symbolId) {
        this.symbolId = symbolId;
    }

    public Symbol(String symbolName, int sectorId) {
        this.symbolName = symbolName;
        this.sectorId = sectorId;
    }

    public int getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(int symbolId) {
        this.symbolId = symbolId;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public int getSectorId() {
        return sectorId;
    }

    public void setSectorId(int sectorId) {
        this.sectorId = sectorId;
    }
}
