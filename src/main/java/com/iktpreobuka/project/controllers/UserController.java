package com.iktpreobuka.project.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.project.entities.Role;
import com.iktpreobuka.project.entities.UserEntity;

@RestController
@RequestMapping("/project/users")
public class UserController {


	// 1.2
	private List<UserEntity> getDB() {
		
		UserEntity u1 = new UserEntity(
				1, "Vladimir", "Dimitrieski", "vladimir", "vladimir", "dimitrieski@uns.ac.rs", Role.ROLE_CUSTOMER);
		UserEntity u2 = new UserEntity(
				2, "Milan", "Celikovic", "milan", "milan", "milancel@uns.ac.rs", Role.ROLE_CUSTOMER);
		UserEntity u3 = new UserEntity(
				3, "Nebojsa", "Horvat", "nebojsa", "nebojsa", "horva.n@uns.ac.rs", Role.ROLE_CUSTOMER);
		
		return Stream.of(u1, u2, u3).collect(Collectors.toList());
	}
	
	
	// 1.3
	@RequestMapping(method = RequestMethod.GET)
	public List<UserEntity> getAll() {;
		return getDB();
	}
	
	
	// 1.4
	@RequestMapping(method = RequestMethod.GET, value ="/{id}")
	public UserEntity getById(@PathVariable String id) {
		
		for (UserEntity u : getDB())
			if (u.getId() == Integer.parseInt(id))
				return u;
		
		return null;
	}
	
	
	// 1.5
	@RequestMapping(method = RequestMethod.POST)
	public UserEntity add(@RequestBody UserEntity u) {
		
		/*
		 * {
		 *	    "id": 4,
		 *	    "firstName": "Nikola",
		 *	    "lastName": "Vetnic",
		 *		"username": "nikola",
		 *		"password": "nikola",
		 *	    "email": "nv@uns.ac.rs",
		 *		"role": "customer"
		 *	}
		 */
		
		UserEntity u0 = new UserEntity();
		u0.setId(u.getId());
		u0.setFirstName(u.getFirstName());
		u0.setLastName(u.getLastName());
		u0.setUsername(u.getUsername());
		u0.setPassword(u.getPassword());
		u0.setEmail(u.getEmail());
		u0.setUserRole(Role.ROLE_CUSTOMER);
		
		getDB().add(u0);
		
		return u0;
	}
	
	
	// 1.6
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public UserEntity changeUser(@PathVariable Integer id, @RequestBody UserEntity user) {
		
		for (UserEntity u : getDB())
			if (u.getId().equals(user.getId())) {
				
				if (user.getFirstName() != null)
					u.setFirstName(user.getFirstName());
				
				if (user.getLastName() != null)
					u.setLastName(user.getLastName());
				
				if (user.getUsername() != null)
					u.setUsername(user.getUsername());
				
				if (user.getEmail() != null)
					u.setEmail(user.getEmail());
				
				return u;
			}
		
		return null;
	}
	
	
	// 1.7
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/role/{role}")
	public UserEntity changeRole(@PathVariable Integer id, @PathVariable String role) {
		
		for (UserEntity u : getDB())
			if (u.getId().equals(id)) {
				
				u.setUserRole(Role.fromString(role));
				
				return u;
			}
		
		return null;
	}
	
	
	// 1.8
	@RequestMapping(method = RequestMethod.PUT, value = "/changePassword/{id}")
	public UserEntity changePassword(
			@PathVariable Integer id, @RequestParam String op, @RequestParam String np) {
		
		for (UserEntity u : getDB()) {
			if (u.getId().equals(id)) {
				if (u.getPassword().equals(op)) {
					
					u.setPassword(np);
					
					return u;
				}
			}
		}
		
		return null;
	}
	
	
	// 1.9
	@RequestMapping(method = RequestMethod.DELETE, value ="/{id}")
	public UserEntity delete(@PathVariable Integer id) {
		
		UserEntity u = getDB().stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
		
		if (u == null) {
			return null;
		} else {
			getDB().remove(u);
			return u;
		}
	}
	
	
	// 1.10
	@RequestMapping(method = RequestMethod.GET, value ="/by-username/{username}")
	public UserEntity getByUsername(@PathVariable String username) {
		
		for (UserEntity u : getDB())
			if (u.getUsername().equals(username))
				return u;
		
		return null;
	}
}
