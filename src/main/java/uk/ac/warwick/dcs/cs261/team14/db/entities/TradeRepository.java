package uk.ac.warwick.dcs.cs261.team14.db.entities;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Ming on 2/12/2017.
 */
@Transactional
public interface TradeRepository extends CrudRepository<Trade, Integer>, Serializable {
    Iterable<Trade> findTop5BySymbolIdOrderByTimeDesc(int symbolId);
    Trade findTop1BySymbolIdOrderByTimeDesc(int symbolId);
    Iterable<Trade> findByTimeBetween(Timestamp start, Timestamp end);
    Iterable<Trade> findBySymbolIdAndTimeBetween(int symbolId, Timestamp start, Timestamp end);
    Iterable<Trade> findTop10ByIsAnomalousOrderByTimeDesc(int isAnomalous);
    Iterable<Trade> findByIsAnomalousAndTimeBetween(int isAnomalous, Timestamp start, Timestamp end);
    Iterable<Trade> findByIsAnomalousAndSymbolIdAndTimeBetween(int isAnomalous, int symbolId, Timestamp start, Timestamp end);
    Iterable<Trade> findByBuyerIdOrSellerIdAndTimeBetween(int buyerId, int sellerId, Timestamp start, Timestamp end);
    Page<Trade> findByIsAnomalousOrderByTimeDesc(int isAnonalous, Pageable pageable);
}
