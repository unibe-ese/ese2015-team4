package ch.ututor.tests.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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
public class ProfileEditControllerTest {
	
	@Autowired
	private WebApplicationContext wac;
	@Autowired
	UserDao userDao;
	@Autowired
	LectureDao lectureDao;
	@Autowired
	TutorLectureDao tutorLectureDao;
	
	private MockMvc mockMvc;
	private User user;
	
	private TutorLecture tutorLecture;
	
	private void dataSetupTutor(User user){
		user.setPrice(19.95F);
		user.setDescription("Safety operations supervisor from the sector 7G of the Springfield Nuclear Power Plant");
		user.setIsTutor(true);
		user = userDao.save(user);
		
		lectureDao.deleteAll();
		tutorLectureDao.deleteAll();
	
		Lecture lecture = new Lecture();
		lecture.setName("Safety in nuclear power plants");
		lecture = lectureDao.save(lecture);
		
		tutorLecture = new TutorLecture();
		tutorLecture.setGrade(1);
		tutorLecture.setLecture(lecture);
		tutorLecture.setTutor(user);
		tutorLecture = tutorLectureDao.save(tutorLecture);
	}

	private void dataSetup() {
		userDao.deleteAll();
		
		user = new User();
		user.setFirstName("Carl");
		user.setLastName("Carlsson");
		user.setUsername("carl@carlsson.com");
		user = userDao.save(user);
		
	}
	
	@Before
	public void setup() {
		dataSetup();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	@WithMockUser (username="carl@carlsson.com", roles = {"USER"})
	public void testDisplayProfileEditForm() throws Exception{
		
		this.mockMvc
			.perform(get("/user/profile/edit"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/profile-edit.jsp"))
			.andExpect(model().attributeExists("profileEditForm"))
			.andExpect(model().attribute("isTutor", false));
	}
	
	@Test
	@WithMockUser (username="carl@carlsson.com", roles = {"USER"})
	public void testDisplayProfileEditFormToTutor() throws Exception{
		dataSetupTutor(user);
		
		this.mockMvc
			.perform(get("/user/profile/edit"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/profile-edit.jsp"))
			.andExpect(model().attribute("isTutor", true))
			.andExpect(model().attribute("profileEditForm", hasProperty("description", is("Safety operations supervisor from the sector 7G of the Springfield Nuclear Power Plant"))));
	}
	
	@Test
	@WithMockUser (username="carl@carlsson.com", roles = {"USER"})
	public void testValidProfileEditForm() throws Exception{
		
		this.mockMvc
			.perform(post("/user/profile/edit")
					.param("firstName", "Tony")
					.param("lastName", "Weak")
					.param("description", "Hi I'm Tony"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/user/profile?isTutor=false"))
				.andExpect(model().attribute("isTutor", "false"));
				
	}

	@Test
	@WithMockUser (username="carl@carlsson.com", roles = {"USER"})
	public void testValidProfileEditFormTutor() throws Exception{
		dataSetupTutor(user);
		
		this.mockMvc
		.perform(post("/user/profile/edit")
				.param("firstName", "Tony")
				.param("lastName", "Weak")
				.param("description", "Hi I'm Tony"))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/user/profile?isTutor=true"))
			.andExpect(model().attribute("isTutor", "true"));
	}

	
	@Test
	@WithMockUser (username="carl@carlsson.com", roles = {"USER"})
	public void testInvalidProfileEditForm() throws Exception{
		
		this.mockMvc
			.perform(post("/user/profile/edit")
					.param("firstName", "")
					.param("lastName", "")
					.param("description", ""))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("/pages/user/profile-edit.jsp"))
				.andExpect(model().attribute("isTutor", false));
			
	}
}
