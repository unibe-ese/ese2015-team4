package ch.ututor.tests.service;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.ututor.exceptions.custom.NoResultFoundException;
import ch.ututor.model.TutorLecture;
import ch.ututor.model.dao.TutorLectureDao;
import ch.ututor.service.interfaces.SearchService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/test/resources/searchService.xml"})
public class SearchServiceTest {

		@Autowired SearchService searchService;
		@Autowired TutorLectureDao tutorLectureDao;
		
		private List<TutorLecture> dbLectures;
		
		@Test(expected = NoResultFoundException.class)
		public void testNoResultFoundException() {
			searchService.searchByLecture("", null);
		}
		
		@Test
		public void testResultFound() {
			dbLectures = new ArrayList<TutorLecture>();
			dbLectures.add(new TutorLecture());
			
			when(tutorLectureDao.findByLectureNameLike(any(String.class), any(Sort.class))).thenReturn(dbLectures);
			
			List<TutorLecture> lectures = searchService.searchByLecture("lecture", null);
			assertFalse(lectures.isEmpty());
		}
}
