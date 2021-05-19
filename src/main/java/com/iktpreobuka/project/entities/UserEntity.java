package com.iktpreobuka.project.entities;

public class UserEntity {

	
	protected Integer id;
	protected String firstName;
	protected String lastName;
	protected String username;
	protected String password;
	protected String email;
	protected Role userRole;
	
	
	public UserEntity() { }
	
	
	public UserEntity(int id, String firstName, String lastName, String username, String password, String email, Role userRole) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.userRole = userRole;
	}


	public Integer getId() 			{ return id; 		}
	public String getFirstName() 	{ return firstName; }
	public String getLastName() 	{ return lastName; 	}
	public String getUsername() 	{ return username; 	}
	public String getPassword() 	{ return password; 	}
	public String getEmail()		{ return email;		}
	public Role getUserRole() 		{ return userRole; 	}
	
	
	public void setId(Integer id) 				{ this.id = id; 				}
	public void setFirstName(String firstName) 	{ this.firstName = firstName; 	}
	public void setLastName(String lastName) 	{ this.lastName = lastName; 	}
	public void setUsername(String username) 	{ this.username = username; 	}
	public void setPassword(String password) 	{ this.password = password; 	}
	public void setEmail(String email)			{ this.email = email;			}
	public void setUserRole(Role userRole) 		{ this.userRole = userRole; 	}
	
	
	@Override
	public String toString() {
		return String.format("[%05d] %s %s", id, firstName, lastName);
	}
}
