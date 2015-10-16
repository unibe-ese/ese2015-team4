package ch.ututor.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.controller.exceptions.FormException;
import ch.ututor.controller.exceptions.form.LoginCredentialsException;
import ch.ututor.controller.pojos.LoginForm;
import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;

@Service
public class LoginServiceImpl implements LoginService {
	
	@Autowired    UserDao userDao;
	
	public User login(LoginForm loginForm) throws FormException {
		
		String email = loginForm.getEmail();
		User user = userDao.findByEmail( email );
		
		if ( user == null || !user.getPassword().equals(loginForm.getPassword())){
			throw new LoginCredentialsException( "Email or password not correct." );
		}
		
		return user;
		
	}
	
}
