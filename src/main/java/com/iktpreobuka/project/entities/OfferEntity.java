package com.iktpreobuka.project.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.project.entities.enums.OfferStatus;

import security.Views;

@Entity
public class OfferEntity {

	
	@Id
	@JsonView(Views.Public.class)
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;
	
	@Column(nullable = false)
	@JsonView(Views.Public.class)
	@NotBlank(message = "Name must be provided.")
	protected String name;
	
	@Column(nullable = false)
	@JsonView(Views.Public.class)
	@NotBlank(message = "Description must be provided.")
	@Size(min=5, max=20, message = "Description name must be between {min} and {max} characters long.")
	protected String description;
	
	@Column(nullable = false)
	@JsonView(Views.Public.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	protected LocalDate created;
	
	@Column(nullable = false)
	@JsonView(Views.Public.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	protected LocalDate expires;
	
	@Column(nullable = false)
	@JsonView(Views.Public.class)
	@Min(value=1, message = "Regular price must be greater than 1.")
	protected Double regPrice;
	
	@Column(nullable = false)
	@JsonView(Views.Public.class)
	@Min(value=1, message = "Action price must be greater than 1.")
	protected Double actPrice;
	
	@Column(nullable = false)
	@JsonView(Views.Public.class)
	@NotBlank(message = "Image path must be provided.")
	protected String imgPath;
	
	@Column(nullable = false)
	@JsonView(Views.Public.class)
	@Min(value=1, message = "Number available must be greater than 0.")
	protected Integer numAvailable;
	
	@Column(nullable = false)
	@JsonView(Views.Public.class)
	@Min(value=0, message = "Number bought must be equal to or greater than 0.")
	protected Integer numBought;

	@Column(nullable = false)
	@JsonView(Views.Public.class)
	@Enumerated(EnumType.STRING)
	protected OfferStatus status;
	
	// @JsonIgnore dodajemo samo kod @ManyToOne sa strane lista!
	// insertable=false, updatable=false - moze praviti problem sa @JsonIgnore
	// fetch = FetchType.LAZY - pravilo problem u @ManyToOne zagradi
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JsonView(Views.Public.class)
	@JoinColumn(name = "category")
	private CategoryEntity category;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JsonView(Views.Public.class)
	@JoinColumn(name = "user")
	private UserEntity user;
	
	@JsonIgnore
	@OneToMany(mappedBy = "offer", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH } )
	protected List<BillEntity> bills = new ArrayList<BillEntity>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "offer", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH } )
	protected List<VoucherEntity> vouchers = new ArrayList<VoucherEntity>();
	
	@Version
	private Integer version;
	
	
	public OfferEntity() { }


//	public OfferEntity(String name, String desc, LocalDate created, LocalDate expires, double regPrice, double actPrice, String imgPath,
//			int numAvailable, int numBought, OfferStatus status) {
//		this.name = name;
//		this.description = desc;
//		this.created = created;
//		this.expires = expires;
//		this.regPrice = regPrice;
//		this.actPrice = actPrice;
//		this.imgPath = imgPath;
//		this.numAvailable = numAvailable;
//		this.numBought = numBought;
//		this.status = status;
//	}


	public Integer getId() 						{ return id; 			}
	public String getName() 					{ return name; 			}
	public String getDescription() 				{ return description; 	}
	public LocalDate getCreated() 				{ return created; 		}
	public LocalDate getExpires() 				{ return expires; 		}
	public Double getRegPrice() 				{ return regPrice; 		}
	public Double getActPrice() 				{ return actPrice; 		}
	public String getImgPath() 					{ return imgPath; 		}
	public Integer getNumAvailable() 			{ return numAvailable; 	}
	public Integer getNumBought() 				{ return numBought; 	} 
	public OfferStatus getStatus() 				{ return status; 		}
	public CategoryEntity getCategory() 		{ return category; 		}
	public UserEntity getUser() 				{ return user; 			}
	public List<BillEntity> getBills() 			{ return bills; 		}
	public List<VoucherEntity> getVouchers() 	{ return vouchers; 		}

	
	public void setName(String name) 						{ this.name = name; 				}
	public void setDesc(String desc) 						{ this.description = desc; 			}
	public void setCreated(LocalDate created) 				{ this.created = created; 			}
	public void setExpires(LocalDate expires) 				{ this.expires = expires; 			}
	public void setRegPrice(Double regPrice) 				{ this.regPrice = regPrice; 		}
	public void setActPrice(Double actPrice) 				{ this.actPrice = actPrice; 		}
	public void setImgPath(String imgPath) 					{ this.imgPath = imgPath; 			}
	public void setNumAvailable(Integer numAvailable) 		{ this.numAvailable = numAvailable; }
	public void setNumBought(Integer numBought)				{ this.numBought = numBought; 		}
	public void setStatus(OfferStatus status) 				{ this.status = status; 			}
	public void setCategory(CategoryEntity category) 		{ this.category = category; 		}
	public void setUser(UserEntity user) 					{ this.user = user; 				}
	public void setDescription(String description) 			{ this.description = description; 	}
//	public void setBills(List<BillEntity> bills) 			{ this.bills = bills; 				}
//	public void setVouchers(List<VoucherEntity> vouchers) 	{ this.vouchers = vouchers; 		}
	
	public void addBills(BillEntity bill) { this.bills.add(bill); }
	public void addVouchers(VoucherEntity voucher) { this.vouchers.add(voucher); }
}
