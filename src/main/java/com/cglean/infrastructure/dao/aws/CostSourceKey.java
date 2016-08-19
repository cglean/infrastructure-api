package com.cglean.infrastructure.dao.aws;

import com.cglean.infrastructure.domain.TimeInterval;

import lombok.Data;

@Data
public class CostSourceKey {
	String key;
	TimeInterval interval;
	String guid;
}
