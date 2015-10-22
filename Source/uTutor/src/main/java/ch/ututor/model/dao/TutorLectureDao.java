package ch.ututor.model.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ch.ututor.model.TutorLecture;

public interface TutorLectureDao extends CrudRepository<TutorLecture,Long> {
	public List<TutorLecture> findByLectureNameLike(String lecture);
}
