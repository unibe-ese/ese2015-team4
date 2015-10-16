package ch.ututor.controller.service;

import ch.ututor.controller.exceptions.FormException;
import ch.ututor.controller.pojos.EditForm;
import ch.ututor.model.User;

public interface EditService {
	
	public User update(EditForm editForm, Long userId) throws FormException;
	
}
