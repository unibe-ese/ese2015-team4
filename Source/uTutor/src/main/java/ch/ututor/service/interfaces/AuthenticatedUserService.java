package ch.ututor.service.interfaces;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import ch.ututor.model.User;
import ch.ututor.pojos.ChangePasswordForm;
import ch.ututor.pojos.ProfileEditForm;

public interface AuthenticatedUserService {
	public ProfileEditForm preFillProfileEditForm(ProfileEditForm profileEditForm);
	public User updateUserData(ProfileEditForm profileEditForm);
	public User updatePassword(ChangePasswordForm changePasswordForm);
	public User updateProfilePicture(MultipartFile file) throws IOException;
	public User removeProfilePicture();
	public boolean getIsTutor();
}