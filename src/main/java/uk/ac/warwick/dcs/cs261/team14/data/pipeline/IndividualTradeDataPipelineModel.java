package uk.ac.warwick.dcs.cs261.team14.data.pipeline;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.warwick.dcs.cs261.team14.Application;
import uk.ac.warwick.dcs.cs261.team14.data.transformers.DataTransformerMapping;
import uk.ac.warwick.dcs.cs261.team14.data.transformers.IndividualTradeDataTransformer;
import uk.ac.warwick.dcs.cs261.team14.db.entities.*;
import uk.ac.warwick.dcs.cs261.team14.learning.IndividualTradeLearningModel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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

    @Autowired
    private TraderRepository traderRepository;

    @Autowired
    private SymbolRepository symbolRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private DataTransformerMapping dataTransformerMapping;

    private final Logger logger = LoggerFactory.getLogger(Application.class);

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

    @Transactional
    @Override
    public Row processLine(String input) {
        String[] vals = input.split(",");
        boolean error = false;
        if (!vals[0].equals("time")) {
            if (dataTransformerMapping.getTraderIndex(vals[1]) == null) {
                Trader trader = new Trader(vals[1]);
                trader = traderRepository.save(trader);
                dataTransformerMapping.addTrader(vals[1], trader.getTraderId());
                error = true;
            }
            if (dataTransformerMapping.getTraderIndex(vals[2]) == null) {
                Trader trader = new Trader(vals[2]);
                trader = traderRepository.save(trader);
                dataTransformerMapping.addTrader(vals[2], trader.getTraderId());
                error = true;
            }
            if (dataTransformerMapping.getSectorIndex(vals[7]) == null) {
                Sector sector = new Sector(vals[7]);
                sector = sectorRepository.save(sector);
                dataTransformerMapping.addSector(vals[7], sector.getSectorId());
            }
            if (dataTransformerMapping.getSymbolIndex(vals[6]) == null) {
                Symbol symbol = new Symbol(vals[6], dataTransformerMapping.getSectorIndex(vals[7]));
                symbol = symbolRepository.save(symbol);
                dataTransformerMapping.addSymbol(vals[6], symbol.getSymbolId());
            }
            if (dataTransformerMapping.getCurrencyIndex(vals[5]) == null) {
                Currency currency = new Currency(vals[5]);
                currency = currencyRepository.save(currency);
                dataTransformerMapping.addCurrency(vals[5], currency.getCurrencyId());
            }
            if (!error) {
                Row row = individualTradeDataTransformer.transform(input);
                if (row != null) {
                    return individualTradeLearningModel.predictRow(row);
                } else {
                    return null;
                }
            } else {
                logger.info("Unknown indexes, creating new");
                Row row = individualTradeDataTransformer.transform(input);
                return individualTradeLearningModel.createRow(row);
            }
        } else {
            return null;
        }
    }
}
