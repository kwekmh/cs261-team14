package uk.ac.warwick.dcs.cs261.team14.data.pipeline;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by Ming on 2/12/2017.
 */
@Component
public interface DataPipelineModel extends Serializable {
    Dataset<Row> processRDD(JavaRDD<Row> rdd);
    Row processLine(String input);
}
