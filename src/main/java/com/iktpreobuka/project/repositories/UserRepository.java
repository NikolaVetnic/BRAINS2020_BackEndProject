package com.iktpreobuka.project.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.project.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
	
	List<UserEntity> findByFirstName(String firstName);
	List<UserEntity> findByLastName(String lastName);
	
	Optional<UserEntity> findByUsername(String username);
}
