package ch.ututor.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.controller.exceptions.FormException;
import ch.ututor.controller.pojos.BecomeTutorForm;
import ch.ututor.model.Lecture;
import ch.ututor.model.TutorLecture;
import ch.ututor.model.User;
import ch.ututor.model.dao.LectureDao;
import ch.ututor.model.dao.TutorLectureDao;
import ch.ututor.model.dao.UserDao;

@Service
public class TutorServiceImpl implements TutorService {

	@Autowired 
	AuthenticatedUserService authenticatedUserService;
	
	@Autowired    
	UserDao userDao;
	
	@Autowired
	LectureDao lectureDao;
	
	@Autowired
	TutorLectureDao tutorLectureDao;
	
	public User saveForm( BecomeTutorForm becomeTutorForm ) {
		
		String priceEntered = becomeTutorForm.getPrice();
		float price;
		try{
			price = Float.parseFloat( priceEntered );
		} catch (NumberFormatException e){
			throw new FormException("Please enter a valid price!");
		}
		
		if ( price <= 0.0 ){
			throw new FormException("Please enter a valid price!");
		}
		
		// update user
		User user = authenticatedUserService.getAuthenticatedUser();
		
		user.setPrice( price );
		user.setDescription( becomeTutorForm.getDescription() );
		user.setIsTutor( true );
		user = userDao.save( user );
		
		// create or get lecture
		Lecture lecture = lectureDao.findByName( becomeTutorForm.getLecture() );
		if ( lecture == null ){
			lecture = new Lecture();
			lecture.setName( becomeTutorForm.getLecture() );
			lecture = lectureDao.save( lecture );
		}
		
		// create relation
		TutorLecture tutorLecture = new TutorLecture();
		tutorLecture.setLecture( lecture );
		tutorLecture.setTutor( user );
		tutorLecture.setGrade( becomeTutorForm.getGrade() );
		tutorLecture = tutorLectureDao.save( tutorLecture );
		
		return user;
		
	}
	
	
	
	
}