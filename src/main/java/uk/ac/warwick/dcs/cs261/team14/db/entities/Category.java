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
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue
    private int categoryId;

    @NotNull
    private String categoryName;

    public Category() {

    }

    public Category(int categoryId) {
        this.categoryId = categoryId;
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
