package com.iktpreobuka.project.services;

import java.time.LocalDate;
import java.util.List;

import com.iktpreobuka.project.entities.BillEntity;
import com.iktpreobuka.project.entities.OfferEntity;

public interface BillService {

	// T4 1.2
	public List<BillEntity> findByPeriod(LocalDate start, LocalDate end);
	public List<BillEntity> findNonPaid();
}
