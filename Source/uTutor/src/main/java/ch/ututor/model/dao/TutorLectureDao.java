package ch.ututor.model.dao;

import org.springframework.data.repository.CrudRepository;
import ch.ututor.model.TutorLecture;

public interface TutorLectureDao extends CrudRepository<TutorLecture,Long> {
	
}
