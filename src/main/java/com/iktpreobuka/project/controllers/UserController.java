package com.iktpreobuka.project.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iktpreobuka.project.entities.UserEntity;
import com.iktpreobuka.project.enums.Role;
import com.iktpreobuka.project.repositories.UserRepository;
import com.iktpreobuka.project.utils.UserCustomValidator;

@RestController
@RequestMapping("/api/v1/project/users")
public class UserController {
	
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	private UserCustomValidator userCustomValidator;
	
	
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.addValidators(userCustomValidator);
	}
	
	
	// =-=-=-= POST =-=-=-=
	
	
	// T6 1.1
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@Valid @RequestBody UserEntity user, BindingResult result) {
		
		if (result.hasErrors())
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		
		userRepository.save(user);
		
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
	
	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}
	
	
	// =-=-=-= GET =-=-=-=
	
	
	// T2 1.3
	@RequestMapping(method = RequestMethod.GET)
	public List<UserEntity> getAllUsers() {
		return (List<UserEntity>) userRepository.findAll();
	}
	
	
	// T2 1.4
	@RequestMapping(method = RequestMethod.GET, value ="/{id}")
	public UserEntity getById(@PathVariable Integer id) {
		return userRepository.findById(id).orElse(null);
	}
	
	
	// T2 1.10
	@RequestMapping(method = RequestMethod.GET, value ="/by-username/{username}")
	public UserEntity getByUsername(@PathVariable String username) {		
		return userRepository.findByUsername(username);
	}


	// =-=-=-= PUT =-=-=-=
	
	
	// T2 1.6
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public UserEntity changeUser(@PathVariable Integer id, @RequestBody ObjectNode objectNode) {
		
		UserEntity user = userRepository.findById(id).orElse(null);
		if (user == null) return null;
		
		String firstName 	= objectNode.get("firstName").asText(); 
		String lastName 	= objectNode.get("lastName").asText();
		String username 	= objectNode.get("username").asText(); 
		String password 	= objectNode.get("password").asText(); 
		String email 		= objectNode.get("email").asText();
		Role role 			= Role.fromString(objectNode.get("userRole").asText());
		
		if (firstName != null) 	user.setFirstName(firstName);
		if (lastName != null) 	user.setLastName(lastName);
		if (username != null) 	user.setUsername(username);
		if (password != null) 	user.setPassword(password);
		if (email != null) 		user.setEmail(email);
		if (role != null) 		user.setUserRole(role);
		
		userRepository.save(user);
		
		return user;
	}
	
	
	// T2 1.7
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/role")
	public UserEntity changeRole(@PathVariable Integer id, @RequestParam String role) {
		
		UserEntity user = userRepository.findById(id).orElse(null);
		if (user == null) return null;
		
		user.setUserRole(Role.fromString(role));
		userRepository.save(user);
		
		return user;
	}
	

	// T2 1.8
	@RequestMapping(method = RequestMethod.PUT, value = "/changePassword/{id}")
	public UserEntity changePassword(
			@PathVariable Integer id, @RequestParam String op, @RequestParam String np) {
		
		UserEntity user = userRepository.findById(id).orElse(null);
		
		if (user == null) return null;
		
		if (user.getPassword().equals(op))
			user.setPassword(np);

		userRepository.save(user);
		
		return user;
	}
	
	
	// =-=-=-= DELETE =-=-=-=
	
	
	// T2 1.9
	@RequestMapping(method = RequestMethod.DELETE, value ="/{id}")
	public UserEntity delete(@PathVariable Integer id) {
		
		UserEntity user = userRepository.findById(id).orElse(null);
		if (user == null) return null;
		
		userRepository.delete(user);
		
		return user;
	}
}
