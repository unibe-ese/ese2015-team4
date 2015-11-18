package ch.ututor.controller.service.tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import ch.ututor.controller.exceptions.custom.InvalidPriceException;
import ch.ututor.controller.exceptions.custom.NoLecturesFoundException;
import ch.ututor.controller.exceptions.custom.TutorLectureAlreadyExistsException;
import ch.ututor.controller.pojos.AddLectureForm;
import ch.ututor.controller.pojos.BecomeTutorForm;
import ch.ututor.controller.service.AuthenticatedUserLoaderService;
import ch.ututor.controller.service.TutorService;
import ch.ututor.model.Lecture;
import ch.ututor.model.TutorLecture;
import ch.ututor.model.User;
import ch.ututor.model.dao.LectureDao;
import ch.ututor.model.dao.TutorLectureDao;
import ch.ututor.model.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/test/tutorService.xml"})
public class TutorServiceTest {

	@Autowired TutorService tutorService;
	@Autowired AuthenticatedUserLoaderService authenticatedUserLoaderService;
	@Autowired UserDao userDao;
	@Autowired LectureDao lectureDao;
	@Autowired TutorLectureDao tutorLectureDao;
	
	private BecomeTutorForm becomeTutorForm;
	private AddLectureForm addLectureForm;
	private User authenticatedUser;
	private List<TutorLecture> authLectures;
	
	@Before
	public void setup() {
		setupUserDao();
		setupLectureDao();
		setupTutorLectureDao();
		setupAuthenticatedUser();
		setupEmptyLecture();
		setupEmptyTutorLectures();
		becomeTutorForm = new BecomeTutorForm();
		addLectureForm = new AddLectureForm();
	}
	
	@Test
	public void testBecomeTutorAddLectureDeleteLecture() {
		setupAuthenticatedUser();
		setupEmptyLecture();
		setupEmptyTutorLectures();
		
		setupBecomeTutorForm();
		Lecture newLecture = setupSingleLecture(1L, becomeTutorForm.getLecture());
		setupAddTutorLecture(authenticatedUser, newLecture, becomeTutorForm.getGrade());
		
		tutorService.becomeTutor(becomeTutorForm);
		
		assertTrue(authenticatedUser.getIsTutor());
		assertEquals(Float.parseFloat(becomeTutorForm.getPrice()), authenticatedUser.getPrice(), 0.0f);
		assertTrue(tutorService.hasLectures(authenticatedUser));
		assertEquals(1, tutorService.findLecturesByTutor(authenticatedUser).size());

		setupAddLectureForm();
		Lecture addLecture = setupSingleLecture(2L, addLectureForm.getLecture());
		setupAddTutorLecture(authenticatedUser, addLecture, addLectureForm.getGrade());
		when(tutorLectureDao.findByTutorAndLecture(any(User.class), any(Lecture.class))).thenReturn(null);
		
		tutorService.addTutorLecture(addLectureForm);
		
		assertEquals(2, tutorService.findLecturesByTutor(authenticatedUser).size());
		
		tutorService.deleteTutorLecture(1L);
		setupEmptyTutorLectures();
		setupAddTutorLecture(authenticatedUser, newLecture, becomeTutorForm.getGrade());
		assertEquals(1, tutorService.findLecturesByTutor(authenticatedUser).size());
	}
	
	@Test
	public void testHasNoLectures() {
		setupEmptyTutorLectures();
		assertFalse(tutorService.hasLectures(authenticatedUser));
	}
	
	@Test(expected = NoLecturesFoundException.class)
	public void testNoLecturesFoundException() {
		setupEmptyTutorLectures();
		tutorService.findLecturesByTutor(new User());
	}
	
	@Test(expected = TutorLectureAlreadyExistsException.class)
	public void testTutorLectureAlreadyExistsException() {
		when(tutorLectureDao.findByTutorAndLecture(any(User.class), any(Lecture.class))).thenReturn(new TutorLecture());
		tutorService.addTutorLecture(addLectureForm);
	}
	
	@Test
	public void testPrefillBecomeTutorForm() {
		User tutorUser = new User();
		tutorUser.setPrice(20.0f);
		tutorUser.setDescription("I'm a tutor!");
		when(authenticatedUserLoaderService.getAuthenticatedUser()).thenReturn(tutorUser);
		
		becomeTutorForm = tutorService.preFillBecomeTutorForm(becomeTutorForm);
		
		assertEquals("20.00", becomeTutorForm.getPrice());
		assertEquals("I'm a tutor!", becomeTutorForm.getDescription());
	}
	
	@Test(expected = InvalidPriceException.class)
	public void testInvalidPriceNegative() {
		setupBecomeTutorForm();
		becomeTutorForm.setPrice("-3");
		
		tutorService.becomeTutor(becomeTutorForm);
	}
	
	@Test(expected = InvalidPriceException.class)
	public void testInvalidPriceText() {
		setupBecomeTutorForm();
		becomeTutorForm.setPrice("I'm working for free!");
		
		tutorService.becomeTutor(becomeTutorForm);
	}
	
	
	private void setupEmptyTutorLectures() {
		authLectures = new ArrayList<TutorLecture>();
		when(tutorLectureDao.findByTutorOrderByLectureName(any(User.class))).thenReturn(authLectures);
		when(tutorLectureDao.findByTutorAndLecture(any(User.class), any(Lecture.class))).thenReturn(null);
		when(tutorLectureDao.findByTutorAndId(any(User.class), any(Long.class))).thenReturn(null);
	}
	
	private void setupAddTutorLecture(User tutor, Lecture lecture, float grade) {
		TutorLecture tutorLecture = new TutorLecture();
		tutorLecture.setGrade(grade);
		tutorLecture.setLecture(lecture);
		tutorLecture.setTutor(tutor);
		authLectures.add(tutorLecture);
		
		when(tutorLectureDao.findByTutorOrderByLectureName(any(User.class))).thenReturn(authLectures);
		when(tutorLectureDao.findByTutorAndLecture(any(User.class), any(Lecture.class))).thenReturn(tutorLecture);
		when(tutorLectureDao.findByTutorAndId(any(User.class), any(Long.class))).thenReturn(tutorLecture);
	}
	
	private Lecture setupSingleLecture(Long id, String lectureName) {
		Lecture lecture = new Lecture();
		lecture.setId(id);
		lecture.setName(lectureName);
		when(lectureDao.findByName(any(String.class))).thenReturn(lecture);
		return lecture;
	}
	
	private void setupEmptyLecture() {
		when(lectureDao.findByName(any(String.class))).thenReturn(null);
	}
	
	private void setupBecomeTutorForm() {
		becomeTutorForm.setDescription("Hermione Jean Granger (b. 19 September, 1979) was an English Muggle-born witch and the only daughter of Mr and Mrs Granger, both dentists in London.");
		becomeTutorForm.setPrice("50.0");
		becomeTutorForm.setLecture("Defense Against the Dark Arts");
		becomeTutorForm.setGrade(6.0f);
	}
	
	private void setupAddLectureForm() {
		addLectureForm.setLecture("Care for Magical Creatures");
		addLectureForm.setGrade(6.0f);
	}
	
	private void setupUserDao() {
		when(userDao.save(any(User.class))).then(returnsFirstArg());
	}
	
	private void setupLectureDao() {
		when(lectureDao.save(any(Lecture.class))).then(returnsFirstArg());
	}
	
	private void setupTutorLectureDao() {
		when(tutorLectureDao.save(any(TutorLecture.class))).then(returnsFirstArg());
	}

	private void setupAuthenticatedUser() {
		authenticatedUser = new User();
		authenticatedUser.setFirstName("Hermione");
		authenticatedUser.setLastName("Granger");
		authenticatedUser.setPassword("WingardiumLeviosa");
		authenticatedUser.setUsername("hermione.granger@hogwarts.com");
		when(authenticatedUserLoaderService.getAuthenticatedUser()).thenReturn(authenticatedUser);
	}
}
