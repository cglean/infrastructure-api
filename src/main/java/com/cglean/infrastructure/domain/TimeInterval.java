package com.cglean.infrastructure.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeInterval {
	LocalDate start;
	LocalDate end;

	public boolean overlapsWith(TimeInterval overlap) {
		if (isWithin(overlap.start) || isWithin(overlap.end)) {
			return true;
		}
		if (overlap.isWithin(this.start) || overlap.isWithin(this.end)) {
			return true;
		}
		return false;
	}

	public boolean isWithin(LocalDate date) {
		return (date.isAfter(start) && date.isBefore(end)) || date.isEqual(end) || date.isEqual(start);
	}

}
