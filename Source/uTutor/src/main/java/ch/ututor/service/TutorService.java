package ch.ututor.service;

import java.util.List;

import ch.ututor.model.TutorLecture;
import ch.ututor.model.User;
import ch.ututor.pojos.AddLectureForm;
import ch.ututor.pojos.BecomeTutorForm;

public interface TutorService {
	
	public User becomeTutor( BecomeTutorForm becomeTutorForm );
	
	public TutorLecture addTutorLecture(AddLectureForm addLectureForm);
	
	public List<TutorLecture> findLecturesByTutor( User tutor );
	
	public void deleteTutorLecture( Long lectureId );
	
	public boolean hasLectures(User tutor);
	
	public BecomeTutorForm preFillBecomeTutorForm( BecomeTutorForm becomeTutorForm );
}