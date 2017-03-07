package uk.ac.warwick.dcs.cs261.team14.data.pipeline;

import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by Ming on 2/12/2017.
 */

@Component
public class InputController implements Serializable {


    @Autowired
    IndividualTradeDataPipelineModel individualTradeDataPipelineModel;

    public InputController() {

    }

    public void processFile(File file) {

    }

    public Row getAsRow(String input) {
        return individualTradeDataPipelineModel.getAsRow(input);
    }

    public Row processLine(String input) {
        return individualTradeDataPipelineModel.processLine(input);
    }
}
