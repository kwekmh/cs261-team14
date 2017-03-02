package uk.ac.warwick.dcs.cs261.team14.db.entities;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by kwekmh on 28/02/17.
 */
@Transactional
public interface TraderRepository extends CrudRepository<Trader, Integer> {
}
