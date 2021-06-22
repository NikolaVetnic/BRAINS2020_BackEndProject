package com.iktpreobuka.project.controllers.dto;

import java.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VoucherRegisterDTO {

	@Future(message = "Expiration date must be a future date.")
	@JsonProperty("expirationDate")
	@NotNull(message = "Expiration date must be provided.")
	private LocalDate expirationDate;
	
	public VoucherRegisterDTO() {
		super();
	}
	
	public VoucherRegisterDTO(LocalDate expirationDate) {
		super();
		this.expirationDate = expirationDate;
	}

	public LocalDate getExpirationDate() 					{ return expirationDate; 				}
	public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }
}
