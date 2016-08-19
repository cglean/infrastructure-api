package com.cglean.infrastructure.dao;

import java.util.Collection;

import com.cglean.infrastructure.domain.DailyCost;
import com.cglean.infrastructure.domain.TimeInterval;

public interface CostDAO {

	Collection<DailyCost> findCost(TimeInterval interval);
}
