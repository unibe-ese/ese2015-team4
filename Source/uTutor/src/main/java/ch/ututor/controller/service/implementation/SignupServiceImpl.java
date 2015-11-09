package ch.ututor.controller.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.controller.exceptions.form.PasswordRepetitionException;
import ch.ututor.controller.exceptions.form.UserAlreadyExistsException;
import ch.ututor.controller.pojos.SignUpForm;
import ch.ututor.controller.service.SignupService;
import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;

/**
 *	This class offers the method to create a new user account from a SignUpForm.
 */

@Service
public class SignupServiceImpl implements SignupService {
	
	@Autowired    UserDao userDao;

	/**
	 *	@param signUpForm	should not be null
	 */
	public User createUserAccount(SignUpForm signUpForm){
		assert( signUpForm != null );
		
		String email = signUpForm.getEmail();
		System.out.println(email);
		User user = userDao.findByUsername(email);
		
		if(user != null){
			throw new UserAlreadyExistsException("There's already a user registered with this email address!");
		}else if( !signUpForm.getPassword( ).equals( signUpForm.getPasswordRepeat() ) ){
			throw new PasswordRepetitionException("The password repetition did not match.");
		}

		return createUserFromSignUpForm( signUpForm );
	}
	
	private User createUserFromSignUpForm( SignUpForm signUpForm ){
		User user = new User();
			
		user.setFirstName(signUpForm.getFirstName());
		user.setLastName(signUpForm.getLastName());
		user.setUsername(signUpForm.getEmail());
		user.setPassword(signUpForm.getPassword());
		
		return userDao.save(user);
	}
	
}