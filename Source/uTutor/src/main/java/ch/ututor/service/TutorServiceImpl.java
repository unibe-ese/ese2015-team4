package ch.ututor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.exceptions.custom.InvalidPriceException;
import ch.ututor.exceptions.custom.TutorLectureAlreadyExistsException;
import ch.ututor.model.Lecture;
import ch.ututor.model.TutorLecture;
import ch.ututor.model.User;
import ch.ututor.model.dao.LectureDao;
import ch.ututor.model.dao.TutorLectureDao;
import ch.ututor.model.dao.UserDao;
import ch.ututor.pojos.AddLectureForm;
import ch.ututor.pojos.BecomeTutorForm;
import ch.ututor.service.interfaces.AuthenticatedUserLoaderService;
import ch.ututor.service.interfaces.TutorService;

@Service
public class TutorServiceImpl implements TutorService {

	@Autowired 	private AuthenticatedUserLoaderService authenticatedUserLoaderService;
	@Autowired	private UserDao userDao;
	@Autowired	private LectureDao lectureDao;
	@Autowired	private TutorLectureDao tutorLectureDao;
	
	/**
	 *	Adds the required information (price, description and first lecture)
	 *	to the user profile so that he becomes a tutor.
	 *
	 *	@param becomeTutorForm	mustn't be null
	 */
	public User becomeTutor( BecomeTutorForm becomeTutorForm ) {
		assert( becomeTutorForm != null );
		
		User user = authenticatedUserLoaderService.getAuthenticatedUser();
		user = updateUserProfile( user, becomeTutorForm );
		
		Lecture lecture = createOrGetLecture( becomeTutorForm.getLecture() );
		
		createTutorLectureDataset( lecture, user, becomeTutorForm.getGrade() );
		
		updateTutorState();
		
		return user;
	}
	
	/**
	 * @param user				mustn't be null
	 * @param becomeTutorForm	mustn't be null
	 */
	private User updateUserProfile( User user, BecomeTutorForm becomeTutorForm ) {
		assert( user != null );
		assert( becomeTutorForm != null );
		
		float price = validatePrice( becomeTutorForm.getPrice() );
		user.setPrice( price );
		user.setDescription( becomeTutorForm.getDescription() );
		return userDao.save( user );
	}
	
	/**
	 *	@throws InvalidPriceException if the parameter can't be parsed into a string
	 *			or if the value is <= 0.
	 */
	private float validatePrice( String priceEntered ) throws InvalidPriceException {
		float price;
		
		try{
			price = Float.parseFloat( priceEntered );
		} catch( NumberFormatException e ) {
			throw new InvalidPriceException( "Please enter a valid price!" );
		}
		
		if ( price <= 0.0 ){
			throw new InvalidPriceException( "Please enter a price greater than zero!" );
		}
		
		return price;
	}
	
	/**
	 *	Add a lecture to the tutor's profile by creating the entry
	 *	in the table TutorLecture.
	 *
	 *	@param addLectureForm	mustn't be null
	 *
	 *	@throws TutorLectureAlreadyExistsException if the tutor has already registered 
	 *			the lecture he wants to add.
	 */
	public TutorLecture addTutorLecture( AddLectureForm addLectureForm ) throws TutorLectureAlreadyExistsException {
		assert( addLectureForm != null );
		
		User user = authenticatedUserLoaderService.getAuthenticatedUser();
		Lecture lecture = createOrGetLecture( addLectureForm.getLecture() );
				
		TutorLecture tutorLecture = tutorLectureDao.findByTutorAndLecture( user, lecture );
		
		if( tutorLecture != null ) {
			throw new TutorLectureAlreadyExistsException( "You've already registered this lecture!" );			
		}
		
		tutorLecture = createTutorLectureDataset( lecture, user, addLectureForm.getGrade() );
		
		return tutorLecture;
	}
	
	/**
	 *	@param tutor		mustn't be null
	 *
	 *	@throws	NoLecturesFoundException if the user hasn't registered any lectures.
	 */
	public List<TutorLecture> findLecturesByTutor( User tutor ) {
		assert( tutor != null );
		
		return tutorLectureDao.findByTutorOrderByLectureName( tutor );		
	}
	
	/**
	 *	Deletes the specified lecture from the tutor's profile
	 */
	public void deleteTutorLecture( Long tutorLectureId ) {
		User user = authenticatedUserLoaderService.getAuthenticatedUser();
		TutorLecture tutorLecture = tutorLectureDao.findByTutorAndId( user, tutorLectureId );
		if( tutorLecture != null ) {
			tutorLectureDao.delete( tutorLecture );
			updateTutorState();
		}
	}
	
	/**
	 *	Checks if a lecture already exists or not and creates it
	 *	if necessary.
	 */
	private Lecture createOrGetLecture( String lectureName ) {
		Lecture lecture = lectureDao.findByName( lectureName );
		if (lecture == null){
			lecture = new Lecture();
			lecture.setName( lectureName );
			lecture = lectureDao.save( lecture );
		}
		return lecture;
	}
	
	private TutorLecture createTutorLectureDataset( Lecture lecture, User tutor, Float grade ) {
		TutorLecture tutorLecture = new TutorLecture();
		tutorLecture.setLecture( lecture );
		tutorLecture.setTutor( tutor );
		tutorLecture.setGrade( grade );
		return tutorLectureDao.save( tutorLecture );
	}
	
	/**
	 *	@param tutor	mustn't null
	 */
	public boolean hasLectures( User tutor ) {
		assert( tutor != null );
		
		if( tutorLectureDao.findByTutorOrderByLectureName( tutor ).isEmpty() )
			return false;
		else
			return true;
	}
	
	/**
	 *	@param becomeTutorForm		mustn't be null
	 */
	public BecomeTutorForm preFillBecomeTutorForm( BecomeTutorForm becomeTutorForm ) {
		assert( becomeTutorForm != null );
		
		User user = authenticatedUserLoaderService.getAuthenticatedUser();
		
		becomeTutorForm.setDescription( user.getDescription() );		
		if( user.getPrice() > 0 ) {
			becomeTutorForm.setPrice( String.format( "%.2f", user.getPrice() ).toString() );
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

	/**
	 * Updates the rating of a tutor
	 * 
	 * @param tutor		The tutor for which the rating will be updated
	 * @param rating	The rating of a tutor should be null (to remove the rating) or between 1 and 5.
	 */
	public User updateTutorRating(User tutor, Integer rating) {
		assert(rating == null || (rating>0 && rating<6));
		
		if( tutor.getIsTutor() ){
			tutor.setRating( rating );
			tutor = userDao.save( tutor );
		}
		return tutor;
	}
}