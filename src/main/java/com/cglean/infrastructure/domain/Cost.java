package com.cglean.infrastructure.domain;

import lombok.Data;

@Data
public class Cost {
	String startDate;
	String endDate;
	String total;
	String application;
	String platform;
	String cpu;
	String memory;
	String disk;
}
