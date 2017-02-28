package uk.ac.warwick.dcs.cs261.team14.db.entities;

import java.sql.Timestamp;

/**
 * Created by Ming on 2/26/2017.
 */
public interface AnomalousEvent {
    Timestamp getTime();
    int getId();
    int getSymbolId();
    int getAnomalousEventType();
}
