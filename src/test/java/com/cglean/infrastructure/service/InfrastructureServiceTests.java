package com.cglean.infrastructure.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cglean.infrastructure.InfrastructureApiApplication;
import com.cglean.infrastructure.dao.CostDAO;
import com.cglean.infrastructure.domain.Cost;
import com.cglean.infrastructure.domain.DailyCost;
import com.cglean.infrastructure.domain.TimeInterval;
import com.cglean.infrastructure.repository.DailyCostRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InfrastructureApiApplication.class)
@TestPropertySource(properties = "spring.jpa.showSql=true")
public class InfrastructureServiceTests {

	@Mock
	CostDAO dao;

	@InjectMocks
	@Autowired
	InfrastructureService service;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		repository.deleteAll();
	}

	@Autowired
	DailyCostRepository repository;

	//@Test
	public void findCostProgressingForward() {
		
		LocalDate yesterday = LocalDate.now().minusDays(1);
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = LocalDate.now().plusDays(1);

		DailyCost yesterdayCost = new DailyCost();
		yesterdayCost.setCost(BigDecimal.TEN);
		yesterdayCost.setCostDate(yesterday);
		repository.save(yesterdayCost);

		DailyCost todayCost = new DailyCost();
		todayCost.setCost(BigDecimal.ONE);
		todayCost.setCostDate(today);
		repository.save(todayCost);

		Cost cost = service.findCostForPeriod(new TimeInterval(yesterday, today));
		assertThat(cost.getTotal(),
				equalTo(NumberFormat.getCurrencyInstance().format(yesterdayCost.getCost().add(todayCost.getCost()))));

		DailyCost tomorrowCost = new DailyCost();
		tomorrowCost.setCost(new BigDecimal("3"));
		tomorrowCost.setCostDate(tomorrow);

		Collection<DailyCost> cos = new ArrayList<DailyCost>();
		cos.add(tomorrowCost);

		when(dao.findCost(any(TimeInterval.class))).thenReturn(cos);

		cost = service.findCostForPeriod(new TimeInterval(yesterday, tomorrow));
		assertThat(cost.getTotal(), equalTo(NumberFormat.getCurrencyInstance()
				.format(yesterdayCost.getCost().add(todayCost.getCost()).add(tomorrowCost.getCost()))));

	}

	@Test
	public void findCostProgressingBackward() {
		LocalDate yesterday = LocalDate.now().minusDays(1);
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = LocalDate.now().plusDays(1);

		DailyCost todayCost = new DailyCost();
		todayCost.setCost(BigDecimal.ONE);
		todayCost.setCostDate(today);
		repository.save(todayCost);

		DailyCost tomorrowCost = new DailyCost();
		tomorrowCost.setCost(new BigDecimal("3"));
		tomorrowCost.setCostDate(tomorrow);
		repository.save(tomorrowCost);

		Cost cost = service.findCostForPeriod(new TimeInterval(today, tomorrow));
		assertThat(cost.getTotal(),
				equalTo(NumberFormat.getCurrencyInstance().format(todayCost.getCost().add(tomorrowCost.getCost()))));

		DailyCost yesterdayCost = new DailyCost();
		yesterdayCost.setCost(BigDecimal.TEN);
		yesterdayCost.setCostDate(yesterday);

		Collection<DailyCost> cos = new ArrayList<DailyCost>();
		cos.add(yesterdayCost);

		when(dao.findCost(any(TimeInterval.class))).thenReturn(cos);

		cost = service.findCostForPeriod(new TimeInterval(yesterday, tomorrow));
		assertThat(cost.getTotal(), equalTo(NumberFormat.getCurrencyInstance()
				.format(yesterdayCost.getCost().add(todayCost.getCost()).add(tomorrowCost.getCost()))));

	}

}
