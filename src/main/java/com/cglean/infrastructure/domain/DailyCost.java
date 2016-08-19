package com.cglean.infrastructure.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class DailyCost {

	@Id
	private LocalDate costDate;

	private BigDecimal cost;

	public void accumulate(BigDecimal blendedCost) {
		cost = cost.add(blendedCost);
	}
}
