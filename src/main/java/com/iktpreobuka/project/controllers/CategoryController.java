package com.iktpreobuka.project.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.project.entities.CategoryEntity;

@RestController
@RequestMapping("/project/categories")
public class CategoryController {


	// 2.2
	private List<CategoryEntity> getDB() {
		
		CategoryEntity c1 = new CategoryEntity(1, "music", "description 1");
		CategoryEntity c2 = new CategoryEntity(2, "food", "description 2");
		CategoryEntity c3 = new CategoryEntity(3, "entertainment", "description 3");
		
		return Stream.of(c1, c2, c3).collect(Collectors.toList());
	}
	
	
	// 2.3
	@RequestMapping(method = RequestMethod.GET)
	public List<CategoryEntity> getAll() {;
		return getDB();
	}
	
	
	// 2.4
	@RequestMapping(method = RequestMethod.POST)
	public CategoryEntity add(@RequestBody CategoryEntity c) {
		
		/*
		 * {
		 *	    "id": 4,
		 *	    "name": "life",
		 *	    "description": "description 4"
		 *	}
		 */
		
		CategoryEntity c0 = new CategoryEntity();
		c0.setId(c.getId());
		c0.setName(c.getName());
		c0.setDescription(c.getDescription());
		
		getDB().add(c0);
		
		return c0;
	}
	
	
	// 2.5
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public CategoryEntity changeCategory(@PathVariable Integer id, @RequestBody CategoryEntity cat) {
		
		for (CategoryEntity c : getDB())
			if (c.getId().equals(cat.getId())) {
				
				if (cat.getName() != null)
					c.setName(cat.getName());
				
				if (cat.getDescription() != null)
					c.setDescription(cat.getDescription());
				
				return c;
			}
		
		return null;
	}
	
	
	// 2.6
	@RequestMapping(method = RequestMethod.DELETE, value ="/{id}")
	public CategoryEntity delete(@PathVariable Integer id) {

		CategoryEntity c = getDB().stream().filter(cat -> cat.getId().equals(id)).findFirst().orElse(null);
		
		if (c == null) {
			return null;
		} else {
			getDB().remove(c);
			return c;
		}
	}
	
	
	// 2.7
	@RequestMapping(method = RequestMethod.GET, value ="/{id}")
	public CategoryEntity getById(@PathVariable Integer id) {
		
		for (CategoryEntity c : getDB())
			if (c.getId().equals(id))
				return c;
		
		return null;
	}
}
