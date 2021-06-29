package com.iktpreobuka.project.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iktpreobuka.project.controllers.util.RESTError;
import com.iktpreobuka.project.entities.BillEntity;
import com.iktpreobuka.project.entities.CategoryEntity;
import com.iktpreobuka.project.entities.UserEntity;
import com.iktpreobuka.project.repositories.CategoryRepository;

import security.Views;

@RestController
@RequestMapping("/api/v1/project/categories")
public class CategoryController {
	
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	
	// =-=-=-= POST =-=-=-=
	
	
	// T6 1.2
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryEntity category, BindingResult result) {
		
		if (result.hasErrors())
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		
		categoryRepository.save(category);
		
		return new ResponseEntity<>(category, HttpStatus.CREATED);
	}
	
	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}
	

	// =-=-=-= GET =-=-=-=
	
	
	// T6 1.5, 1.6, 1.7, 1.8
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<List<CategoryEntity>>((List<CategoryEntity>) categoryRepository.findAll(), HttpStatus.OK);
	}
	
	
	// T6 1.5, 1.6, 1.7, 1.8
	@JsonView(Views.Public.class)
	@RequestMapping(method = RequestMethod.GET, path = "/public")
	public ResponseEntity<?> getAllPublic() {
		return new ResponseEntity<List<CategoryEntity>>((List<CategoryEntity>) categoryRepository.findAll(), HttpStatus.OK);
	}
	
	
	// T2 2.7 (izmena u T6 2.2)
	@RequestMapping(method = RequestMethod.GET, value ="/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		
		try {
			
			CategoryEntity category = categoryRepository.findById(id).orElse(null);
			
			return category == null ? 
					new ResponseEntity<RESTError>		(new RESTError(1, "Category not found."), 	HttpStatus.NOT_FOUND) : 
					new ResponseEntity<CategoryEntity>	(category, 									HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(2, "Internal server error. Error: " + e.getMessage()), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	// =-=-=-= PUT =-=-=-=
	
	
	// T2 2.5
	// TODO azurirati sve PUT metode da budu u skladu sa novim POST metodima
	// TODO dodati ResponseEntity
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public CategoryEntity changeCategory(@PathVariable Integer id, @RequestBody ObjectNode objectNode) {
		
		CategoryEntity category = categoryRepository.findById(id).orElse(null);
		if (category == null) return null;
		
		String name 		= objectNode.get("name").asText();
		String description 	= objectNode.get("description").asText();
		
		if (name != null) 			category.setName(name);
		if (description != null) 	category.setDescription(description);
		
		categoryRepository.save(category);
		
		return category;
	}
	
	
	// =-=-=-= DELETE =-=-=-=
	
	
	// T2 2.6 (izmena u T6 2.2)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		
		try {
			
			CategoryEntity category = categoryRepository.findById(id).orElse(null);
			
			categoryRepository.delete(category);

			return category == null ? 
					new ResponseEntity<RESTError>		(new RESTError(1, "Category not found."), 	HttpStatus.NOT_FOUND) : 
					new ResponseEntity<CategoryEntity>	(category, 									HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(2, "Internal server error. Error: " + e.getMessage()), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
