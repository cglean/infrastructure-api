package com.cglean.infrastructure.controller;

import static com.cglean.Constants.dateFormat;

import java.time.LocalDate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cglean.infrastructure.domain.Cost;
import com.cglean.infrastructure.domain.TimeInterval;
import com.cglean.infrastructure.service.InfrastructureService;

@RestController
@RefreshScope
public class CostController {
	private Log log = LogFactory.getLog(CostController.class);

	@Value("${config.server.bind.test:DISCONNECTED}")
	private String bindTest;

	@Autowired
	InfrastructureService service;

	@RequestMapping(value = "/cost")
	public Cost findCost(@RequestParam(required = false) @DateTimeFormat(pattern = dateFormat) LocalDate start,
			@RequestParam(required = false) @DateTimeFormat(pattern = dateFormat) LocalDate end) {
		log.info("Configured value for bind test is " + bindTest);
		if (start == null && end == null) {
			end = LocalDate.now();
			start = end.minusMonths(1);
		}
		if (end == null) {
			end = start.plusMonths(1);
		}
		if (start == null) {
			start = end.minusMonths(1);
		}

		TimeInterval interval = new TimeInterval();
		interval.setStart(start);
		interval.setEnd(end);
		return service.findCostForPeriod(interval);
	}
}
