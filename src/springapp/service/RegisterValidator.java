package springapp.service;

import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;

import org.apache.log4j.Logger;

import springapp.domain.User;

public class RegisterValidator implements Validator {

    /** Logger for this class and subclasses */
    protected final Logger logger = Logger.getLogger(getClass());

    public boolean supports(Class clazz) {
        return User.class.equals(clazz);
    }
    
    
    public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(
			errors, "username", "required.username");
		ValidationUtils.rejectIfEmptyOrWhitespace(
				errors, "password", "required.password");
		ValidationUtils.rejectIfEmptyOrWhitespace(
				errors, "imeiNumber", "required.imeiNumber");
		ValidationUtils.rejectIfEmptyOrWhitespace(
					errors, "emailAddress", "required.emailAddress");
	}
}