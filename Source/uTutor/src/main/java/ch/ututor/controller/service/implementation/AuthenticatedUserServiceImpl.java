package ch.ututor.controller.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ch.ututor.controller.exceptions.custom.PasswordNotCorrectException;
import ch.ututor.controller.exceptions.custom.PasswordRepetitionException;
import ch.ututor.controller.pojos.ProfileEditForm;
import ch.ututor.controller.pojos.ChangePasswordForm;
import ch.ututor.controller.service.AuthenticatedUserLoaderService;
import ch.ututor.controller.service.AuthenticatedUserService;
import ch.ututor.controller.service.ProfilePictureService;
import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;

import java.io.IOException;


@Service
public class AuthenticatedUserServiceImpl implements AuthenticatedUserService {
	
	@Autowired   private UserDao userDao;
	@Autowired   private ProfilePictureService profilePictureService;
	@Autowired   private AuthenticatedUserLoaderService authenticatedUserLoaderService;
	
	/**
	 * Prefills the profileEditForm with the information of the currently
	 * logged in user.
	 * 
	 * @param profileEditForm	mustn't be null
	 */
	public ProfileEditForm preFillProfileEditForm( ProfileEditForm profileEditForm ) {
		assert( profileEditForm != null );
		
		User user = authenticatedUserLoaderService.getAuthenticatedUser();
		profileEditForm.setFirstName(user.getFirstName());
		profileEditForm.setLastName(user.getLastName());
		
		if( user.getIsTutor() ) {
			profileEditForm.setDescription( user.getDescription() );
		} else {
			profileEditForm.setDescription( null );
		}
		
		return profileEditForm;
	}
	
	/**
	 * Updates the profile of the currently logged in user with the data
	 * from the ProfileEditForm.
	 * 
	 * @param profileEditForm	mustn't be null
	 */
	public User updateUserData( ProfileEditForm profileEditForm ) {
		assert( profileEditForm != null );
		
		User user = authenticatedUserLoaderService.getAuthenticatedUser();
		user.setFirstName( profileEditForm.getFirstName() );
		user.setLastName( profileEditForm.getLastName() );
		
		if ( user.getIsTutor() ){
			user.setDescription( profileEditForm.getDescription() );
		}
		
		return userDao.save( user );
	}
	
	/**
	 *	@param changePasswordForm	mustn't be null.
	 */
	public User updatePassword( ChangePasswordForm changePasswordForm ) {
		assert( changePasswordForm != null );
		
		User user = authenticatedUserLoaderService.getAuthenticatedUser();

		if( !BCrypt.checkpw( changePasswordForm.getOldPassword(), user.getPassword() ) ) {
			throw new PasswordNotCorrectException( "Entered password doesn't match your actual password." );
		}
		
		if( !changePasswordForm.getNewPassword().equals( changePasswordForm.getNewPasswordRepeat() ) ) {
			throw new PasswordRepetitionException( "The password repetition did not match." );
		}
		
		user.setPassword( changePasswordForm.getNewPassword() );
		return userDao.save( user );
	}
	
	/**
	 *	@param file		mustn't be null
	 */
	public User updateProfilePicture( MultipartFile file ) throws IOException {
		assert( file != null );
		
		User user = authenticatedUserLoaderService.getAuthenticatedUser();
		
		if( profilePictureService.validateUploadedPicture(file) ) {
			user.setProfilePic( profilePictureService.resizeProfilePicture( file.getBytes() ) );
			userDao.save( user );
		}
		return user;
	}
	
	public User removeProfilePicture(){
		User user = authenticatedUserLoaderService.getAuthenticatedUser();
		user.setProfilePic( null );
		return userDao.save( user );
	}
	
	public boolean getIsTutor() {
		User user = authenticatedUserLoaderService.getAuthenticatedUser();
		return user.getIsTutor();
	}
}