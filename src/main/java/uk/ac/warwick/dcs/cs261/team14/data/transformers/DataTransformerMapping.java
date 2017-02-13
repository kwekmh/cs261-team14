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

    @Autowired
    SectorRepository sectorRepository;

    @Autowired
    SymbolRepository symbolRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    @PostConstruct
    public void init() {
        if (sectorMapping == null) {
            sectorMapping = new HashMap<String, Integer>();
            for (Sector sector : sectorRepository.findAll()) {
                sectorMapping.put(sector.getSectorName(), sector.getSectorId());
            }
        }
        if (symbolMapping == null) {
            symbolMapping = new HashMap<String, Integer>();
            for (Symbol symbol : symbolRepository.findAll()) {
                symbolMapping.put(symbol.getSymbolName(), symbol.getSymbolId());
            }
        }

        if (currencyMapping == null) {
            currencyMapping = new HashMap<String, Integer>();
            for (Currency currency : currencyRepository.findAll()) {
                currencyMapping.put(currency.getCurrencyName(), currency.getCurrencyId());
            }
        }
    }

    public int getSectorIndex(String sector) {
        return sectorMapping.get(sector);
    }

    public int getSymbolIndex(String symbol) {
        return symbolMapping.get(symbol);
    }

    public int getCurrencyIndex(String currency) {
        return currencyMapping.get(currency);
    }
}
