package ch.ututor.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ch.ututor.controller.exceptions.FormException;
import ch.ututor.controller.exceptions.form.LoginCredentialsException;
import ch.ututor.controller.exceptions.form.PasswordRepetitionException;
import ch.ututor.controller.pojos.ProfileEditForm;
import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired    UserDao userDao;

	public User update(ProfileEditForm profileEditForm, User user) throws FormException {		
		user.setFirstName(profileEditForm.getFirstName());
		user.setLastName(profileEditForm.getLastName());
		
		user = userDao.save( user );
		
		return user;
	}
	
	public User getAuthenticatedUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userDao.findByUsername(auth.getName());
	}
	
}
