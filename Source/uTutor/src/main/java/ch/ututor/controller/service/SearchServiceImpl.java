package ch.ututor.controller.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.controller.exceptions.form.NoResultFoundException;
import ch.ututor.model.TutorLecture;
import ch.ututor.model.dao.TutorLectureDao;

@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired    TutorLectureDao tutorLectureDao;

	public List<TutorLecture> searchByLecture(String query) throws NoResultFoundException {
		List<TutorLecture> lectures = tutorLectureDao.findByLectureNameLike('%'+query+'%');
		if(lectures.size()==0){
			throw new NoResultFoundException("No lectures found.");
		}
		return lectures;
	}
}
