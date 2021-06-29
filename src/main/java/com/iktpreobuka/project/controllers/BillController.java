package com.iktpreobuka.project.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iktpreobuka.project.controllers.dto.BillRegisterDTO;
import com.iktpreobuka.project.controllers.util.RESTError;
import com.iktpreobuka.project.entities.BillEntity;
import com.iktpreobuka.project.entities.CategoryEntity;
import com.iktpreobuka.project.entities.OfferEntity;
import com.iktpreobuka.project.entities.UserEntity;
import com.iktpreobuka.project.entities.VoucherEntity;
import com.iktpreobuka.project.entities.dto.ReportDTO;
import com.iktpreobuka.project.entities.dto.ReportItem;
import com.iktpreobuka.project.repositories.BillRepository;
import com.iktpreobuka.project.repositories.CategoryRepository;
import com.iktpreobuka.project.repositories.OfferRepository;
import com.iktpreobuka.project.repositories.UserRepository;
import com.iktpreobuka.project.repositories.VoucherRepository;
import com.iktpreobuka.project.services.BillService;
import com.iktpreobuka.project.services.OfferService;
import com.iktpreobuka.project.services.VoucherService;

import security.Views;

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

	
	@Autowired
	private VoucherRepository voucherRepository;

	
	@Autowired
	private BillService billService;
	
	
	@Autowired
	private OfferService offerService;

	
	@Autowired
	private VoucherService voucherService;
	
	
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
	
	
	// T4 2.2 (izmena u T6 1.4)
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
		
		offerService.changeNumAvailableAndNumBought(offer.getId(), offer.getNumAvailable() - 1, offer.getNumBought() + 1);
		
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
	
	
	// T6 1.5, 1.6, 1.7, 1.9
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<List<BillEntity>>((List<BillEntity>) billRepository.findAll(), HttpStatus.OK);
	}
	
	
	// T6 1.5, 1.6, 1.7, 1.9
	@JsonView(Views.Public.class)
	@RequestMapping(method = RequestMethod.GET, path = "/public")
	public ResponseEntity<?> getAllPublic() {
		return new ResponseEntity<List<BillEntity>>((List<BillEntity>) billRepository.findAll(), HttpStatus.OK);
	}
	
	
	// T6 1.5, 1.6, 1.7, 1.9
	@JsonView(Views.Private.class)
	@RequestMapping(method = RequestMethod.GET, value = "/private")
	public ResponseEntity<?> getAllPrivate() {
		return new ResponseEntity<List<BillEntity>>((List<BillEntity>) billRepository.findAll(), HttpStatus.OK);
	}
	

	// T6 1.5, 1.6, 1.7, 1.9
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/admin")
	public ResponseEntity<?> getAllAdmin() {
		return new ResponseEntity<List<BillEntity>>((List<BillEntity>) billRepository.findAll(), HttpStatus.OK);
	}
	
	
	// T3 3.7 (izmena u T6 2.2)
	@RequestMapping(method = RequestMethod.GET, value = "/findByBuyer/{buyerId}")
	public ResponseEntity<?> getAllBillsByBuyer(@PathVariable Integer buyerId) {
		
		try {
			
			UserEntity buyer = userRepository.findById(buyerId).orElse(null);
			
			return buyer == null ? 
					new ResponseEntity<RESTError>			(new RESTError(1, "Buyer not found."), 	HttpStatus.NOT_FOUND) : 
					new ResponseEntity<List<BillEntity>>	(billRepository.findByUser(buyer), 		HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(2, "Internal server error. Error: " + e.getMessage()), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	// T3 3.8 (izmena u T6 2.2)
	@RequestMapping(method = RequestMethod.GET, value = "/findByCategory/{categoryId}")
	public ResponseEntity<?> getAllBillsByCategory(@PathVariable Integer categoryId) {
		
		try {
			
			CategoryEntity category = categoryRepository.findById(categoryId).orElse(null);
			
			if (category == null) {
				
				return new ResponseEntity<RESTError>(new RESTError(1, "Category not found."), HttpStatus.NOT_FOUND);
			} else {
				
				String hql = "select b from BillEntity b where b.offer.category = :catX";
				
				Query query = em.createQuery(hql);
				query.setParameter("catX", category);
				
				List<BillEntity> bills = query.getResultList();
				
				return new ResponseEntity<List<BillEntity>>(bills, HttpStatus.OK);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(2, "Internal server error. Error: " + e.getMessage()), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	// T3 3.9 (izmena u T6 2.2)
	@RequestMapping(method = RequestMethod.GET, value = "/findByDate/{startDate}/and/{endDate}")
	public ResponseEntity<?> getAllBillsBetweenDates(@PathVariable String startDate, @PathVariable String endDate) {
		
		try {
			
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			
			List<BillEntity> bills = billService.findByPeriod(
					LocalDate.parse(startDate, dateFormat), LocalDate.parse(endDate, dateFormat));
			
			return bills.isEmpty() ? 
					new ResponseEntity<RESTError>			(new RESTError(1, "No bills found."), 	HttpStatus.NOT_FOUND) : 
					new ResponseEntity<List<BillEntity>>	(bills, 								HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(2, "Internal server error. Error: " + e.getMessage()), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	// T4 3.1
	@RequestMapping(method = RequestMethod.GET, value = "nonPaid")
	public ResponseEntity<?> getNonExpired() {
		return new ResponseEntity<List<BillEntity>>((List<BillEntity>) billService.findNonPaid(), HttpStatus.OK);
	}
	
	
	// T4 3.3
	@RequestMapping(method = RequestMethod.PUT, value = "/offer/{id}")
	public ResponseEntity<?> cancelAllForOffer(@PathVariable Integer id) {
		
		try {
			
			OfferEntity offer = offerRepository.findById(id)
					.orElseThrow(() -> new Exception("Offer #" + id + " does not exist."));
			
			List<BillEntity> bills = billRepository.findByOffer(offer);
			
			if (bills.isEmpty()) {
				
				return new ResponseEntity<RESTError>(new RESTError(1, "No bills found."), HttpStatus.NOT_FOUND);
			} else {
				
				for (BillEntity b : bills)
					b.setPaymentCanceled(true);
				
				return new ResponseEntity<List<BillEntity>>(bills, HttpStatus.OK);				
			}

		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(2, "Internal server error. Error: " + e.getMessage()), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	// T5 3.4
	@RequestMapping(method = RequestMethod.GET, value = "/generateReportByDate/{startDate}/and/{endDate}")
	public ResponseEntity<?> generateReportByPeriod(@PathVariable String startDate, @PathVariable String endDate) {
		
		try {
			
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			
			List<ReportItem> items = new ArrayList<ReportItem>();
			
			List<BillEntity> bills = billService.findByPeriod(LocalDate.parse(startDate, dateFormat), LocalDate.parse(endDate, dateFormat));
			for (BillEntity bill : bills) {
				
				ReportItem reportItem = new ReportItem();
				
				reportItem.setDate(bill.getBillCreated());
				reportItem.setIncome(bill.getOffer() != null ? bill.getOffer().getRegPrice() : 0);
				reportItem.setNumberOfOffers(bill.getOffer() != null ? bill.getOffer().getNumBought() : 0);
				
				System.out.println(reportItem);
				
				items.add(reportItem);
			}
			
			return new ResponseEntity<List<ReportItem>>(items, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(2, "Internal server error. Error: " + e.getMessage()), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	// T5 3.5
	@RequestMapping(method = RequestMethod.GET, value = "/generateReportByDate/{startDate}/and/{endDate}/category/{categoryId}")
	public ResponseEntity<?> generateReportByPeriodAndCategory(
			@PathVariable String startDate, @PathVariable String endDate, @PathVariable Integer categoryId) {
		
		try {
			
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			
			List<ReportItem> items = new ArrayList<ReportItem>();

			CategoryEntity category = categoryRepository.findById(categoryId)
					.orElseThrow(() -> new Exception("Category #" + categoryId + " does not exist."));
			
			List<BillEntity> bills = billService.findByPeriod(
					LocalDate.parse(startDate, dateFormat), 
					LocalDate.parse(endDate, dateFormat)).stream()
							.filter(b -> b.getOffer() != null)
							.filter(b -> b.getOffer().getCategory().equals(category))
							.collect(Collectors.toList());
			
			for (BillEntity bill : bills) {
				
				ReportItem reportItem = new ReportItem();
				
				reportItem.setDate(bill.getBillCreated());
				reportItem.setIncome(bill.getOffer() != null ? bill.getOffer().getRegPrice() : 0);
				reportItem.setNumberOfOffers(bill.getOffer() != null ? bill.getOffer().getNumBought() : 0);
				
				System.out.println(reportItem);
				
				items.add(reportItem);
			}
			
			ReportDTO reportDTO = new ReportDTO();
			
			reportDTO.setCategoryName(category.getName());
			reportDTO.setItems(items);
			reportDTO.setSumOfIncomes(items.stream().collect(Collectors.summingDouble(ReportItem::getIncome)));
			reportDTO.setTotalNumberOfSoldOffers((int) items.stream().count());
			
			return new ResponseEntity<ReportDTO>(reportDTO, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(2, "Internal server error. Error: " + e.getMessage()), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	// =-=-=-= PUT =-=-=-=
	
	
	// T3 3.6 (izmena u T4 2.4, T3 5.2)
	// TODO azurirati sve PUT metode da budu u skladu sa novim POST metodima
	// TODO dodati ResponseEntity
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public BillEntity changeBill(@PathVariable Integer id, @RequestBody ObjectNode objectNode) {
		
		BillEntity bill = billRepository.findById(id).orElse(null);
		if (bill == null) return null;
		
		boolean paymentCanceled = objectNode.get("canceled").textValue().equalsIgnoreCase("yes") ? true : false;
		boolean paymentMade = objectNode.get("made").textValue().equalsIgnoreCase("yes") ? true : false;
		
		if (paymentCanceled && !bill.getPaymentCanceled() && bill.getOffer() != null) {
			
			offerService.changeNumAvailableAndNumBought(
					bill.getOffer().getId(), bill.getOffer().getNumAvailable() + 1, bill.getOffer().getNumBought() - 1);
			
			// ??? da li je neophodno ???
			offerRepository.save(bill.getOffer());
		}
		
		if (paymentMade && !bill.getPaymentMade() && bill.getOffer() != null) {
			
			offerService.changeNumAvailableAndNumBought(
					bill.getOffer().getId(), bill.getOffer().getNumAvailable() - 1, bill.getOffer().getNumBought() + 1);
			
			VoucherEntity voucher = voucherService.createVoucherWithBill(id);
			voucherRepository.save(voucher);
			
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
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		
		try {
		
			BillEntity bill = billRepository.findById(id).orElse(null);
			
			billRepository.delete(bill);
			
			return bill == null ? 
					new ResponseEntity<RESTError>	(new RESTError(1, "Bill not found."), 	HttpStatus.NOT_FOUND) : 
					new ResponseEntity<BillEntity>	(bill, 									HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(2, "Internal server error. Error: " + e.getMessage()), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
