package com.cglean.infrastructure.repository;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cglean.infrastructure.InfrastructureApiApplication;
import com.cglean.infrastructure.domain.DailyCost;
import com.cglean.infrastructure.repository.DailyCostRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InfrastructureApiApplication.class)
@TestPropertySource(properties = "spring.jpa.showSql=true")
public class DailyCostRepositoryTests {

	@Autowired
	private DailyCostRepository repository;

	@Test
	public void saveAndFind() {

		repository.deleteAll();

		DailyCost first = new DailyCost();
		first.setCost(BigDecimal.TEN);
		first.setCostDate(LocalDate.now());
		repository.save(first);

		DailyCost second = new DailyCost();
		second.setCost(BigDecimal.ONE);
		second.setCostDate(LocalDate.now().minusDays(1));
		repository.save(second);

		List<DailyCost> costs = repository.findAll();
		assertThat(costs.size(), equalTo(2));

	}

}
