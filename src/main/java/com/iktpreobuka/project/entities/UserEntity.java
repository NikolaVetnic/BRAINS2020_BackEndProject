package com.iktpreobuka.project.entities;

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
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iktpreobuka.project.enums.Role;

@Entity
public class UserEntity {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;

	@Column(nullable = false)
	@NotBlank(message = "First name must be provided.")
	@Size(min=2, max=30, message = "First name must be between {min} and {max} characters long.")
	protected String firstName;
	
	@Column(nullable = false)
	@NotBlank(message = "Last name must be provided.")
	@Size(min=2, max=30, message = "First name must be between {min} and {max} characters long.")
	protected String lastName;
	
	@Column(nullable = false)
	@NotBlank(message = "Username must be provided.")
	@Size(min=5, max=20, message = "Username must be between {min} and {max} characters long.")
	protected String username;
	
	@Column(nullable = false)
	@NotBlank(message = "Password must be provided.")
	@Size(min=5, message = "Password must be between {min} and {max} characters long.")
	@Pattern(regexp = "([0-9].*[a-zA-Z])|([a-zA-Z].*[0-9])", message = "Password must contain letters and digits.")
	protected String password;
	
	@Transient
	@Column(nullable = false)
	@NotBlank(message = "Confirm password must be provided.")
	@Size(min=5, message = "Confirm password must be between {min} and {max} characters long.")
	@Pattern(regexp = "([0-9].*[a-zA-Z])|([a-zA-Z].*[0-9])", message = "Confirm password must contain letters and digits.")
	protected String confirmPassword;
	
	@Column(nullable = false, unique = true)
	@NotNull(message = "Email must be provided.")
	@Email(message = "Email is not valid.")
	protected String email;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	protected Role userRole;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	protected List<OfferEntity> offers = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	protected List<BillEntity> bills = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	protected List<VoucherEntity> vouchers = new ArrayList<>();

	@Version
	private Integer version;
	
	
	public UserEntity() { }
	
	
//	public UserEntity(String firstName, String lastName, String username, String password, String email, Role userRole) {
//		super();
//		this.firstName = firstName;
//		this.lastName = lastName;
//		this.username = username;
//		this.password = password;
//		this.email = email;
//		this.userRole = userRole;
//	}


	public Integer getId() 						{ return id; 				}
	public String getFirstName() 				{ return firstName; 		}
	public String getLastName() 				{ return lastName; 			}
	public String getUsername() 				{ return username; 			}
	public String getPassword() 				{ return password; 			}
	public String getConfirmPassword() 			{ return confirmPassword; 	}
	public String getEmail()					{ return email;				}
	public Role getUserRole() 					{ return userRole; 			}
	public List<OfferEntity> getOffers()		{ return offers;			}
	public List<BillEntity> getBills() 			{ return bills; 			}
	public List<VoucherEntity> getVouchers() 	{ return vouchers; 			}
	
	
	public void setId(Integer id) 							{ this.id = id; 					}
	public void setFirstName(String firstName) 				{ this.firstName = firstName; 		}
	public void setLastName(String lastName) 				{ this.lastName = lastName; 		}
	public void setUsername(String username) 				{ this.username = username; 		}
	public void setPassword(String password) 				{ this.password = password; 		}
	public void setConfirmPassword(String confirmPassword) 	{ this.confirmPassword = confirmPassword; 	}
	public void setEmail(String email)						{ this.email = email;				}
	public void setUserRole(Role userRole) 					{ this.userRole = userRole; 		}
	public void setOffers(List<OfferEntity> offers) 		{ this.offers = offers; 			}
	public void setBills(List<BillEntity> bills) 			{ this.bills = bills; 				}
	public void setVouchers(List<VoucherEntity> vouchers) 	{ this.vouchers = vouchers; 		}
	
	public void addBills(BillEntity bill) { this.bills.add(bill); }
	public void addVouchers(VoucherEntity voucher) { this.vouchers.add(voucher); }


	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", username=" + username
				+ ", password=" + password + ", confirmPassword=" + confirmPassword + ", email=" + email + ", userRole="
				+ userRole + ", offers=" + offers + ", bills=" + bills + ", vouchers=" + vouchers + ", version="
				+ version + "]";
	}
	
	
	

//	@Override
//	public String toString() {
//		return String.format("[%05d] %s %s", id, firstName, lastName);
//	}
}
