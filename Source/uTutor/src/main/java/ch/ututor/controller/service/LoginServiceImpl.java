package ch.ututor.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.controller.exceptions.UserAlreadyExistsException;
import ch.ututor.controller.pojos.SignUpForm;
import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;

@Service
public class LoginServiceImpl implements LoginService {
	
	@Autowired    UserDao userDao;
	
	public User saveForm(SignUpForm signUpForm) throws UserAlreadyExistsException{
		
		String email = signUpForm.getEmail();
		User user = userDao.findByEmail( email );
		
		if ( user != null ){
			throw new UserAlreadyExistsException( "There's already a user registered "
					+ "with this email address!" );
		} else {
			user = new User();
			
			user.setFirstName( signUpForm.getFirstName() );
			user.setLastName( signUpForm.getLastName() );
			user.setEmail( signUpForm.getEmail() );
			user.setPassword( signUpForm.getPassword() );
			
			user = userDao.save( user );
		}
		
		return user;
		
	}
	
}
