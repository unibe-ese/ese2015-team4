package ch.ututor.model.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import ch.ututor.model.Lecture;
import ch.ututor.model.TutorLecture;
import ch.ututor.model.User;

public interface TutorLectureDao extends CrudRepository<TutorLecture,Long> {
	
	public List<TutorLecture> findByLectureNameLike( String lecture, Sort sort );
	public TutorLecture findByTutorAndLecture( User tutor, Lecture lecture );
	public TutorLecture findByTutorAndId( User tutor, Long tutorLectureId );
	public List<TutorLecture> findByTutorOrderByLectureName( User tutor );
}