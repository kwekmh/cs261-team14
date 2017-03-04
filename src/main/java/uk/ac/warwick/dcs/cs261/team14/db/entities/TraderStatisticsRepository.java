package uk.ac.warwick.dcs.cs261.team14.db.entities;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by kwekmh on 28/02/17.
 */
@Transactional
public interface TraderStatisticsRepository extends CrudRepository<TraderStatistics, Integer> {
    Iterable<TraderStatistics> findTop10ByIsAnomalousOrderByGeneratedDatetime(int isAnomalous);
    Page<TraderStatistics> findByIsAnomalousOrderByGeneratedDatetime(int isAnonalous, Pageable pageable);
}
