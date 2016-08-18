package com.cglean.infrastructure.service;

import java.time.LocalDate;

import com.cglean.infrastructure.domain.Cost;

public interface InfrastructureService {

	Cost getCost(LocalDate start, LocalDate end);
}
