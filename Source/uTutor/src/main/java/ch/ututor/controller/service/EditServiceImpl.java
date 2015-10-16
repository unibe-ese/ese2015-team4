package ch.ututor.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.controller.exceptions.FormException;
import ch.ututor.controller.exceptions.form.LoginCredentialsException;
import ch.ututor.controller.exceptions.form.PasswordRepetitionException;
import ch.ututor.controller.pojos.EditForm;
import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;

@Service
public class EditServiceImpl implements EditService {
	
	@Autowired    UserDao userDao;

	public User update(EditForm editForm, Long userId) throws FormException {
		User user = userDao.findById(userId);
		
		 if(!editForm.getPassword().equals(editForm.getPasswordRepeat()))
				throw new PasswordRepetitionException("The password repetition did not match.");
		
		user.setFirstName(editForm.getFirstName());
		user.setLastName(editForm.getLastName());
		user.setPassword(editForm.getPassword());
		
		user = userDao.save( user );
		
		return user;
	}
	
}
