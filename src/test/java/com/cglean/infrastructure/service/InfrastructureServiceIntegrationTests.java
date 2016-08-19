package com.cglean.infrastructure.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;

import com.cglean.infrastructure.InfrastructureApiApplication;
import com.cglean.infrastructure.domain.Cost;
import com.cglean.infrastructure.domain.DailyCost;
import com.cglean.infrastructure.domain.TimeInterval;
import com.cglean.infrastructure.repository.DailyCostRepository;

//@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InfrastructureApiApplication.class)
@TestPropertySource(properties = "spring.jpa.showSql=true")
public class InfrastructureServiceIntegrationTests {

	@Autowired
	private DailyCostRepository repository;

	@Autowired
	InfrastructureService service;

	//@Test
	public void findCostsForLastMonth() {

		DailyCost first = new DailyCost();
		first.setCost(BigDecimal.TEN);
		first.setCostDate(LocalDate.now().minusDays(2));
		repository.save(first);

		DailyCost second = new DailyCost();
		second.setCost(BigDecimal.ONE);
		second.setCostDate(LocalDate.now().minusDays(1));
		repository.save(second);

		LocalDate today = LocalDate.now();

		TimeInterval interval = new TimeInterval();
		interval.setEnd(today.minusDays(1));
		interval.setStart(today.minusMonths(1));

		Cost cost = service.findCostForPeriod(interval);
		assertThat(cost.getTotal(),
				equalTo(NumberFormat.getCurrencyInstance().format(first.getCost().add(second.getCost()))));

	}

}
