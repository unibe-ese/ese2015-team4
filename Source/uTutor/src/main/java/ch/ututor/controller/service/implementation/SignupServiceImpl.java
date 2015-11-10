package ch.ututor.controller.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.controller.exceptions.custom.PasswordRepetitionException;
import ch.ututor.controller.exceptions.custom.UserAlreadyExistsException;
import ch.ututor.controller.pojos.SignUpForm;
import ch.ututor.controller.service.SignupService;
import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;

@Service
public class SignupServiceImpl implements SignupService {
	
	@Autowired   private UserDao userDao;

	/**
	 *	@param signUpForm	mustn't be null
	 *  @throws UserAlreadyExistsException if there's already a user with the same email
	 *  @throws PasswordRepetitionException if the repeated password doesn't match the real password
	 */
	public User createUserAccount( SignUpForm signUpForm ) throws UserAlreadyExistsException, PasswordRepetitionException {
		assert( signUpForm != null );
		
		String email = signUpForm.getEmail();
		User user = userDao.findByUsername( email );
		
		if( user != null ) {
			throw new UserAlreadyExistsException( "There's already a user registered with this email address!" );
		} else if( !signUpForm.getPassword( ).equals( signUpForm.getPasswordRepeat() ) ) {
			throw new PasswordRepetitionException( "The password repetition did not match." );
		}

		return createUserFromSignUpForm( signUpForm );
	}
	
	private User createUserFromSignUpForm( SignUpForm signUpForm ) {
		User user = new User();
			
		user.setFirstName( signUpForm.getFirstName() );
		user.setLastName( signUpForm.getLastName() );
		user.setUsername( signUpForm.getEmail() );
		user.setPassword( signUpForm.getPassword() );
		
		return userDao.save( user );
	}
	
}