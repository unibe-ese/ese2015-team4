package ch.ututor.controller.service;

import java.util.List;

import ch.ututor.controller.pojos.AddLectureForm;
import ch.ututor.controller.pojos.BecomeTutorForm;
import ch.ututor.model.TutorLecture;
import ch.ututor.model.User;

public interface TutorService {
	
	public User becomeTutor( BecomeTutorForm becomeTutorForm );
	
	public TutorLecture addTutorLecture(AddLectureForm addLectureForm);
	
	public List<TutorLecture> findLecturesByTutor( User tutor );
	
	public void deleteTutorLecture( Long lectureId );
	
	public boolean hasLectures(User tutor);
	
	public BecomeTutorForm preFillBecomeTutorForm( BecomeTutorForm becomeTutorForm );

}