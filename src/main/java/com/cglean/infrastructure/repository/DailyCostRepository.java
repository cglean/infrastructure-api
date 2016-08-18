package com.cglean.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cglean.infrastructure.domain.DailyCost;

public interface DailyCostRepository extends JpaRepository<DailyCost, Long> {

}
