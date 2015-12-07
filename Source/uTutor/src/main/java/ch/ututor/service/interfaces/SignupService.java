package ch.ututor.service.interfaces;

import ch.ututor.model.User;
import ch.ututor.pojos.SignUpForm;

public interface SignUpService {
	
	public User createUserAccount(SignUpForm signUpForm);
	
}