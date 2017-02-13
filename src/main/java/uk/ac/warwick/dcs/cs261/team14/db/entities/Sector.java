package uk.ac.warwick.dcs.cs261.team14.db.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by Ming on 2/12/2017.
 */

@Entity
@Table(name = "sector")
public class Sector {
    @Id
    private int sectorId;

    @NotNull
    private String sectorName;

    public Sector() {

    }

    public Sector(int sectorId) {
        this.sectorId = sectorId;
    }

    public Sector(String sectorName) {
        this.sectorName = sectorName;
    }

    public int getSectorId() {
        return sectorId;
    }

    public void setSectorId(int sectorId) {
        this.sectorId = sectorId;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }
}
