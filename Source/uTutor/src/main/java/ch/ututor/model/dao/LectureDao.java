package ch.ututor.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.ututor.model.Lecture;

public interface LectureDao extends CrudRepository<Lecture, Long> {
	
	public Lecture findByName( String name );

	public Lecture findById(Long lectureId);
	
}
