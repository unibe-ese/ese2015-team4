package ch.ututor.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.controller.exceptions.FormException;
import ch.ututor.controller.exceptions.form.PasswordRepetitionException;
import ch.ututor.controller.exceptions.form.UserAlreadyExistsException;
import ch.ututor.controller.pojos.SignUpForm;
import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;

@Service
public class SignupServiceImpl implements SignupService {
	
	@Autowired    UserDao userDao;
	
	public User saveForm(SignUpForm signUpForm) throws FormException{
		
		String email = signUpForm.getEmail();
		User user = userDao.findByUsername( email );
		
		if ( user != null ){
			throw new UserAlreadyExistsException( "There's already a user registered "
					+ "with this email address!" );
		} else if(!signUpForm.getPassword().equals(signUpForm.getPasswordRepeat())){
			throw new PasswordRepetitionException("The password repetition did not match.");
		} else{
			user = new User();
			
			user.setFirstName( signUpForm.getFirstName() );
			user.setLastName( signUpForm.getLastName() );
			user.setUsername( signUpForm.getEmail() );
			user.setPassword( signUpForm.getPassword() );
			
			user = userDao.save( user );
		}
		
		return user;
		
	}
	
}
