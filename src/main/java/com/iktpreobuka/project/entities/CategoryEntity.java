package com.iktpreobuka.project.entities;

public class CategoryEntity {

	
	protected Integer id;
	protected String name;
	protected String description;
	
	
	public CategoryEntity() { }


	public CategoryEntity(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}


	public Integer getId() 			{ return id; 			}
	public String getName() 		{ return name; 			}
	public String getDescription() 	{ return description; 	}


	public void setId(Integer id) 					{ this.id = id; 					}
	public void setName(String name) 				{ this.name = name; 				}
	public void setDescription(String description) 	{ this.description = description; 	}
}
