package uk.ac.warwick.dcs.cs261.team14.db.entities;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.io.Serializable;

/**
 * Created by Ming on 2/12/2017.
 */

@Transactional
public interface SymbolRepository extends CrudRepository<Symbol, Integer>, Serializable {
    Symbol findBySymbolId(int symbolId);
}
