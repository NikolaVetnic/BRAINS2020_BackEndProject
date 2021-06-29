package com.iktpreobuka.project.entities.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

// T5 3.1
public class ReportItem {

	@JsonProperty("date")
	private LocalDate date;
	
	@JsonProperty("income")
	private Double income;
	
	@JsonProperty("numberOfOffers")
	private Integer numberOfOffers;
	
	public ReportItem() {
		super();
	}

	public LocalDate getDate() 			{ return date; 				} 
	public Double getIncome() 			{ return income; 			} 
	public Integer getNumberOfOffers() 	{ return numberOfOffers; 	}

	public void setDate(LocalDate date) 					{ this.date = date; 					} 
	public void setIncome(Double income) 					{ this.income = income; 				}
	public void setNumberOfOffers(Integer numberOfOffers) 	{ this.numberOfOffers = numberOfOffers; }

	@Override
	public String toString() {
		return "ReportItem [date=" + date + ", income=" + income + ", numberOfOffers=" + numberOfOffers + "]";
	} 
	
	
	
}
