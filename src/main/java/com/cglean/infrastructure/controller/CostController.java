package com.cglean.infrastructure.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cglean.infrastructure.domain.Cost;

@RestController
public class CostController {

	final String dateFormat = "yyyy-MM-dd";

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

		Cost mockCost = new Cost();
		mockCost.setStart(start);
		mockCost.setEnd(end);
		mockCost.setTotal("$1000");
		mockCost.setCpu("$200");
		mockCost.setMemory("$700");
		mockCost.setDisk("$100");
		return mockCost;
	}
}
