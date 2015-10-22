package ch.ututor.controller.service;

import ch.ututor.controller.exceptions.form.NoResultFoundException;
import ch.ututor.model.TutorLecture;

import java.util.List;

public interface SearchService {
	public List<TutorLecture> searchByLecture(String query) throws NoResultFoundException;
}
