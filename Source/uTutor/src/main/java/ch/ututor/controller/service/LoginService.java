package ch.ututor.controller.service;

import ch.ututor.controller.exceptions.FormException;
import ch.ututor.controller.pojos.LoginForm;
import ch.ututor.model.User;

public interface LoginService {
	
	public User login(LoginForm loginForm) throws FormException;
	
}
