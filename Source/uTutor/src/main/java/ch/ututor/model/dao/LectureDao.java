package ch.ututor.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.ututor.model.Lecture;
import ch.ututor.model.User;

public interface LectureDao extends CrudRepository<Lecture, Long> {
	
	public Lecture findByNameLike( String name );
	
	public Lecture findByName( String name );
	
}
