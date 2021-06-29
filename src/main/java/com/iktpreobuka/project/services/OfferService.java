package com.iktpreobuka.project.services;

import java.util.List;

import com.iktpreobuka.project.entities.OfferEntity;

public interface OfferService {

	// T4 1.2
	public OfferEntity changeNumAvailableAndNumBought(Integer id, Integer available, Integer bought);
	public List<OfferEntity> findNonExpired();
}
