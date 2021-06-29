package com.iktpreobuka.project.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.iktpreobuka.project.entities.BillEntity;
import com.iktpreobuka.project.entities.OfferEntity;

@Service
public class BillServiceImpl implements BillService {

	
	@PersistenceContext
	private EntityManager em;
	
	
	// T4 2.4
	@Override
	public List<BillEntity> findByPeriod(LocalDate start, LocalDate end) {
		
		String hql = "SELECT b FROM BillEntity b WHERE b.billCreated BETWEEN :startX AND :endX";
		Query query = em.createQuery(hql);
		query.setParameter("startX", start);
		query.setParameter("endX", end);
		
		List<BillEntity> bills = new ArrayList<BillEntity>();
		bills = query.getResultList();
		
		return bills;
	}


	// T4 3.1
	@Override
	public List<BillEntity> findNonPaid() {
		
		String hql = "SELECT b FROM BillEntity b WHERE b.paymentMade = false";
		Query query = em.createQuery(hql);
		
		List<BillEntity> bills = new ArrayList<BillEntity>();
		bills = query.getResultList();
		
		return bills;
	}
}
