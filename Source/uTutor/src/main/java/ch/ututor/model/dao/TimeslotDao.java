package ch.ututor.model.dao;

import org.springframework.data.repository.CrudRepository;
import ch.ututor.model.Timeslot;

public interface TimeslotDao extends CrudRepository<Timeslot, Long> {
	
}