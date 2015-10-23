package ch.ututor.controller.service;

import java.util.List;

import ch.ututor.controller.pojos.AddLectureForm;
import ch.ututor.controller.pojos.BecomeTutorForm;
import ch.ututor.model.TutorLecture;
import ch.ututor.model.User;

public interface TutorService {
	
	public User saveForm( BecomeTutorForm becomeTutorForm );

	public TutorLecture addLecture(AddLectureForm addLectureForm);
	
	public List<TutorLecture> findLectures( User user );
	
	public void deleteLecture( Long lectureId );
	
}
