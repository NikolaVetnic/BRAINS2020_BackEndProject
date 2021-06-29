package com.iktpreobuka.project.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.project.entities.OfferEntity;
import com.iktpreobuka.project.repositories.OfferRepository;

@Service
public class OfferServiceImpl implements OfferService {

	
	@PersistenceContext
	private EntityManager em;
	
	
	@Autowired
	private OfferRepository offerRepository;
	

	// T4 2.1
	@Override
	public OfferEntity changeNumAvailableAndNumBought(Integer id, Integer available, Integer bought) {
		
		OfferEntity offer = offerRepository.findById(id).get();
		if (offer == null) return null;
		
		offer.setNumAvailable(available);
		offer.setNumBought(bought);
		
		return offer;
	}


	// T4 3.1
	@Override
	public List<OfferEntity> findNonExpired() {
		
		String hql = "SELECT o FROM OfferEntity o WHERE o.expires >= :nowX";
		Query query = em.createQuery(hql);
		query.setParameter("nowX", LocalDate.now());
		
		List<OfferEntity> offers = new ArrayList<OfferEntity>();
		offers = query.getResultList();
		
		return offers;
	}
}
