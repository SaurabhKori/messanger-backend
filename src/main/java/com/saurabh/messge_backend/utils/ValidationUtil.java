package com.saurabh.messge_backend.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
@Component
public class ValidationUtil {
	 private final Validator validator;

	    public ValidationUtil(Validator validator) {
	        this.validator = validator;
	    }

	    // Generic method to validate any object
	    public <T> Map<String, String> validate(T object) {
	        Set<ConstraintViolation<T>> violations = validator.validate(object);
	        Map<String, String> errors = new HashMap<>();

	        for (ConstraintViolation<T> violation : violations) {
	            String field = violation.getPropertyPath().toString();
	            String message = violation.getMessage();
	            errors.put(field, message);
	        }

	        return errors;
	    }
}
