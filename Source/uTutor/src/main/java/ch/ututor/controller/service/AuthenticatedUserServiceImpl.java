package ch.ututor.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ch.ututor.controller.exceptions.FormException;
import ch.ututor.controller.exceptions.form.PasswordRepetitionException;
import ch.ututor.controller.pojos.ProfileEditForm;
import ch.ututor.controller.pojos.ChangePasswordForm;
import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;

import java.io.IOException;

@Service
public class AuthenticatedUserServiceImpl implements AuthenticatedUserService {
	
	@Autowired    UserDao userDao;
	@Autowired    UserService userService;
	@Autowired    ProfilePictureService profilePictureService;
	@Autowired    TutorService tutorService;
	
	public ProfileEditForm fillEditForm(ProfileEditForm profileEditForm){
		User user = getAuthenticatedUser();
		profileEditForm.setFirstName(user.getFirstName());
		profileEditForm.setLastName(user.getLastName());
		if(user.getIsTutor()){
			profileEditForm.setDescription(user.getDescription());
		}else{
			profileEditForm.setDescription(null);
		}
		
		return profileEditForm;
	}

	public User updateData(ProfileEditForm profileEditForm) throws FormException {
		User user = getAuthenticatedUser();
		user.setFirstName(profileEditForm.getFirstName());
		user.setLastName(profileEditForm.getLastName());
		
		if ( user.getIsTutor() ){
			user.setDescription(profileEditForm.getDescription());
		}
		
		return userDao.save( user );
	}
	
	public User updatePassword(ChangePasswordForm changePasswordForm){
		User user = getAuthenticatedUser();
		if(!BCrypt.checkpw(changePasswordForm.getOldPassword(), user.getPassword())){
			throw new PasswordRepetitionException("Entered password doesn't match your actual password.");
		}
		if(!changePasswordForm.getNewPassword().equals(changePasswordForm.getNewPasswordRepeat())){
			throw new PasswordRepetitionException("The password repetition did not match.");
		}
		user.setPassword(changePasswordForm.getNewPassword());
		return userDao.save( user );
	}
	
	public User updateProfilePicture(MultipartFile file) throws IOException{
		User user = getAuthenticatedUser();
		if(profilePictureService.validateUploadedPicture(file)){
			user.setProfilePic(profilePictureService.resizePicture(file.getBytes()));
			userDao.save( user );
		}
		return user;
	}
	
	public User removeProfilePicture(){
		User user = getAuthenticatedUser();
		user.setProfilePic(null);
		userDao.save( user );
		return user;
	}

	public boolean getIsTutor() {
		User user = getAuthenticatedUser();
		return user.getIsTutor();
	}
	
	public User updateTutor(){
		User user = getAuthenticatedUser();
		user.setIsTutor(tutorService.hasLectures(user));
		userDao.save( user );
		return user;
	}
	
	public User getAuthenticatedUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.load(auth.getName());
	}
}