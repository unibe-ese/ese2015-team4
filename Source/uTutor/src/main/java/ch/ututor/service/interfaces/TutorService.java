package ch.ututor.service.interfaces;

import java.text.ParseException;
import java.util.List;

import ch.ututor.model.Timeslot;
import ch.ututor.model.TutorLecture;
import ch.ututor.model.User;
import ch.ututor.pojos.AddLectureForm;
import ch.ututor.pojos.AddTimeslotsForm;
import ch.ututor.pojos.BecomeTutorForm;

public interface TutorService {
	
	public User becomeTutor( BecomeTutorForm becomeTutorForm );
	
	public TutorLecture addTutorLecture(AddLectureForm addLectureForm);
	
	public List<Timeslot> addTimeSlots(AddTimeslotsForm addTimeSlotsForm) throws ParseException;
	
	public List<TutorLecture> findLecturesByTutor( User tutor );
	
	public void deleteTutorLecture( Long lectureId );
	
	public boolean hasLectures(User tutor);
	
	public BecomeTutorForm preFillBecomeTutorForm( BecomeTutorForm becomeTutorForm );
	
	public List<String> getPossibleTimeslots();
}