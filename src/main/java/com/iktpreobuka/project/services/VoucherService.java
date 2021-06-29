package com.iktpreobuka.project.services;

import org.springframework.http.ResponseEntity;

import com.iktpreobuka.project.entities.VoucherEntity;

public interface VoucherService {

	// T4 1.2
	public VoucherEntity createVoucherWithBill(Integer billId);
}
