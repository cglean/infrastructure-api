package com.cglean.infrastructure.service.aws;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cglean.infrastructure.domain.Cost;
import com.cglean.infrastructure.repository.DailyCostRepository;
import com.cglean.infrastructure.service.InfrastructureService;

@Service
public class InfrastructureServiceAWS implements InfrastructureService {

	@Autowired
	DailyCostRepository repository;

	@Override
	public Cost getCost(LocalDate start, LocalDate end) {
		if (start == null || end == null) {
			throw new IllegalArgumentException("Both start and end date are required parameters to derive the cost.");
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
