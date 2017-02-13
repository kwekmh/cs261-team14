package uk.ac.warwick.dcs.cs261.team14.data.pipeline;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.warwick.dcs.cs261.team14.data.transformers.IndividualTradeDataTransformer;
import uk.ac.warwick.dcs.cs261.team14.learning.IndividualTradeLearningModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ming on 2/12/2017.
 */
@Component
public class IndividualTradeDataPipelineModel implements DataPipelineModel {
    @Autowired
    IndividualTradeDataTransformer individualTradeDataTransformer;

    @Autowired
    IndividualTradeLearningModel individualTradeLearningModel;

    private String[] schemaFields = {"time", "buyer", "seller", "price", "size", "currency", "symbol", "sector", "bid", "ask" };
    private StructType schema;

    public IndividualTradeDataPipelineModel() {
        List<StructField> fields = new ArrayList<StructField>();
        for (String fieldName : schemaFields) {
            StructField field = DataTypes.createStructField(fieldName, DataTypes.StringType, true);
            fields.add(field);
        }

        schema = DataTypes.createStructType(fields);
    }

    @Override
    public Dataset<Row> processRDD(JavaRDD<Row> rdd) {
        SparkSession spark = SparkSession.builder().master("local").appName("uk.ac.warwick.dcs.cs261.team14.IndividualTradeLearningModel").getOrCreate();
        Dataset<Row> df = spark.createDataFrame(rdd, schema);
        return individualTradeLearningModel.predict(df);
    }

    @Override
    public Row processLine(String input) {
        Row row = individualTradeDataTransformer.transform(input);
        if (row != null) {
            return individualTradeLearningModel.predictRow(row);
        } else {
            return null;
        }
    }
}
