package com.iktpreobuka.project.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.project.entities.BillEntity;
import com.iktpreobuka.project.entities.EmailObject;
import com.iktpreobuka.project.entities.OfferEntity;
import com.iktpreobuka.project.entities.UserEntity;
import com.iktpreobuka.project.entities.VoucherEntity;
import com.iktpreobuka.project.entities.enums.Role;
import com.iktpreobuka.project.repositories.BillRepository;
import com.iktpreobuka.project.repositories.OfferRepository;
import com.iktpreobuka.project.repositories.UserRepository;
import com.iktpreobuka.project.repositories.VoucherRepository;

@Service
public class VoucherServiceImpl implements VoucherService {

	
	@PersistenceContext
	private EntityManager em;

	
	@Autowired
	private BillRepository billRepository;

	
	@Autowired
	private VoucherRepository voucherRepository;

	
	@Autowired
	private UserRepository userRepository;

	
	@Autowired
	private OfferRepository offerRepository;

	
	@Autowired
	private EmailService emailService;

	
	private static final String[] ERRORS = { 
			"Offer not found.", 
			"User not found.", 
			"User role must be 'customer'." };
	

	// T4 4.1, 4.2
	@Override
	public VoucherEntity createVoucherWithBill(Integer billId) {
		
		try {
			
			BillEntity bill = billRepository.findById(billId).orElseThrow(
					() -> new Exception("Bill #" + billId + " does not exist."));
			
			// orElse - baciti izuzetak, obraditi (videti projekat validacija) i odmah izaci
			OfferEntity offer = bill.getOffer();
			UserEntity user = bill.getUser();
			
			// ??? mozda se moze automatizovati ???
			if (offer == null || user == null || user.getUserRole() != Role.ROLE_CUSTOMER)
				return null;
			
			VoucherEntity newVoucher = new VoucherEntity();
			
			newVoucher.setExpirationDate(LocalDate.now().plusDays(20));
			newVoucher.setUsed(false);
			newVoucher.setOffer(offer);
			newVoucher.setUser(user);
			
			voucherRepository.save(newVoucher);

			user.addVouchers(newVoucher);
			userRepository.save(user);
			
			offer.addVouchers(newVoucher);
			offerRepository.save(offer);
			
			String path = "res//table.csv";
			
			try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path)))) {
				
				pw.println("Buyer,Offer,Price,Expires date");
				pw.println(newVoucher.getUser() + "," + newVoucher.getOffer().getName() + "," + newVoucher.getOffer().getRegPrice() + "," + newVoucher.getExpirationDate());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			EmailObject emailObject = new EmailObject();
			
			emailObject.setTo("nikola.pacek.vetnic@gmail.com");
			emailObject.setSubject("New voucher!");
			emailObject.setText("TEST");
			
			emailService.sendMessageWithAttachment(emailObject, path);
			
			return newVoucher;
		
		} catch (Exception e) {
			System.out.println("Internal server error. Error: " + e.getMessage());
		}
		
		return null;
	}
}
