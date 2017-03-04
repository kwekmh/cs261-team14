package uk.ac.warwick.dcs.cs261.team14.data.transformers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.warwick.dcs.cs261.team14.db.entities.*;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Ming on 2/12/2017.
 */
@Component
public class DataTransformerMapping implements Serializable {
    private HashMap<String, Integer> sectorMapping;
    private HashMap<String, Integer> symbolMapping;
    private HashMap<String, Integer> currencyMapping;
    private HashMap<String, Integer> traderMapping;

    @Autowired
    SectorRepository sectorRepository;

    @Autowired
    SymbolRepository symbolRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    TraderRepository traderRepository;

    @PostConstruct
    public void init() {
        if (sectorMapping == null) {
            sectorMapping = new HashMap<>();
            for (Sector sector : sectorRepository.findAll()) {
                sectorMapping.put(sector.getSectorName(), sector.getSectorId());
            }
        }
        if (symbolMapping == null) {
            symbolMapping = new HashMap<>();
            for (Symbol symbol : symbolRepository.findAll()) {
                symbolMapping.put(symbol.getSymbolName(), symbol.getSymbolId());
            }
        }

        if (currencyMapping == null) {
            currencyMapping = new HashMap<>();
            for (Currency currency : currencyRepository.findAll()) {
                currencyMapping.put(currency.getCurrencyName(), currency.getCurrencyId());
            }
        }

        if (traderMapping == null) {
            traderMapping = new HashMap<>();
            for (Trader trader : traderRepository.findAll()) {
                traderMapping.put(trader.getTraderEmailAddress(), trader.getTraderId());
            }
        }
    }

    public Integer getSectorIndex(String sector) {
        return sectorMapping.get(sector);
    }

    public Integer getSymbolIndex(String symbol) {
        return symbolMapping.get(symbol);
    }

    public Integer getCurrencyIndex(String currency) {
        return currencyMapping.get(currency);
    }

    public Integer getTraderIndex(String trader) {
        return traderMapping.get(trader);
    }

    public void addSector(String sector, int sectorId) {
        sectorMapping.put(sector, sectorId);
    }

    public void addSymbol(String symbol, int symbolId) {
        symbolMapping.put(symbol, symbolId);
    }

    public void addCurrency(String currency, int currencyId) {
        currencyMapping.put(currency, currencyId);
    }

    public void addTrader(String trader, int traderId) {
        traderMapping.put(trader, traderId);
    }
}
