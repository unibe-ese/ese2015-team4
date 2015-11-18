package ch.ututor.service.interfaces;

import ch.ututor.model.User;
import ch.ututor.pojos.SignUpForm;

public interface SignupService {
	
	public User createUserAccount(SignUpForm signUpForm);
}