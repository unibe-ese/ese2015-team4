package ch.ututor.controller.service;

import ch.ututor.controller.pojos.BecomeTutorForm;
import ch.ututor.model.User;

public interface TutorService {
	
	public User saveForm( BecomeTutorForm becomeTutorForm );
	
}
