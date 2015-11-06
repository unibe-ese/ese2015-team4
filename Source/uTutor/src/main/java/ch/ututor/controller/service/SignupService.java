package ch.ututor.controller.service;

import ch.ututor.controller.pojos.SignUpForm;
import ch.ututor.model.User;

public interface SignupService {
	
	public User createUserAccount(SignUpForm signUpForm);
}