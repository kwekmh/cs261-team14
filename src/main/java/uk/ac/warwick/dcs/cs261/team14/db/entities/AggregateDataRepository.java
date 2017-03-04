package uk.ac.warwick.dcs.cs261.team14.db.entities;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.io.Serializable;

/**
 * Created by Ming on 2/23/2017.
 */

@Transactional
public interface AggregateDataRepository extends CrudRepository<AggregateData, Integer>, Serializable {
    AggregateData findTop1ByTypeIdAndSymbolIdOrderByGeneratedDateDesc(int typeId, int symbolId);
    Iterable<AggregateData> findTop10ByIsAnomalousOrderByGeneratedDateDesc(int isAnomalous);
    Page<AggregateData> findByIsAnomalousOrderByGeneratedDateDesc(int isAnomalous, Pageable pageable);
}
