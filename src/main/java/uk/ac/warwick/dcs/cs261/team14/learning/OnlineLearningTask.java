package uk.ac.warwick.dcs.cs261.team14.learning;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.ac.warwick.dcs.cs261.team14.db.entities.SymbolRepository;
import uk.ac.warwick.dcs.cs261.team14.db.entities.Trade;
import uk.ac.warwick.dcs.cs261.team14.db.entities.TradeRepository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ming on 2/23/2017.
 */
@Component
public class OnlineLearningTask {
    private String[] schemaFields = {"time", "buyer", "seller", "price", "size", "currency", "symbol", "sector", "bid", "ask", "pct_price_change", "is_anomalous" };
    private StructType schema;

    @Autowired
    IndividualTradeLearningModel individualTradeLearningModel;

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    SymbolRepository symbolRepository;

    public OnlineLearningTask() {
        List<StructField> fields = new ArrayList<StructField>();
        for (String fieldName : schemaFields) {
            StructField field = DataTypes.createStructField(fieldName, DataTypes.StringType, true);
            fields.add(field);
        }

        schema = DataTypes.createStructType(fields);
    }

    @Async
    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/London")
    public void learn() {
        SparkSession spark = SparkSession.builder().master("local").appName("uk.ac.warwick.dcs.cs261.team14.IndividualTradeLearningModel").getOrCreate();

        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime yesterday = today.minusDays(1);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");

        ArrayList<Row> rows = new ArrayList<Row>();

        String[] vals = new String[schemaFields.length];

        for (Trade trade : tradeRepository.findByTimeBetween(Timestamp.valueOf(yesterday), Timestamp.valueOf(today))) {
            vals[0] = sdf.format(trade.getTime());
            vals[1] = Integer.toString(trade.getBuyerId());
            vals[2] = Integer.toString(trade.getSellerId());
            vals[3] = Double.toString(trade.getPrice());
            vals[4] = Integer.toString(trade.getSize());
            vals[5] = Integer.toString(trade.getCurrencyId());
            vals[6] = Integer.toString(trade.getSymbolId());
            vals[7] = Integer.toString(symbolRepository.findBySymbolId(trade.getSymbolId()).getSectorId());
            vals[8] = Double.toString(trade.getBidPrice());
            vals[9] = Double.toString(trade.getAskPrice());
            vals[10] = Double.toString(trade.getPctPriceChange());
            vals[11] = Integer.toString(trade.getIsAnomalous());

            Row row = RowFactory.create(vals);
            rows.add(row);
        }

        Dataset<Row> df = spark.createDataFrame(rows, schema);

        individualTradeLearningModel.learn(df);
    }
}
