package ch.ututor.controller.service;

import ch.ututor.controller.exceptions.FormException;
import ch.ututor.controller.pojos.ProfileEditForm;
import ch.ututor.model.User;

public interface UserService {
	
	public User update(ProfileEditForm profileEditForm, User user) throws FormException;
	public User getAuthenticatedUser();
}
