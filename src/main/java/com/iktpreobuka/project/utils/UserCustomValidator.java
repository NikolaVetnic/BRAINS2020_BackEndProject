package com.iktpreobuka.project.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.iktpreobuka.project.entities.UserEntity;

@Component
public class UserCustomValidator implements Validator {

	
	@Override
	public boolean supports(Class<?> clazz) {
		return UserEntity.class.equals(clazz);
	}

	
	@Override
	public void validate(Object target, Errors errors) {
		
		UserEntity user = (UserEntity) target;
		
		if (!user.getPassword().equals(user.getConfirmPassword()))
			errors.reject("400", "Password and confirm password fields must match.");
	}
}
