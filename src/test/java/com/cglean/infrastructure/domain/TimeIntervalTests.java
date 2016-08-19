package com.cglean.infrastructure.domain;

import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;

public class TimeIntervalTests {

	LocalDate past = LocalDate.now().minusMonths(1);
	LocalDate yesterday = LocalDate.now().minusDays(1);
	LocalDate today = LocalDate.now();
	LocalDate tomorrow = LocalDate.now().plusDays(1);
	LocalDate future = LocalDate.now().plusMonths(1);

	@Test
	public void nonIntersectingIntervalsShouldBeFalse() {
		TimeInterval pastInterval = new TimeInterval(past, yesterday);
		TimeInterval futureInterval = new TimeInterval(tomorrow, future);
		assertThat(pastInterval.overlapsWith(futureInterval), equalTo(false));
		assertThat(futureInterval.overlapsWith(pastInterval), equalTo(false));
	}

	@Test
	public void embeddedIntervalsShouldBeTrue() {
		TimeInterval largeRange = new TimeInterval(past, future);
		TimeInterval embedded = new TimeInterval(yesterday, tomorrow);
		assertThat(embedded.overlapsWith(largeRange), equalTo(true));
		assertThat(largeRange.overlapsWith(embedded), equalTo(true));
	}

	@Test
	public void touchingIntervalsShouldBeTrue() {
		TimeInterval pastWithToday = new TimeInterval(past, today);
		TimeInterval futureWithToday = new TimeInterval(today, future);
		assertThat(pastWithToday.overlapsWith(futureWithToday), equalTo(true));
		assertThat(futureWithToday.overlapsWith(pastWithToday), equalTo(true));
	}

}
