package com.cglean.infrastructure.controller;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cglean.infrastructure.InfrastructureApiApplication;
import com.cglean.infrastructure.domain.TimeInterval;
import com.cglean.infrastructure.service.InfrastructureService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InfrastructureApiApplication.class)
public class CostControllerTests {

	@Mock
	private InfrastructureService service;

	@InjectMocks
	@Autowired
	private CostController controller;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void whenDatesAreNotSuppliedControllerInitializesDefaults() {
		controller.findCost(null, null);
		TimeInterval timeInterval = new TimeInterval(LocalDate.now().minusMonths(1), LocalDate.now());
		Mockito.verify(service).findCostForPeriod((Mockito.eq(timeInterval)));
	}

}
