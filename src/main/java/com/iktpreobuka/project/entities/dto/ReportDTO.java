package com.iktpreobuka.project.entities.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

// T5 3.2
public class ReportDTO {

	@JsonProperty("categoryName")
	private String categoryName;
	
	@JsonProperty("items")
	private List<ReportItem> items;
	
	@JsonProperty("sumOfIncomes")
	private Double sumOfIncomes;
	
	@JsonProperty("totalNumberOfSoldOffers")
	private Integer totalNumberOfSoldOffers;
	
	public ReportDTO() {
		super();
	}

	public String getCategoryName() {
		return categoryName;
	}

	public List<ReportItem> getItems() {
		return items;
	}

	public Double getSumOfIncomes() {
		return sumOfIncomes;
	}

	public Integer getTotalNumberOfSoldOffers() {
		return totalNumberOfSoldOffers;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setItems(List<ReportItem> items) {
		this.items = items;
	}

	public void setSumOfIncomes(Double sumOfIncomes) {
		this.sumOfIncomes = sumOfIncomes;
	}

	public void setTotalNumberOfSoldOffers(Integer totalNumberOfSoldOffers) {
		this.totalNumberOfSoldOffers = totalNumberOfSoldOffers;
	} 
}
