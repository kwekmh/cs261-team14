package uk.ac.warwick.dcs.cs261.team14.data.transformers;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * Created by Ming on 2/12/2017.
 */
public interface DataTransformer {
    Dataset<Row> transform(Dataset<Row> input);
}
