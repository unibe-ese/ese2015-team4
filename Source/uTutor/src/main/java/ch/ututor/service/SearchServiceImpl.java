package ch.ututor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
	public List<TutorLecture> searchByLecture( String query, String sortAttribute ) throws NoResultFoundException {
		assert ( query != null );
		
		Sort sort = getSortFromString( sortAttribute );
				
		List<TutorLecture> lectures = tutorLectureDao.findByLectureNameLike( '%' + query + '%', sort);
		
		if( lectures.size() == 0 ) {
			throw new NoResultFoundException( "No lectures found." );
		}
		
		return lectures;
	}
	
	/**
	 * @param sortString needs to be equal to "lecture.name", "tutor.rating", "tutorlecture.grade" or "tutor.price" otherwise "lecture.name" will be assumed. 
	 * @return Sort which sorts the results by lecture name ascending and after according to the given sortString.
	 */
	private Sort getSortFromString( String sortString ){
		if(sortString == null || (!sortString.equals("tutor.rating") && !sortString.equals("grade") && !sortString.equals("tutor.price"))){
			return new Sort(Sort.Direction.ASC, "lecture.name").and(new Sort(Sort.Direction.DESC, "tutor.rating"));
		}
		
		Sort.Direction sortDirection = Sort.Direction.DESC;
		if(sortString == "price"){
			sortDirection = Sort.Direction.ASC;
		}
		
		return new Sort(sortDirection, sortString).and(new Sort(Sort.Direction.ASC, "lecture.name"));
	}
}