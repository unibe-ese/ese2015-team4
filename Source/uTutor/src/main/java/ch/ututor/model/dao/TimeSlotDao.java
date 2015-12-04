package ch.ututor.model.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ch.ututor.model.TimeSlot;
import ch.ututor.model.User;

public interface TimeSlotDao extends CrudRepository<TimeSlot, Long> {
	
	public TimeSlot findByBeginDateTimeAndTutor( Date date, User tutor );
	public List<TimeSlot> findByTutorOrStudentOrderByBeginDateTimeAsc( User tutor, User student );
	public List<TimeSlot> findByTutorAndRatingNotNull( User tutor );
	public TimeSlot findById( long id );

}