package com.cglean.infrastructure.dao.vsphere;

import java.util.Collection;

import com.cglean.infrastructure.dao.CostDAO;
import com.cglean.infrastructure.domain.DailyCost;
import com.cglean.infrastructure.domain.TimeInterval;

public class CostDAOImpl implements CostDAO {

	@Override
	public Collection<DailyCost> findCost(TimeInterval interval) {
		throw new UnsupportedOperationException("Implementation of VSphere not available.");
	}

}
