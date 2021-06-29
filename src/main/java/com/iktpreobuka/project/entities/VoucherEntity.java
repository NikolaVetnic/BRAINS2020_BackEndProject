package com.iktpreobuka.project.entities;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

import security.Views;

@Entity
public class VoucherEntity {

	
	@Id
	@JsonView(Views.Public.class)
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;
	
	@Column(nullable = false)
	@JsonView(Views.Public.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	protected LocalDate expirationDate;
	
	@Column(nullable = false)
	@JsonView(Views.Admin.class)
	protected boolean isUsed;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JsonView(Views.Private.class)
	@JoinColumn(name = "offer")
	protected OfferEntity offer;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JsonView(Views.Private.class)
	@JoinColumn(name = "user")
	protected UserEntity user;
	
	@Version
	private Integer version;
	
	
	public VoucherEntity() { }


	public Integer getId() 					{ return id; 				}
	public LocalDate getExpirationDate() 	{ return expirationDate; 	}
	public boolean isUsed() 				{ return isUsed; 			}
	public OfferEntity getOffer() 			{ return offer; 			}
	public UserEntity getUser() 			{ return user; 				}


	public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }
	public void setUsed(boolean isUsed) 					{ this.isUsed = isUsed; 				}
	public void setOffer(OfferEntity offer) 				{ this.offer = offer; 					}
	public void setUser(UserEntity user) 					{ this.user = user; 					}	
}
