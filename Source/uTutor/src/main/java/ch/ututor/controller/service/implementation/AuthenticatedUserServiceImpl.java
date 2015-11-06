package ch.ututor.controller.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ch.ututor.controller.exceptions.form.PasswordRepetitionException;
import ch.ututor.controller.pojos.ProfileEditForm;
import ch.ututor.controller.pojos.ChangePasswordForm;
import ch.ututor.controller.service.AuthenticatedUserService;
import ch.ututor.controller.service.ProfilePictureService;
import ch.ututor.controller.service.TutorService;
import ch.ututor.controller.service.UserService;
import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;

import java.io.IOException;

/**
 *	This class provides methods to update the basic profile data of the
 *	currently logged-in user.  
 */

@Service
public class AuthenticatedUserServiceImpl implements AuthenticatedUserService {
	
	@Autowired    UserDao userDao;
	@Autowired    UserService userService;
	@Autowired    ProfilePictureService profilePictureService;
	@Autowired    TutorService tutorService;
	
	/**
	 * Prefills the profileEditForm with the information of the currently
	 * logged in user.
	 * 
	 * @param profileEditForm	Should not be null
	 */
	public ProfileEditForm preFillProfileEditForm(ProfileEditForm profileEditForm){
		assert( profileEditForm != null );
		
		User user = getAuthenticatedUser();
		profileEditForm.setFirstName(user.getFirstName());
		profileEditForm.setLastName(user.getLastName());
		
		if( user.getIsTutor() ){
			profileEditForm.setDescription(user.getDescription());
		}else{
			profileEditForm.setDescription(null);
		}
		
		return profileEditForm;
	}
	
	/**
	 * Updates the profile of the currently logged in user with the data
	 * from the ProfileEditForm.
	 * 
	 * @param profileEditForm	should not be null
	 */
	public void updateUserData(ProfileEditForm profileEditForm){
		assert( profileEditForm != null );
		
		User user = getAuthenticatedUser();
		user.setFirstName(profileEditForm.getFirstName());
		user.setLastName(profileEditForm.getLastName());
		
		if ( user.getIsTutor() ){
			user.setDescription(profileEditForm.getDescription());
		}
		
		userDao.save( user );
	}
	
	/**
	 *	@param changePasswordForm	Should not be null.
	 */
	public void updatePassword(ChangePasswordForm changePasswordForm){
		assert( changePasswordForm != null );
		
		User user = getAuthenticatedUser();

		if(!BCrypt.checkpw(changePasswordForm.getOldPassword(), user.getPassword())){
			throw new PasswordRepetitionException("Entered password doesn't match your actual password.");
		}
		
		if(!changePasswordForm.getNewPassword().equals(changePasswordForm.getNewPasswordRepeat())){
			throw new PasswordRepetitionException("The password repetition did not match.");
		}
		
		user.setPassword(changePasswordForm.getNewPassword());
		userDao.save( user );
	}
	
	/**
	 *	@param file		should not be null
	 */
	public void updateProfilePicture(MultipartFile file) throws IOException{
		assert( file != null );
		
		User user = getAuthenticatedUser();
		
		if(profilePictureService.validateUploadedPicture(file)){
			user.setProfilePic(profilePictureService.resizeProfilePicture(file.getBytes()));
			userDao.save( user );
		}
	}
	
	public void removeProfilePicture(){
		User user = getAuthenticatedUser();
		user.setProfilePic(null);
		userDao.save( user );
	}
	
	public boolean getIsTutor() {
		User user = getAuthenticatedUser();
		return user.getIsTutor();
	}
	
	/**
	 * Checks if the currently logged-in user is a tutor by 
	 * checking if he has registered lectures in his profile.
	 */
	public void updateTutorState(){
		User user = getAuthenticatedUser();
		user.setIsTutor( tutorService.hasLectures(user) );
		userDao.save( user );
	}
	
	public User getAuthenticatedUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.load(auth.getName());
	}
}