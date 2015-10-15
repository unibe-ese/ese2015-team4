package ch.ututor.controller.service;

import ch.ututor.controller.exceptions.UserAlreadyExistsException;
import ch.ututor.controller.pojos.SignUpForm;
import ch.ututor.model.User;

public interface LoginService {
	
	public User saveForm(SignUpForm signupForm) throws UserAlreadyExistsException;
	
}
