package com.iktpreobuka.project.controllers.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BillRegisterDTO {
	
	@Past
	@JsonProperty("billCreated")
	@NotNull(message = "Date created must be provided.")
	private LocalDate billCreated;
	
	public BillRegisterDTO() {
		super();
	}
	
	public BillRegisterDTO(LocalDate billCreated) {
		super();
		this.billCreated = billCreated;
	}

	public LocalDate getBillCreated() 					{ return billCreated; 				}
	public void setBillCreated(LocalDate billCreated) 	{ this.billCreated = billCreated; 	}
}
