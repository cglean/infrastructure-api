package com.cglean.infrastructure.service;

import com.cglean.infrastructure.domain.Cost;
import com.cglean.infrastructure.domain.TimeInterval;

public interface InfrastructureService {

	Cost findCostForPeriod(TimeInterval interval);
}
