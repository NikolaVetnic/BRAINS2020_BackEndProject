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

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iktpreobuka.project.controllers.dto.VoucherRegisterDTO;
import com.iktpreobuka.project.controllers.util.RESTError;
import com.iktpreobuka.project.entities.BillEntity;
import com.iktpreobuka.project.entities.OfferEntity;
import com.iktpreobuka.project.entities.UserEntity;
import com.iktpreobuka.project.entities.VoucherEntity;
import com.iktpreobuka.project.entities.enums.Role;
import com.iktpreobuka.project.repositories.OfferRepository;
import com.iktpreobuka.project.repositories.UserRepository;
import com.iktpreobuka.project.repositories.VoucherRepository;

import security.Views;

@RestController
@RequestMapping("/api/v1/project/vouchers")
public class VoucherController {
	
	
	@PersistenceContext
	private EntityManager em;

	
	@Autowired
	private VoucherRepository voucherRepository;

	
	@Autowired
	private UserRepository userRepository;

	
	@Autowired
	private OfferRepository offerRepository;
	
	
	private static final String[] ERRORS = { 
			"Offer not found.", 
			"User not found.", 
			"User role must be 'customer'." };
	
	
	// =-=-=-= POST =-=-=-=
	
	
	// T6 1.6, 1.7
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createVoucher(@Valid @RequestBody VoucherRegisterDTO voucherDTO, BindingResult result) {
		
		if (result.hasErrors())
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		
		VoucherEntity newVoucher = new VoucherEntity();
		
		newVoucher.setExpirationDate(voucherDTO.getExpirationDate());
		newVoucher.setUsed(false);
		
		voucherRepository.save(newVoucher);
		
		return new ResponseEntity<>(newVoucher, HttpStatus.CREATED);
	}
		
		
	// T6 1.6, 1.7
	@RequestMapping(method = RequestMethod.POST, value = "/{offerId}/buyer/{buyerId}")
	public ResponseEntity<?> createVoucherWithOfferAndBuyer(
			@PathVariable Integer offerId, @PathVariable Integer buyerId, 
			@Valid @RequestBody VoucherRegisterDTO voucherDTO, BindingResult result) {
		
		if (result.hasErrors())
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		
		// orElse - baciti izuzetak, obraditi (videti projekat validacija) i odmah izaci
		OfferEntity offer = offerRepository.findById(offerId).orElse(null);
		UserEntity user = userRepository.findById(buyerId).orElse(null);
		
		// ??? mozda se moze automatizovati ???
		if (offer == null || user == null || user.getUserRole() != Role.ROLE_CUSTOMER) {
			
			StringBuilder sb = new StringBuilder();
			
			if (offer == null) {
				sb.append(ERRORS[0]);
				sb.append(" ");
			}
			
			if (user == null) {
				sb.append(ERRORS[1]);
				sb.append(" ");
			}
			
			if (user.getUserRole() != Role.ROLE_CUSTOMER) {
				sb.append(ERRORS[2]);
				sb.append(" ");
			}
			
			return new ResponseEntity<>(sb.toString() + " " + createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		
		VoucherEntity newVoucher = new VoucherEntity();
		
		newVoucher.setExpirationDate(voucherDTO.getExpirationDate());
		newVoucher.setUsed(false);
		newVoucher.setOffer(offer);
		newVoucher.setUser(user);
		
		voucherRepository.save(newVoucher);

		user.addVouchers(newVoucher);
		userRepository.save(user);
		
		offer.addVouchers(newVoucher);
		offerRepository.save(offer);
		
		return new ResponseEntity<>(newVoucher, HttpStatus.CREATED);
	}
	
	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}
	
	
	// =-=-=-= GET =-=-=-=-=
	
	
	// T6 1.5, 1.6, 1.7, 1.10
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<List<VoucherEntity>>((List<VoucherEntity>) voucherRepository.findAll(), HttpStatus.OK);
	}
	
	
	// T6 1.5, 1.6, 1.7, 1.10
	@JsonView(Views.Public.class)
	@RequestMapping(method = RequestMethod.GET, path = "/public")
	public ResponseEntity<?> getAllPublic() {
		return new ResponseEntity<List<VoucherEntity>>((List<VoucherEntity>) voucherRepository.findAll(), HttpStatus.OK);
	}
	
	
	// T6 1.5, 1.6, 1.7, 1.10
	@JsonView(Views.Private.class)
	@RequestMapping(method = RequestMethod.GET, value = "/private")
	public ResponseEntity<?> getAllPrivate() {
		return new ResponseEntity<List<VoucherEntity>>((List<VoucherEntity>) voucherRepository.findAll(), HttpStatus.OK);
	}
	

	// T6 1.5, 1.6, 1.7, 1.10
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/admin")
	public ResponseEntity<?> getAllAdmin() {
		return new ResponseEntity<List<VoucherEntity>>((List<VoucherEntity>) voucherRepository.findAll(), HttpStatus.OK);
	}
	
	
	// T3 4.7 (izmena u T6 2.2)
	@RequestMapping(method = RequestMethod.GET, value = "/findByBuyer/{buyerId}")
	public ResponseEntity<?> getAllVouchersByBuyer(@PathVariable Integer buyerId) {
		
		try {
			
			UserEntity buyer = userRepository.findById(buyerId).orElse(null);
			
			return buyer == null ? 
					new ResponseEntity<RESTError>			(new RESTError(1, "User not found."), 	HttpStatus.NOT_FOUND) : 
					new ResponseEntity<List<VoucherEntity>>	(voucherRepository.findByUser(buyer), 	HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(2, "Internal server error. Error: " + e.getMessage()), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	// T3 4.8 (izmena u T6 2.2)
	@RequestMapping(method = RequestMethod.GET, value = "/findByOffer/{offerId}")
	public ResponseEntity<?> getAllVouchersByOffer(@PathVariable Integer offerId) {
		
		try {
			
			OfferEntity offer = offerRepository.findById(offerId).orElse(null);
			
			return offer == null ? 
					new ResponseEntity<RESTError>			(new RESTError(1, "Pffer not found."), 	HttpStatus.NOT_FOUND) : 
					new ResponseEntity<List<VoucherEntity>>	(voucherRepository.findByOffer(offer), 	HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(2, "Internal server error. Error: " + e.getMessage()), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	// T3 4.9 (izmena u T6 2.2)
	@RequestMapping(method = RequestMethod.GET, value = "/findNonExpiredVoucher")
	public ResponseEntity<?> getNonExpiredVouchers() {
		
		try {
			
			String hql = "select v from VoucherEntity v where v.expirationDate > :dateX";
			Query q = em.createQuery(hql);
			q.setParameter("dateX", LocalDate.now());
			
			List<VoucherEntity> vouchers = q.getResultList();
			
			return vouchers.isEmpty() ? 
					new ResponseEntity<RESTError>(
							new RESTError(1, "No vouchers match criteria."), 
							HttpStatus.NOT_FOUND) : 
					new ResponseEntity<List<VoucherEntity>>	(
							vouchers, 
							HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(2, "Internal server error. Error: " + e.getMessage()), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	// =-=-=-= PUT =-=-=-=
	
	
	// T3 4.6
	// TODO azurirati sve PUT metode da budu u skladu sa novim POST metodima
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/{offerId}/buyer/{buyerId}")
	public VoucherEntity changeVoucher(
			@PathVariable Integer id, @PathVariable Integer offerId, 
			@PathVariable Integer buyerId, @RequestBody ObjectNode objectNode) {
		
		VoucherEntity voucher = voucherRepository.findById(id).orElse(null);
		OfferEntity offer = offerRepository.findById(offerId).orElse(null);
		UserEntity buyer = userRepository.findById(buyerId).orElse(null);
		
		if (voucher == null || offer == null || buyer == null || buyer.getUserRole() != Role.ROLE_CUSTOMER) return null;
		
		voucher.setExpirationDate(
				LocalDate.parse(objectNode.get("expDate").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		voucher.setUser(buyer);
		voucher.setOffer(offer);
		
		voucherRepository.save(voucher);
		
		return voucher;
	}
	
	
	// =-=-=-= DELETE =-=-=-=
	
	
	// T3 4.6
	@RequestMapping(method = RequestMethod.DELETE, value ="/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		
		try {
			
			VoucherEntity voucher = voucherRepository.findById(id).orElse(null);
			
			voucherRepository.delete(voucher);
			
			return voucher == null ? 
					new ResponseEntity<RESTError>		(new RESTError(1, "Voucher not found."), 	HttpStatus.NOT_FOUND) : 
					new ResponseEntity<VoucherEntity>	(voucher, 									HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(2, "Internal server error. Error: " + e.getMessage()), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
