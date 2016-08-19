package com.cglean.infrastructure.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class DailyCostSpecification {

	public static Specification<DailyCost> withDateWindow(final TimeInterval interval) {
		return new Specification<DailyCost>() {
			@Override
			public Predicate toPredicate(Root<DailyCost> cost, CriteriaQuery<?> query, CriteriaBuilder builder) {
				LocalDate start = interval.getStart();
				LocalDate end = interval.getEnd();
				if (start == null && end == null) {
					throw new IllegalStateException("One of start or end dates are required to construct this query");
				}
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (start != null) {
					predicates.add(builder.and(builder.greaterThanOrEqualTo(cost.get(DailyCost_.costDate), start)));
				}
				if (end != null) {
					predicates.add(builder.and(builder.lessThanOrEqualTo(cost.get(DailyCost_.costDate), end)));
				}
				return builder.and(predicates.stream().toArray(size -> new Predicate[size]));
			}
		};
	}

}
