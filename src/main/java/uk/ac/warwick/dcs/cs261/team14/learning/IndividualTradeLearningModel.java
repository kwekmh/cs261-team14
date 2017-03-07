package uk.ac.warwick.dcs.cs261.team14.learning;

import org.apache.commons.io.FileUtils;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.classification.GBTClassificationModel;
import org.apache.spark.ml.classification.GBTClassifier;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
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

    @Value("${cs261.learning.checkpoints.directory}")
    private String checkpointsDirectory;

    private String[] schemaFields = {"time", "buyer", "seller", "price", "size", "currency", "symbol", "sector", "bid", "ask", "orig_time" };
    private StructType schema;

    private GBTClassificationModel model;

    public IndividualTradeLearningModel() {
        List<StructField> fields = new ArrayList<StructField>();
        for (String fieldName : schemaFields) {
            StructField field = DataTypes.createStructField(fieldName, DataTypes.StringType, true);
            fields.add(field);
        }

        schema = DataTypes.createStructType(fields);
    }

    @PostConstruct
    public void init() {
        SparkSession spark = SparkSession.builder().master("local").appName("uk.ac.warwick.dcs.cs261.team14.IndividualTradeLearningModel").getOrCreate();

        spark.sparkContext().setCheckpointDir(checkpointsDirectory);

        String modelPath = getModelPath(modelsDirectory);

        model = GBTClassificationModel.load(modelPath);
    }

    public Dataset<Row> predict(Dataset<Row> rows) {
        SparkSession spark = SparkSession.builder().master("local").appName("uk.ac.warwick.dcs.cs261.team14.IndividualTradeLearningModel").getOrCreate();

        spark.sparkContext().setCheckpointDir(checkpointsDirectory);

        Dataset<Row> df = rows.withColumn("time", rows.col("time").cast(IntegerType));

        PipelineModel model = PipelineModel.load(getModelPath(modelsDirectory));

        Dataset<Row> transformed = model.transform(df);

        return transformed;
    }

    public Row createRow(Row input) {
        SparkSession spark = SparkSession.builder().master("local").appName("uk.ac.warwick.dcs.cs261.team14.IndividualTradeLearningModel").getOrCreate();

        List<Row> rows = new ArrayList<Row>();

        rows.add(input);

        Dataset<Row> df = spark.createDataFrame(rows, schema);

        df = df.withColumn("time", df.col("time").cast(TimestampType));
        df = df.withColumn("time", df.col("time").cast(IntegerType));
        df = df.withColumn("buyer", df.col("buyer").cast(IntegerType));
        df = df.withColumn("seller", df.col("seller").cast(IntegerType));
        df = df.withColumn("price", df.col("price").cast(DoubleType));
        df = df.withColumn("size", df.col("size").cast(IntegerType));
        df = df.withColumn("currency", df.col("currency").cast(IntegerType));
        df = df.withColumn("symbol", df.col("symbol").cast(IntegerType));
        df = df.withColumn("sector", df.col("sector").cast(IntegerType));
        df = df.withColumn("bid", df.col("bid").cast(DoubleType));
        df = df.withColumn("ask", df.col("ask").cast(DoubleType));
        df = df.withColumn("features", org.apache.spark.sql.functions.lit(0.0));
        df = df.withColumn("is_anomalous", org.apache.spark.sql.functions.lit(0.0));

        return df.first();
    }

    public Row predictRow(Row input) {
        SparkSession spark = SparkSession.builder().master("local").appName("uk.ac.warwick.dcs.cs261.team14.IndividualTradeLearningModel").getOrCreate();

        spark.sparkContext().setCheckpointDir(checkpointsDirectory);

        List<Row> rows = new ArrayList<Row>();

        rows.add(input);

        Dataset<Row> df = spark.createDataFrame(rows, schema);

        df = df.withColumn("time", df.col("time").cast(TimestampType));
        df = df.withColumn("time", df.col("time").cast(IntegerType));
        df = df.withColumn("buyer", df.col("buyer").cast(IntegerType));
        df = df.withColumn("seller", df.col("seller").cast(IntegerType));
        df = df.withColumn("price", df.col("price").cast(DoubleType));
        df = df.withColumn("size", df.col("size").cast(IntegerType));
        df = df.withColumn("currency", df.col("currency").cast(IntegerType));
        df = df.withColumn("symbol", df.col("symbol").cast(IntegerType));
        df = df.withColumn("sector", df.col("sector").cast(IntegerType));
        df = df.withColumn("bid", df.col("bid").cast(DoubleType));
        df = df.withColumn("ask", df.col("ask").cast(DoubleType));

        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(new String[] {"time","buyer","seller","price","currency","symbol","sector","bid","ask"})
                .setOutputCol("features");

        df = assembler.transform(df);

        Dataset<Row> transformed = model.transform(df);

        return transformed.first();
    }

        public Dataset<Row> predictList(ArrayList<Row> rows) {
        SparkSession spark = SparkSession.builder().master("local").appName("uk.ac.warwick.dcs.cs261.team14.IndividualTradeLearningModel").getOrCreate();

        spark.sparkContext().setCheckpointDir(checkpointsDirectory);

        Dataset<Row> df = spark.createDataFrame(rows, schema);

        df = df.withColumn("time", df.col("time").cast(TimestampType));
        df = df.withColumn("time", df.col("time").cast(IntegerType));
        df = df.withColumn("buyer", df.col("buyer").cast(IntegerType));
        df = df.withColumn("seller", df.col("seller").cast(IntegerType));
        df = df.withColumn("price", df.col("price").cast(DoubleType));
        df = df.withColumn("size", df.col("size").cast(IntegerType));
        df = df.withColumn("currency", df.col("currency").cast(IntegerType));
        df = df.withColumn("symbol", df.col("symbol").cast(IntegerType));
        df = df.withColumn("sector", df.col("sector").cast(IntegerType));
        df = df.withColumn("bid", df.col("bid").cast(DoubleType));
        df = df.withColumn("ask", df.col("ask").cast(DoubleType));

        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(new String[] {"time","buyer","seller","price","currency","symbol","sector","bid","ask"})
                .setOutputCol("features");

        df = assembler.transform(df);

        Dataset<Row> transformed = model.transform(df);

        return transformed;
    }

    public void learn(Dataset<Row> df) {
        SparkSession spark = SparkSession.builder().master("local").appName("uk.ac.warwick.dcs.cs261.team14.IndividualTradeLearningModel").getOrCreate();

        spark.sparkContext().setCheckpointDir(checkpointsDirectory);

        df = df.withColumn("time", df.col("time").cast(TimestampType));
        df = df.withColumn("time", df.col("time").cast(IntegerType));
        df = df.withColumn("buyer", df.col("buyer").cast(IntegerType));
        df = df.withColumn("seller", df.col("seller").cast(IntegerType));
        df = df.withColumn("price", df.col("price").cast(DoubleType));
        df = df.withColumn("size", df.col("size").cast(IntegerType));
        df = df.withColumn("currency", df.col("currency").cast(IntegerType));
        df = df.withColumn("symbol", df.col("symbol").cast(IntegerType));
        df = df.withColumn("sector", df.col("sector").cast(IntegerType));
        df = df.withColumn("bid", df.col("bid").cast(DoubleType));
        df = df.withColumn("ask", df.col("ask").cast(DoubleType));
        df = df.withColumn("is_anomalous", df.col("is_anomalous").cast(IntegerType));

        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(new String[] {"time","buyer","seller","price","currency","symbol","sector","bid","ask"})
                .setOutputCol("features");

        df = assembler.transform(df);

        String modelPath = getModelPath(modelsDirectory);

        GBTClassifier gbt = new GBTClassifier()
                .setLabelCol("is_anomalous")
                .setFeaturesCol("features")
                .setMaxIter(300);

        GBTClassificationModel model = gbt.fit(df);

        this.model = model;

        try {
            // TODO: Ensure that no contention or race conditions can occur while attempting to delete the current model
            FileUtils.deleteDirectory(new File(modelPath));
            model.save(modelPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getModelPath(String modelsDirectory) {
        if (modelsDirectory.indexOf(modelsDirectory.length() - 1) != File.separatorChar) {
            return modelsDirectory + File.separator + "gbt_model_individual_trades";
        } else {
            return modelsDirectory + "gbt_model_individual_trades";
        }
    }
}
