package uk.ac.warwick.dcs.cs261.team14.data.pipeline;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import uk.ac.warwick.dcs.cs261.team14.data.transformers.DataTransformer;
import uk.ac.warwick.dcs.cs261.team14.learning.LearningModel;

/**
 * Created by Ming on 2/12/2017.
 */
public class DataPipelineModel {
    private DataTransformer dataTransformer;
    private LearningModel learningModel;

    public void process(Dataset<Row> input) {

    }

    public void processLine(String input) {

    }
}
