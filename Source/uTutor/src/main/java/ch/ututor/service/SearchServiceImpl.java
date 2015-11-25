package ch.ututor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.exceptions.custom.NoResultFoundException;
import ch.ututor.model.TutorLecture;
import ch.ututor.model.dao.TutorLectureDao;
import ch.ututor.service.interfaces.SearchService;

@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired   private TutorLectureDao tutorLectureDao;
	
	/**
	 *	@param query	mustn't be null
	 *
	 *	@throws			NoResultFoundException if no lectures are found for the search term
	 */
	public List<TutorLecture> searchByLecture( String query ) throws NoResultFoundException {
		assert ( query != null );
		
		List<TutorLecture> lectures = tutorLectureDao.findByLectureNameLikeOrderByLectureNameAscTutorRatingDesc( '%' + query + '%' );
		
		if( lectures.size() == 0 ) {
			throw new NoResultFoundException( "No lectures found." );
		}
		
		return lectures;
	}
}