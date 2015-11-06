package ch.ututor.controller.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import ch.ututor.controller.pojos.ProfileEditForm;
import ch.ututor.controller.pojos.ChangePasswordForm;
import ch.ututor.model.User;

public interface AuthenticatedUserService {
	public ProfileEditForm preFillProfileEditForm(ProfileEditForm profileEditForm);
	public User updateUserData(ProfileEditForm profileEditForm);
	public User updatePassword(ChangePasswordForm changePasswordForm);
	public User updateProfilePicture(MultipartFile file) throws IOException;
	public User removeProfilePicture();
	public boolean getIsTutor();
}