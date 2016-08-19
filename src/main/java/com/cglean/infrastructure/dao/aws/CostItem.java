package com.cglean.infrastructure.dao.aws;

import java.math.BigDecimal;

import com.cglean.infrastructure.domain.TimeInterval;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import lombok.Data;

@Data
public class CostItem {
	@CsvBindByName(column = "identity/LineItemId")
	String identityLineItemId;

	@CsvCustomBindByName(column = "identity/TimeInterval", converter = ConvertToTimeInterval.class)
	TimeInterval interval;
	
	@CsvBindByName(column = "lineItem/BlendedCost")
	BigDecimal blendedCost;
}
