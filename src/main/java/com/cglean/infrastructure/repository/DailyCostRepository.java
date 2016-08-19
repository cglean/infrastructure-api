package com.cglean.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.cglean.infrastructure.domain.DailyCost;

@RepositoryRestResource(path = "/dailycost")
public interface DailyCostRepository extends JpaRepository<DailyCost, Long>, JpaSpecificationExecutor<DailyCost> {
	public List<DailyCost> findTop1ByOrderByCostDateDesc();

	public List<DailyCost> findTop1ByOrderByCostDateAsc();
}
