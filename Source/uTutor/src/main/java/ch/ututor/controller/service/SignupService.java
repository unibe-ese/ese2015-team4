package ch.ututor.controller.service;

import ch.ututor.controller.exceptions.FormException;
import ch.ututor.controller.pojos.SignUpForm;
import ch.ututor.model.User;

public interface SignupService {
	
	public User saveForm(SignUpForm signupForm) throws FormException;
}