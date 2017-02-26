package uk.ac.warwick.dcs.cs261.team14.db.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Created by Ming on 2/23/2017.
 */

@Entity
@Table(name = "aggregate_data")
public class AggregateData implements AnomalousEvent {
    @Id
    private int aggregateDataId;

    @NotNull
    private int typeId;

    @NotNull
    private int symbolId;

    @NotNull
    private double value;

    @NotNull
    private Timestamp generatedDate;

    @NotNull
    private int isAnomalous;

    public AggregateData() {

    }

    public AggregateData(int typeId) {
        this.typeId = typeId;
    }

    public AggregateData(int typeId, int symbolId, double value, int isAnomalous, Timestamp generatedDate) {
        this.typeId = typeId;
        this.symbolId = symbolId;
        this.value = value;
        this.generatedDate = generatedDate;
        this.isAnomalous = isAnomalous;
    }

    public int getAggregateDataId() {
        return aggregateDataId;
    }

    public void setAggregateDataId(int aggregateDataId) {
        this.aggregateDataId = aggregateDataId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(int symbolId) {
        this.symbolId = symbolId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Timestamp getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(Timestamp generatedDate) {
        this.generatedDate = generatedDate;
    }

    public int getIsAnomalous() {
        return isAnomalous;
    }

    public void setIsAnomalous(int isAnomalous) {
        this.isAnomalous = isAnomalous;
    }

    public Timestamp getTime() {
        return getGeneratedDate();
    }

    public int getAnomalousEventType() {
        return 2;
    }
}
