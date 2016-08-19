package com.cglean.infrastructure.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cglean.infrastructure.domain.Cost;
import com.cglean.infrastructure.domain.TimeInterval;
import com.cglean.infrastructure.service.InfrastructureService;

@RestController
public class CostController {

	final String dateFormat = "yyyy-MM-dd";

	@Autowired
	InfrastructureService service;

	@RequestMapping(value = "/cost")
	public Cost findCost(@RequestParam(required = false) @DateTimeFormat(pattern = dateFormat) LocalDate start,
			@RequestParam(required = false) @DateTimeFormat(pattern = dateFormat) LocalDate end) {
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
