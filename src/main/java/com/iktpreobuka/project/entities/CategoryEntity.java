package com.iktpreobuka.project.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import security.Views;

@Entity
public class CategoryEntity {

	
	@Id
	@JsonView(Views.Public.class)
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;
	
	@Column(nullable = false)
	@JsonView(Views.Public.class)
	@NotBlank(message = "Category name must be provided.")
	protected String name;
	
	@Column(nullable = false)
	@JsonView(Views.Public.class)
	@NotBlank(message = "Category description must be provided.")
	@Size(max=30, message = "Category description must be under {max} characters long.")
	protected String description;
	
	@JsonIgnore
	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private List<OfferEntity> offers = new ArrayList<>();

	@Version
	private Integer version;
	
	
	public CategoryEntity() { }


	public CategoryEntity(String name, String description) {
		this.name = name;
		this.description = description;
	}


	public Integer getId() 					{ return id; 			}
	public String getName() 				{ return name; 			}
	public String getDescription() 			{ return description; 	}
	public List<OfferEntity> getOffers()	{ return offers;		}


	public void setId(Integer id) 					{ this.id = id; 					}
	public void setName(String name) 				{ this.name = name; 				}
	public void setDescription(String description) 	{ this.description = description; 	}
	public void addOffer(OfferEntity offer)			{ this.offers.add(offer);			}
}
