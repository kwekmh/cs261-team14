package uk.ac.warwick.dcs.cs261.team14.db.entities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by kwekmh on 06/03/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class TradeRepositoryTest {
    @Autowired
    private TradeRepository tradeRepository;

    @Test
    public void contextLoads() throws Exception {
        assertThat(tradeRepository).isNotNull();
    }

    @Test
    public void returnsAnomalousResults() {
        assertTrue(tradeRepository.findByIsAnomalousGreaterThanOrderByTimeDesc(0, new PageRequest(0, 20)).getContent().stream().allMatch(t -> t.getIsAnomalous() > 0));
    }
}
