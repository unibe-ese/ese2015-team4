package ch.ututor.service.interfaces;
import ch.ututor.model.TutorLecture;

import java.util.List;

public interface SearchService {
	
	public List<TutorLecture> searchByLecture(String query);
}