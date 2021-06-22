package com.iktpreobuka.project.controllers;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iktpreobuka.project.controllers.dto.BillRegisterDTO;
import com.iktpreobuka.project.entities.BillEntity;
import com.iktpreobuka.project.entities.CategoryEntity;
import com.iktpreobuka.project.entities.OfferEntity;
import com.iktpreobuka.project.entities.UserEntity;
import com.iktpreobuka.project.enums.Role;
import com.iktpreobuka.project.repositories.BillRepository;
import com.iktpreobuka.project.repositories.CategoryRepository;
import com.iktpreobuka.project.repositories.OfferRepository;
import com.iktpreobuka.project.repositories.UserRepository;

@RestController
@RequestMapping("/api/v1/project/bills")
public class BillController {
	
	
	@PersistenceContext
	private EntityManager em;

	
	@Autowired
	private BillRepository billRepository;

	
	@Autowired
	private UserRepository userRepository;

	
	@Autowired
	private OfferRepository offerRepository;

	
	@Autowired
	private CategoryRepository categoryRepository;
	
	
	private static final String[] ERRORS = { 
			"Offer not found.", 
			"User not found." };
	
	
	// =-=-=-= POST =-=-=-=
	
	
	// T6 1.4, 1.5
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createBill(@Valid @RequestBody BillRegisterDTO billDTO, BindingResult result) {
		
		if (result.hasErrors())
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		
		BillEntity newBill = new BillEntity();
		
		newBill.setBillCreated(billDTO.getBillCreated());
		newBill.setPaymentCanceled(false);
		newBill.setPaymentMade(false);
		
		billRepository.save(newBill);
		
		return new ResponseEntity<>(newBill, HttpStatus.CREATED);
	}
	
	
	// T6 1.4
	@RequestMapping(method = RequestMethod.POST, value = "/{offerId}/buyer/{buyerId}")
	public ResponseEntity<?> createBillWithOfferAndBuyer(
			@PathVariable Integer offerId, @PathVariable Integer buyerId, @Valid @RequestBody BillRegisterDTO billDTO, 
			BindingResult result) {
		
		if (result.hasErrors())
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		
		OfferEntity offer = offerRepository.findById(offerId).orElse(null);
		UserEntity user = userRepository.findById(buyerId).orElse(null);
		
		// ??? mozda se moze automatizovati ???
		if (offer == null || user == null) {
			
			StringBuilder sb = new StringBuilder();
			
			if (offer == null) {
				sb.append(ERRORS[0]);
				sb.append(" ");
			}
			
			if (user == null) {
				sb.append(ERRORS[1]);
				sb.append(" ");
			}
			
			return new ResponseEntity<>(sb.toString() + " " + createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		
		offer.setNumAvailable(offer.getNumAvailable() - 1);
		offer.setNumBought(offer.getNumBought() + 1);
		
		// ??? da li je neophodno ???
		offerRepository.save(offer);
		
		BillEntity newBill = new BillEntity();
		
		newBill.setBillCreated(billDTO.getBillCreated());
		newBill.setPaymentCanceled(false);
		newBill.setPaymentMade(false);
		
		newBill.setUser(user);
		newBill.setOffer(offer);
		
		billRepository.save(newBill);
		
		return new ResponseEntity<>(newBill, HttpStatus.CREATED);
	}
	
	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}
	
	
	// =-=-=-= GET =-=-=-=-=
	
	
	// T3 3.3
	@RequestMapping(method = RequestMethod.GET)
	public List<BillEntity> getAllBills() {
		return (List<BillEntity>) billRepository.findAll();
	}
	
	
	// T3 3.7
	@RequestMapping(method = RequestMethod.GET, value = "/findByBuyer/{buyerId}")
	public List<BillEntity> getAllBillsByBuyer(@PathVariable Integer buyerId) {
		
		UserEntity buyer = userRepository.findById(buyerId).orElse(null);
		if (buyer == null) return null;
		
		List<BillEntity> bills = billRepository.findByUser(buyer);
		
		return bills;
	}
	
	
	// T3 3.8
	@RequestMapping(method = RequestMethod.GET, value = "/findByCategory/{categoryId}")
	public List<BillEntity> getAllBillsByCategory(@PathVariable Integer categoryId) {
		
		CategoryEntity category = categoryRepository.findById(categoryId).orElse(null);
		if (category == null) return null;
		
		String hql = "select b from BillEntity b where b.offer.category = :catX";
		
		Query query = em.createQuery(hql);
		query.setParameter("catX", category);
		
		List<BillEntity> bills = query.getResultList();
		
		return bills;
	}
	
	
	// T3 3.9
	@RequestMapping(method = RequestMethod.GET, value = "/findByDate/{startDate}/and/{endDate}")
	public List<BillEntity> getAllBillsBetweenDates(@PathVariable String startDate, @PathVariable String endDate) {
		
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		LocalDate start = LocalDate.parse(startDate, dateFormat);
		LocalDate end = LocalDate.parse(endDate, dateFormat);
		
		Date s = Date.valueOf(start);
		Date e = Date.valueOf(end);
		
		String hql = "select b from BillEntity b where b.billCreated between :startX and :endX";
		Query q = em.createQuery(hql);
		q.setParameter("startX", s);
		q.setParameter("endX", e);
		
		List<BillEntity> bills = q.getResultList();
		
		return bills;
	}
	
	
	// =-=-=-= PUT =-=-=-=
	
	
	// T3 3.6 (izmena u T3 5.2)
	// TODO azurirati sve PUT metode da budu u skladu sa novim POST metodima
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public BillEntity changeBill(@PathVariable Integer id, @RequestBody ObjectNode objectNode) {
		
		BillEntity bill = billRepository.findById(id).orElse(null);
		if (bill == null) return null;
		
		boolean paymentCanceled = objectNode.get("canceled").textValue().equalsIgnoreCase("yes") ? true : false;
		boolean paymentMade = objectNode.get("made").textValue().equalsIgnoreCase("yes") ? true : false;
		
		if (paymentCanceled) {
			
			bill.getOffer().setNumAvailable(bill.getOffer().getNumAvailable() + 1);
			bill.getOffer().setNumBought(bill.getOffer().getNumBought() - 1);
			
			// ??? da li je neophodno ???
			offerRepository.save(bill.getOffer());
		}
				
		bill.setPaymentCanceled(paymentCanceled);
		bill.setPaymentMade(paymentMade);
		
		billRepository.save(bill);
		
		return bill;
	}
	
	
	// =-=-=-= DELETE =-=-=-=
	
	
	// T3 3.6
	@RequestMapping(method = RequestMethod.DELETE, value ="/{id}")
	public BillEntity delete(@PathVariable Integer id) {
		
		BillEntity bill = billRepository.findById(id).orElse(null);
		if (bill == null) return null;
		
		billRepository.delete(bill);
		
		return bill;
	}
}
