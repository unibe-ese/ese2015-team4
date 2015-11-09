package ch.ututor.controller.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.controller.exceptions.custom.InvalidPriceException;
import ch.ututor.controller.exceptions.custom.NoLecturesFoundException;
import ch.ututor.controller.exceptions.custom.TutorLectureAlreadyExistsException;
import ch.ututor.controller.pojos.AddLectureForm;
import ch.ututor.controller.pojos.BecomeTutorForm;
import ch.ututor.controller.service.AuthenticatedUserLoaderService;
import ch.ututor.controller.service.TutorService;
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
	AuthenticatedUserLoaderService authenticatedUserLoaderService;
	
	@Autowired    
	UserDao userDao;
	
	@Autowired
	LectureDao lectureDao;
	
	@Autowired
	TutorLectureDao tutorLectureDao;
	
	/**
	 *	Adds the required information (price, description and first lecture)
	 *	to the user profile so that he becomes a tutor.
	 *
	 *	@param becomeTutorForm	Should not be null
	 */
	public User becomeTutor(BecomeTutorForm becomeTutorForm) {
		assert( becomeTutorForm != null );
		
		User user = authenticatedUserLoaderService.getAuthenticatedUser();
		user = updateUserProfile( user, becomeTutorForm );
		
		Lecture lecture = createOrGetLecture(becomeTutorForm.getLecture());
		
		createTutorLectureDataset(lecture, user, becomeTutorForm.getGrade());
		
		updateTutorState();
		
		return user;
	}
	
	/**
	 * @param user				Should not be null
	 * @param becomeTutorForm	Should not be null
	 */
	private User updateUserProfile( User user, BecomeTutorForm becomeTutorForm ){
		assert( user != null );
		assert( becomeTutorForm != null );
		
		float price = validatePrice( becomeTutorForm.getPrice() );
		user.setPrice(price);
		user.setDescription(becomeTutorForm.getDescription());
		return userDao.save(user);
	}
	
	/**
	 *	@throws InvalidPriceException if the parameter can't be parsed into a string
	 *			or if the value is <= 0.
	 */
	private float validatePrice( String priceEntered ){
		float price;
		
		try{
			price = Float.parseFloat( priceEntered );
		} catch (NumberFormatException e){
			throw new InvalidPriceException("Please enter a valid price!");
		}
		
		if ( price <= 0.0 ){
			throw new InvalidPriceException("Please enter a price greater than zero!");
		}
		
		return price;
	}
	
	/**
	 *	Add a lecture to the tutor's profile by creating the entry
	 *	in the table TutorLecture.
	 *
	 *	@param addLectureForm	should not be null
	 *
	 *	@throws TutorLectureAlreadyExists if the tutor has already registered 
	 *			the lecture he wants to add.
	 */
	public TutorLecture addTutorLecture(AddLectureForm addLectureForm){
		assert( addLectureForm != null );
		
		User user = authenticatedUserLoaderService.getAuthenticatedUser();
		Lecture lecture = createOrGetLecture(addLectureForm.getLecture());
				
		TutorLecture tutorLecture = tutorLectureDao.findByTutorAndLecture(user, lecture);
		
		if (tutorLecture != null){
			throw new TutorLectureAlreadyExistsException("You've already registered this lecture!");			
		}
		
		tutorLecture = createTutorLectureDataset(lecture, user, addLectureForm.getGrade());
		
		return tutorLecture;
	}
	
	/**
	 *	@param tutor		Should not be null
	 *
	 *	@throws	NoLecturesFoundException if the user hasn't registered any lectures.
	 */
	public List<TutorLecture> findLecturesByTutor(User tutor){
		assert( tutor != null );
		
		List<TutorLecture> lectures = tutorLectureDao.findByTutor(tutor);
		
		if (lectures.size() == 0){
			throw new NoLecturesFoundException("No lectures found for this tutor!");
		} 
		return lectures;		
	}
	
	/**
	 *	Deletes the specified lecture from the tutor's profile
	 */
	public void deleteTutorLecture(Long tutorLectureId){
		User user = authenticatedUserLoaderService.getAuthenticatedUser();
		TutorLecture tutorLecture = tutorLectureDao.findByTutorAndId(user, tutorLectureId);
		if ( tutorLecture != null ){
			tutorLectureDao.delete(tutorLecture);
			updateTutorState();
		}
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
	
	/**
	 *	@param tutor	Should not be null
	 */
	public boolean hasLectures(User tutor) {
		assert( tutor != null );
		
		if( tutorLectureDao.findByTutor(tutor).isEmpty() )
			return false;
		else
			return true;
	}
	
	/**
	 *	@param becomeTutorForm		Should not be null
	 */
	public BecomeTutorForm preFillBecomeTutorForm( BecomeTutorForm becomeTutorForm ){
		assert( becomeTutorForm != null );
		
		User user = authenticatedUserLoaderService.getAuthenticatedUser();
		
		becomeTutorForm.setDescription( user.getDescription() );		
		if(user.getPrice()>0){
			becomeTutorForm.setPrice(String.format("%.2f", user.getPrice()).toString());
		}
		return becomeTutorForm;		
	}
	
	/**
	 * Checks if the currently logged-in user is a tutor by 
	 * checking if he has registered lectures in his profile.
	 */
	private void updateTutorState(){
		User user = authenticatedUserLoaderService.getAuthenticatedUser();
		user.setIsTutor( hasLectures(user) );
		userDao.save( user );
	}
}