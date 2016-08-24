package com.cglean.infrastructure.service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cglean.Constants;
import com.cglean.infrastructure.dao.CostDAO;
import com.cglean.infrastructure.domain.Cost;
import com.cglean.infrastructure.domain.DailyCost;
import com.cglean.infrastructure.domain.DailyCostSpecification;
import com.cglean.infrastructure.domain.TimeInterval;
import com.cglean.infrastructure.repository.DailyCostRepository;

@Service
public class InfrastructureServiceImpl implements InfrastructureService {

	private Log log = LogFactory.getLog(InfrastructureServiceImpl.class);

	@Autowired
	CostDAO dao;

	@Value("${platform.cost:0.20}")
	double platformRatio;

	@Value("${platform.memory:0.60}")
	double memoryRatio;

	@Value("${platform.cpu:0.30}")
	double cpuRatio;

	@Autowired
	DailyCostRepository repository;

	@Override
	public Cost findCostForPeriod(TimeInterval interval) {
		log.info(String.format("Find cost between for interval %s", interval));

		Specification<DailyCost> dateWindow = DailyCostSpecification.withDateWindow(interval);
		long existingCosts = repository.count(dateWindow);

		TimeInterval daoInterval = new TimeInterval(interval.getStart(), interval.getEnd());
		boolean refresh = false;

		if (existingCosts > 0) {
			List<DailyCost> latest = repository.findTop1ByOrderByCostDateDesc();
			if (!latest.isEmpty()) {
				DailyCost last = latest.get(0);
				if (interval.getEnd().isAfter(last.getCostDate())) {
					refresh = true;
					daoInterval.setStart(last.getCostDate());
				}
			}

			List<DailyCost> oldest = repository.findTop1ByOrderByCostDateAsc();
			if (!oldest.isEmpty()) {
				DailyCost old = oldest.get(0);
				if (interval.getStart().isBefore(old.getCostDate())) {
					refresh = true;
				}
			}
		}

		if (existingCosts <= 0 || refresh) {
			dao.findCost(daoInterval).stream().forEach(cost -> repository.save(cost));
		}

		Cost costForPeriod = new Cost();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.dateFormat);
		costForPeriod.setStartDate(interval.getStart().format(formatter));
		costForPeriod.setEndDate(interval.getEnd().format(formatter));

		List<DailyCost> costs = repository.findAll(dateWindow);
		if (!costs.isEmpty()) {
			BigDecimal total = costs.stream().map(cost -> cost.getCost()).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal platform = total.multiply(BigDecimal.valueOf((platformRatio)));
			BigDecimal application = total.subtract(platform);
			BigDecimal memory = application.multiply(BigDecimal.valueOf((memoryRatio)));
			BigDecimal cpu = application.multiply(BigDecimal.valueOf((cpuRatio)));

			NumberFormat format = NumberFormat.getCurrencyInstance();
			costForPeriod.setTotal(format.format(total));
			costForPeriod.setPlatform(format.format(platform));
			costForPeriod.setApplication(format.format(application));
			costForPeriod.setMemory(format.format(memory));
			costForPeriod.setCpu(format.format(cpu));
			costForPeriod.setDisk(format.format(application.subtract(memory).subtract(cpu)));
		}

		log.info(String.format("The cost for period is %s", costForPeriod));
		return costForPeriod;
	}

}
