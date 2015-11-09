package ch.ututor.tests.integration;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@Transactional
@Rollback

public class ProfileViewControllerTest {
	@Autowired private WebApplicationContext wac;
	@Autowired UserDao userDao;
	@Autowired LectureDao lectureDao;
	@Autowired TutorLectureDao tutorLectureDao;
	
	private MockMvc mockMvc;
	private User user;
	private User otherUser;
	private TutorLecture tutorLecture;
	
	private void dataSetupTutor(User user){
		user.setPrice(19.95F);
		user.setDescription("Safety operations supervisor from the sector 7G of the Springfield Nuclear Power Plant");
		user.setIsTutor(true);
		user = userDao.save(otherUser);
		
		lectureDao.deleteAll();
		tutorLectureDao.deleteAll();
	
		Lecture lecture = new Lecture();
		lecture.setName("Safety in nuclear power plants");
		lecture = lectureDao.save(lecture);
		
		tutorLecture = new TutorLecture();
		tutorLecture.setGrade(1);
		tutorLecture.setLecture(lecture);
		tutorLecture.setTutor(otherUser);
		tutorLecture = tutorLectureDao.save(tutorLecture);
	}
	
	private void dataSetup(){
		userDao.deleteAll();
		
		user = new User();
		user.setFirstName("Lenny");
		user.setLastName("Lenford");
		user.setUsername("lenny.lenford@simpsons.com");
		user.setPassword("springfield");
		user = userDao.save(user);
		
		otherUser = new User();
		otherUser.setFirstName("Carl");
		otherUser.setLastName("Carlson");
		otherUser.setUsername("carl.carlson@simpsons.com");
		otherUser.setPrice(19.95F);
		otherUser.setDescription("Safety operations supervisor from the sector 7G of the Springfield Nuclear Power Plant");
		otherUser.setIsTutor(true);
		otherUser = userDao.save(otherUser);
	}
	
	@Before
	public void setup() {
		dataSetup();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	@WithMockUser(username="lenny.lenford@simpsons.com",roles={"USER"})
	public void testProfileViewOwn() throws Exception{
		this.mockMvc.perform(get("/user/profile"))		
					.andExpect(status().isOk())
					.andExpect(forwardedUrl("/pages/user/profile.jsp"))
					.andExpect(model().attribute("ownProfile", true))
					.andExpect(model().attribute("user", user));
	}
	
	@Test
	@WithMockUser(username="lenny.lenford@simpsons.com",roles={"USER"})
	public void testProfileViewOtherUserNotTutor() throws Exception{
		this.mockMvc.perform(get("/user/profile?userId="+otherUser.getId()))		
					.andExpect(status().isOk())
					.andExpect(forwardedUrl("/pages/user/profile.jsp"))
					.andExpect(model().attribute("ownProfile", false))
					.andExpect(model().attribute("user", otherUser))
					.andExpect(model().attributeDoesNotExist("lectures"));
	}
	
	@Test
	@WithMockUser(username="lenny.lenford@simpsons.com",roles={"USER"})
	public void testProfileViewOtherUserUserNotFoundException() throws Exception{
		this.mockMvc.perform(get("/user/profile?userId="+(otherUser.getId()+1)))		
					.andExpect(status().isOk())
					.andExpect(forwardedUrl("/pages/exception.jsp"))
					.andExpect(model().attribute("exception_message", "User not found."));
	}
	
	@Test
	@WithMockUser(username="lenny.lenford@simpsons.com",roles={"USER"})
	public void testProfileViewOtherUserTutor() throws Exception{
		dataSetupTutor(otherUser);
		this.mockMvc.perform(get("/user/profile?userId="+otherUser.getId()))		
					.andExpect(status().isOk())
					.andExpect(forwardedUrl("/pages/user/profile.jsp"))
					.andExpect(model().attribute("ownProfile", false))
					.andExpect(model().attribute("user", otherUser))
					.andExpect(model().attributeExists("lectures"));
	}
	
	@Test
	@WithMockUser(username="lenny.lenford@simpsons.com",roles={"USER"})
	public void testProfileViewOtherUserTutorNoLecturesFoundException() throws Exception{
		otherUser.setIsTutor(true);
		userDao.save(otherUser);
		this.mockMvc.perform(get("/user/profile?userId="+otherUser.getId()))		
					.andExpect(status().isOk())
					.andExpect(forwardedUrl("/pages/user/profile.jsp"))
					.andExpect(model().attribute("ownProfile", false))
					.andExpect(model().attribute("user", otherUser))
					.andExpect(model().attributeDoesNotExist("lectures"))
					.andExpect(model().attribute("exception_message", "No lectures found for this tutor!"));
	}

	@Test
	@WithMockUser(username="lenny.lenford@simpsons.com",roles={"USER"})
	public void testProfileViewOtherUserTutorDeleteLecture() throws Exception{
		dataSetupTutor(user);
		assertTrue(user.getIsTutor());
		
		this.mockMvc.perform(post("/user/profile")
					.param("action", "deleteLecture")
					.param("objectId", tutorLecture.getId().toString()))
					.andExpect(status().is(302))
					.andExpect(redirectedUrl("/user/profile"));
		
		this.mockMvc.perform(get("/user/profile"))		
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/profile.jsp"))
			.andExpect(model().attribute("ownProfile", true))
			.andExpect(model().attribute("user", user))
			.andExpect(model().attributeDoesNotExist("lectures"))
			.andExpect(model().attributeDoesNotExist("exeption_message"));
	}
}