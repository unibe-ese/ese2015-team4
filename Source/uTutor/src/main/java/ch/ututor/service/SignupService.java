package ch.ututor.service;

import ch.ututor.model.User;
import ch.ututor.pojos.SignUpForm;

public interface SignupService {
	
	public User createUserAccount(SignUpForm signUpForm);
}