package com.cglean.infrastructure.dao.aws;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.cglean.infrastructure.domain.TimeInterval;
import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class ConvertToTimeInterval extends AbstractBeanField<TimeInterval> {

	// 2016-08-08T00:00:00Z
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

	@Override
	protected Object convert(String value)
			throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, CsvConstraintViolationException {
		String[] duration = value.split("/");
		return new TimeInterval(LocalDate.parse(duration[0], formatter), LocalDate.parse(duration[1], formatter));
	}
}