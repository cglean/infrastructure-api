package com.cglean.infrastructure.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(DailyCost.class)
public class DailyCost_ {
	public static volatile SingularAttribute<DailyCost, Long> id;
	public static volatile SingularAttribute<DailyCost, LocalDate> costDate;
	public static volatile SingularAttribute<DailyCost, BigDecimal> cost;
}
