package uk.ac.warwick.dcs.cs261.team14.learning;

import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.classification.GBTClassificationModel;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.apache.spark.sql.types.DataTypes.*;

/**
 * Created by Ming on 2/12/2017.
 */
@Component
public class IndividualTradeLearningModel implements LearningModel {
    @Value("${cs261.learning.models.directory}")
    private String modelsDirectory;

    private String[] schemaFields = {"time", "buyer", "seller", "price", "size", "currency", "symbol", "sector", "bid", "ask", "pct_price_change", "orig_time" };
    private StructType schema;

    public IndividualTradeLearningModel() {
        List<StructField> fields = new ArrayList<StructField>();
        for (String fieldName : schemaFields) {
            StructField field = DataTypes.createStructField(fieldName, DataTypes.StringType, true);
            fields.add(field);
        }

        schema = DataTypes.createStructType(fields);
    }

    public Dataset<Row> predict(Dataset<Row> rows) {
        SparkSession spark = SparkSession.builder().master("local").appName("uk.ac.warwick.dcs.cs261.team14.IndividualTradeLearningModel").getOrCreate();

        Dataset<Row> df = rows.withColumn("time", rows.col("time").cast(IntegerType));

        PipelineModel model = PipelineModel.load(getModelPath(modelsDirectory));

        Dataset<Row> transformed = model.transform(df);

        return transformed;
    }

    public Row predictRow(Row input) {
        SparkSession spark = SparkSession.builder().master("local").appName("uk.ac.warwick.dcs.cs261.team14.IndividualTradeLearningModel").getOrCreate();

        List<Row> rows = new ArrayList<Row>();

        rows.add(input);

        Dataset<Row> df = spark.createDataFrame(rows, schema);

        df = df.withColumn("time", df.col("time").cast(TimestampType));
        df = df.withColumn("time", df.col("time").cast(IntegerType));
        df = df.withColumn("price", df.col("price").cast(DoubleType));
        df = df.withColumn("size", df.col("size").cast(IntegerType));
        df = df.withColumn("currency", df.col("currency").cast(IntegerType));
        df = df.withColumn("symbol", df.col("symbol").cast(IntegerType));
        df = df.withColumn("sector", df.col("sector").cast(IntegerType));
        df = df.withColumn("bid", df.col("bid").cast(DoubleType));
        df = df.withColumn("ask", df.col("ask").cast(DoubleType));
        df = df.withColumn("pct_price_change", df.col("pct_price_change").cast(DoubleType));

        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(new String[] {"time","price","currency","symbol","sector","bid","ask","pct_price_change"})
                .setOutputCol("features");

        df = assembler.transform(df);

        String modelPath = getModelPath(modelsDirectory);

        GBTClassificationModel model = GBTClassificationModel.load(modelPath);

        Dataset<Row> transformed = model.transform(df);

        return transformed.first();
    }

    private String getModelPath(String modelsDirectory) {
        if (modelsDirectory.indexOf(modelsDirectory.length() - 1) != '\\') {
            return modelsDirectory + "\\gbt_model_individual_trades";
        } else {
            return modelsDirectory + "gbt_model_individual_trades";
        }
    }
}
