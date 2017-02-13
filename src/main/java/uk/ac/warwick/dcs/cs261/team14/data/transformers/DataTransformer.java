package uk.ac.warwick.dcs.cs261.team14.data.transformers;

import org.apache.spark.sql.Row;

import java.io.Serializable;

/**
 * Created by Ming on 2/12/2017.
 */
public interface DataTransformer extends Serializable {
    Row transform(String input);
}
