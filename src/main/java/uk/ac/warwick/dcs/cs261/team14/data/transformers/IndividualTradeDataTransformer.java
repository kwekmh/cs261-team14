package uk.ac.warwick.dcs.cs261.team14.data.transformers;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.warwick.dcs.cs261.team14.db.entities.Trade;
import uk.ac.warwick.dcs.cs261.team14.db.entities.TradeRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ming on 2/12/2017.
 */

@Component
public class IndividualTradeDataTransformer implements DataTransformer {
    private String[] schemaFields = { "time", "buyer", "seller", "price", "size", "currency", "symbol", "sector", "bid", "ask", "orig_time" };
    private StructType schema;

    @Autowired
    DataTransformerMapping dataTransformerMapping;

    @Autowired
    TradeRepository tradeRepository;

    public IndividualTradeDataTransformer() {
        List<StructField> fields = new ArrayList<StructField>();
        for (String fieldName : schemaFields) {
            StructField field = DataTypes.createStructField(fieldName, DataTypes.StringType, true);
            fields.add(field);
        }

        schema = DataTypes.createStructType(fields);
    }

    public Row transform(String input) {
        String[] vals = input.split(",");
        String[] newVals = new String[schemaFields.length];
        if (!vals[0].equals("time")) {
            Row row = null;
            try {
                int i;
                for (i = 0; i < vals.length; i++) {
                    newVals[i] = vals[i];
                }
                newVals[i] = vals[0];
                newVals[1] = Integer.toString(dataTransformerMapping.getTraderIndex(newVals[1]));
                newVals[2] = Integer.toString(dataTransformerMapping.getTraderIndex(newVals[2]));
                newVals[5] = Integer.toString(dataTransformerMapping.getCurrencyIndex(newVals[5]));
                newVals[6] = Integer.toString(dataTransformerMapping.getSymbolIndex(newVals[6]));
                newVals[7] = Integer.toString(dataTransformerMapping.getSectorIndex(newVals[7]));
                row = RowFactory.create(newVals);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return row;
        } else {
            return null;
        }
    }

    private double getPriceChangeOverLastFive(double price, int symbolId) {
        double sum = 0;
        int count = 0;

        for (Trade trade : tradeRepository.findTop5BySymbolIdOrderByTimeDesc(symbolId)) {
            sum += trade.getPrice();
            count++;
        }

        if (count > 0) {
            double average = sum / count;
            return (Math.abs((price - average)) / average) * 100;
        } else {
            return 0;
        }
    }
}
