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
	
	public User saveForm(BecomeTutorForm becomeTutorForm) {
		
		String priceEntered = becomeTutorForm.getPrice();
		float price;
		try{
			price = Float.parseFloat(priceEntered);
		} catch (NumberFormatException e){
			throw new InvalidPriceException("Please enter a valid price!");
		}
		
		if ( price <= 0.0 ){
			throw new InvalidPriceException("Please enter a valid price!");
		}
		
		// update user
		User user = authenticatedUserService.getAuthenticatedUser();
		
		user.setPrice(price);
		user.setDescription(becomeTutorForm.getDescription());
		user = userDao.save(user);
		
		// create or get lecture if it exists already
		Lecture lecture = createOrGetLecture(becomeTutorForm.getLecture());
		
		// create relation between tutor and lecture
		createTutorLectureDataset(lecture, user, becomeTutorForm.getGrade());
		
		return user;
	}
	
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
	
	public List<TutorLecture> findLectures(User user){
		List<TutorLecture> lectures = tutorLectureDao.findByTutor(user);
		
		if (lectures.size() == 0){
			throw new NoLecturesFoundException("No lectures found for this tutor!");
		} 
		return lectures;		
	}
	
	public void deleteTutorLecture(Long lectureId){
		User user = authenticatedUserService.getAuthenticatedUser();
		TutorLecture tutorLecture = tutorLectureDao.findByTutorAndId(user, lectureId);
		tutorLectureDao.delete(tutorLecture);
		authenticatedUserService.updateTutor();
	}
	
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
		if(tutorLectureDao.findByTutor(tutor).isEmpty())
			return false;
		else
			return true;
	}
}