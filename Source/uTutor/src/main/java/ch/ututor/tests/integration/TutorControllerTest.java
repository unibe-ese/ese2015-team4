package ch.ututor.tests.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import ch.ututor.model.Lecture;
import ch.ututor.model.TutorLecture;
import ch.ututor.model.User;
import ch.ututor.model.dao.LectureDao;
import ch.ututor.model.dao.TutorLectureDao;
import ch.ututor.model.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"
		})
@Transactional
@Rollback
public class TutorControllerTest {

	@Autowired
	private WebApplicationContext wac;
	@Autowired
	UserDao userDao;
	@Autowired
	TutorLectureDao tutorLectureDao;
	@Autowired
	LectureDao lectureDao;
	private MockMvc mockMvc;
	private User user;
	private TutorLecture tutorLecture;
	private Lecture lecture;
	
	private void dataSetup(){
		userDao.deleteAll();
		tutorLectureDao.deleteAll();
		lectureDao.deleteAll();
		
		user = new User();
		user.setFirstName("Hermione");
		user.setLastName("Granger");
		user.setUsername("hermione@hogwarts.ch");
		user = userDao.save(user);
		
		lecture = new Lecture();
		lecture.setName("Potions");
		lecture = lectureDao.save(lecture);
		
		tutorLecture = new TutorLecture();
		tutorLecture.setTutor(user);
		tutorLecture.setGrade(5);
		tutorLecture.setLecture(lecture);
		tutorLecture = tutorLectureDao.save(tutorLecture);
	}
	
	@Before
	public void setup() {
		dataSetup();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	@WithMockUser(username="hermione@hogwarts.ch", roles={"USER"})
	public void testDisplayBecomeTutorForm() throws Exception{
		
		this.mockMvc
			.perform(get("/user/become-tutor"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/become-tutor.jsp"))
			.andExpect(model().attributeExists("becomeTutorForm"));
	}
	
	@Test
	@WithMockUser(username="hermione@hogwarts.ch", roles={"USER"})
	public void testTutorGetsBecomeTutorForm() throws Exception{
		user.setDescription("Hi I'm Hermione");
		user.setPrice(99.9F);
		user = userDao.save(user);
		
		this.mockMvc
			.perform(get("/user/become-tutor"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/become-tutor.jsp"))
			.andExpect(model().attributeExists("becomeTutorForm"))
			.andExpect(model().attribute("becomeTutorForm", hasProperty("description", is("Hi I'm Hermione"))))
			.andExpect(model().attribute("becomeTutorForm", hasProperty("price", is("99.9"))));
	}
	
	@Test
	@WithMockUser(username="hermione@hogwarts.ch", roles={"USER"})
	public void testGetIsAlreadyTutor() throws Exception{
		user.setIsTutor(true);
		user = userDao.save(user);
		
		this.mockMvc
			.perform(get("/user/become-tutor"))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/user/profile"));
	
	}
	
	@Test
	@WithMockUser(username="hermione@hogwarts.ch", roles={"USER"})
	public void testPostIsAlreadyTutor() throws Exception{
		user.setIsTutor(true);
		user = userDao.save(user);
		
		this.mockMvc
			.perform(post("/user/become-tutor"))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/user/profile"));
	}
	
	@Test
	@WithMockUser(username="hermione@hogwarts.ch", roles={"USER"})
	public void testInvalidBecomeTutorForm() throws Exception{
		
		this.mockMvc
			.perform(post("/user/become-tutor"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/become-tutor.jsp"))
			.andExpect(model().attributeHasFieldErrors("becomeTutorForm", "price"));
	}
	
	@Test
	@WithMockUser(username="hermione@hogwarts.ch", roles={"USER"})
	public void testValidBecomeTutorForm() throws Exception{
		
		this.mockMvc
			.perform(post("/user/become-tutor")
					.param("grade", "" +4)
					.param("lecture", "Transformation")
					.param("price", Float.toString( 50 ))
					.param("description", "Hi I'm Hermione"))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/user/profile"));
	}
	
	@Test
	@WithMockUser(username="hermione@hogwarts.ch", roles={"USER"})
	public void testInvalidPriceExceptionNoFloat() throws Exception{
		
		this.mockMvc
			.perform(post("/user/become-tutor")
					.param("grade", "" +4)
					.param("lecture", "Defense against the dark arts")
					.param("price", "hello")
					.param("description", "Hi I'm Hermione"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/become-tutor.jsp"))
			.andExpect(model().attribute("exception_message", "Please enter a valid price!"));
	}
	
	@Test
	@WithMockUser(username="hermione@hogwarts.ch", roles={"USER"})
	public void testInvalidPriceExceptionLessThanZero() throws Exception{
		
		this.mockMvc
			.perform(post("/user/become-tutor")
					.param("grade", "" +4)
					.param("lecture", "Defense against the dark arts")
					.param("price", "" + -5)
					.param("description", "Hi I'm Hermione"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/become-tutor.jsp"))
			.andExpect(model().attribute("exception_message", "Please enter a price greater than zero!"));
	}
	
	@Test
	@WithMockUser(username="hermione@hogwarts.ch", roles={"USER"})
	public void testGetAddLecturesIsNoTutor() throws Exception{
		
		this.mockMvc
			.perform(get("/user/add-lecture"))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/user/become-tutor"));
	}
	
	@Test
	@WithMockUser(username="hermione@hogwarts.ch", roles={"USER"})
	public void testValidAddLecture() throws Exception{
		user.setIsTutor(true);
		user = userDao.save(user);
		
		this.mockMvc
			.perform(get("/user/add-lecture"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/add-lecture.jsp"))
			.andExpect(model().attributeExists("addLectureForm"));
	}
	
	@Test
	@WithMockUser(username="hermione@hogwarts.ch", roles={"USER"})
	public void testPostAddLecturesIsNoTutor() throws Exception{
		
		this.mockMvc
			.perform(post("/user/add-lecture"))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/user/become-tutor"));
	}
	
	@Test
	@WithMockUser(username="hermione@hogwarts.ch", roles={"USER"})
	public void testInvalidAddLectureForm() throws Exception{
		user.setIsTutor(true);
		user = userDao.save(user);
		
		this.mockMvc
			.perform(post("/user/add-lecture"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/add-lecture.jsp"));
	}
	
	@Test
	@WithMockUser(username="hermione@hogwarts.ch", roles={"USER"})
	public void testValidAddLectureForm() throws Exception{
		user.setIsTutor(true);
		user = userDao.save(user);
		
		this.mockMvc
			.perform(post("/user/add-lecture")
					.param("grade", "" +4)
					.param("lecture", "Charms"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/user/profile"));
	}
	
	@Test
	@WithMockUser(username="hermione@hogwarts.ch", roles={"USER"})
	public void testTutorLectureAlreadyExistsException() throws Exception{
		user.setIsTutor(true);
		user = userDao.save(user);
		
		this.mockMvc
			.perform(post("/user/add-lecture")
					.param("grade", "" +4)
					.param("lecture", "Potions"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("/pages/user/add-lecture.jsp"))
				.andExpect(model().attribute("exception_message", "You've already registered this lecture!"));
	}
}
