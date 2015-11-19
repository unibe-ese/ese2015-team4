package ch.ututor.model.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ch.ututor.model.Timeslot;
import ch.ututor.model.User;

public interface TimeslotDao extends CrudRepository<Timeslot, Long> {
	
	public Timeslot findByBeginDateTimeAndTutor( Date date, User tutor );
	
	public List<Timeslot> findByTutorOrStudent( User tutor, User user );

}