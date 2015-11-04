package ch.ututor.controller.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.controller.exceptions.NoLecturesFoundException;
import ch.ututor.controller.exceptions.form.InvalidPriceException;
import ch.ututor.controller.exceptions.form.TutorLectureAlreadyExistsException;
import ch.ututor.controller.pojos.AddLectureForm;
import ch.ututor.controller.pojos.BecomeTutorForm;
import ch.ututor.model.Lecture;
import ch.ututor.model.TutorLecture;
import ch.ututor.model.User;
import ch.ututor.model.dao.LectureDao;
import ch.ututor.model.dao.TutorLectureDao;
import ch.ututor.model.dao.UserDao;

/**
 * 	This class provides methods to become tutor and add/delete lectures as a tutor.
 */

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
	
	//TODO: more comments needed?
	
	/**
	 *	Adds the required information (price, description and first lecture)
	 *	to the user profile so that he becomes a tutor.
	 */
	public User becomeTutor(BecomeTutorForm becomeTutorForm) {
		User user = authenticatedUserService.getAuthenticatedUser();
		user = updateUserProfile( user, becomeTutorForm );
		
		Lecture lecture = createOrGetLecture(becomeTutorForm.getLecture());
		
		createTutorLectureDataset(lecture, user, becomeTutorForm.getGrade());
		
		return user;
	}
	
	private User updateUserProfile( User user, BecomeTutorForm becomeTutorForm ){
		float price = validatePrice( becomeTutorForm.getPrice() );
		user.setPrice(price);
		user.setDescription(becomeTutorForm.getDescription());
		return userDao.save(user);
	}
	
	private float validatePrice( String priceEntered ){
		float price;
		
		try{
			price = Float.parseFloat( priceEntered );
		} catch (NumberFormatException e){
			throw new InvalidPriceException("Please enter a valid price!");
		}
		
		if ( price <= 0.0 ){
			throw new InvalidPriceException("Please enter a valid price!");
		}
		
		return price;
	}
	
	/**
	 *	Add a lecture to the tutor's profile by creating the entry
	 *	in the table TutorLecture.
	 */
	public TutorLecture addTutorLecture(AddLectureForm addLectureForm){
		
		User user = authenticatedUserService.getAuthenticatedUser();
		Lecture lecture = createOrGetLecture(addLectureForm.getLecture());
				
		TutorLecture tutorLecture = tutorLectureDao.findByTutorAndLecture(user, lecture);
		
		if (tutorLecture != null){
			throw new TutorLectureAlreadyExistsException("You've already registered this lecutre!");			
		}
		
		tutorLecture = createTutorLectureDataset(lecture, user, addLectureForm.getGrade());
		
		return tutorLecture;
	}
	
	public List<TutorLecture> findLecturesFromTutor(User tutor){
		List<TutorLecture> lectures = tutorLectureDao.findByTutor(tutor);
		
		if (lectures.size() == 0){
			throw new NoLecturesFoundException("No lectures found for this tutor!");
		} 
		return lectures;		
	}
	
	/**
	 *	Deletes a lecture from the tutor's profile
	 */
	public void deleteTutorLecture(Long tutorLectureId){
		User user = authenticatedUserService.getAuthenticatedUser();
		TutorLecture tutorLecture = tutorLectureDao.findByTutorAndId(user, tutorLectureId);
		tutorLectureDao.delete(tutorLecture);
		authenticatedUserService.updateTutor();
	}
	
	/**
	 *	Checks if a lecture already exists or not and creates it
	 *	if necessary.
	 */
	private Lecture createOrGetLecture(String lectureName){
		Lecture lecture = lectureDao.findByName(lectureName);
		if (lecture == null){
			lecture = new Lecture();
			lecture.setName(lectureName);
			lecture = lectureDao.save(lecture);
		}
		return lecture;
	}
	
	private TutorLecture createTutorLectureDataset(Lecture lecture, User tutor, Float grade){
		TutorLecture tutorLecture = new TutorLecture();
		tutorLecture.setLecture(lecture);
		tutorLecture.setTutor(tutor);
		tutorLecture.setGrade(grade);
		return tutorLectureDao.save(tutorLecture);
	}

	public boolean hasLectures(User tutor) {
		if( tutorLectureDao.findByTutor(tutor).isEmpty() )
			return false;
		else
			return true;
	}
}