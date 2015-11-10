package ch.ututor.tests.integration;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
	@Autowired private UserDao userDao;
	@Autowired private TutorLectureDao tutorLectureDao;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	@WithMockUser(username="ginevra.weasley@hogwarts.com",roles={"USER"})
	public void testProfileViewOwn() throws Exception{
		User user = userDao.findByUsername("ginevra.weasley@hogwarts.com");
		this.mockMvc.perform(get("/user/profile"))		
					.andExpect(status().isOk())
					.andExpect(forwardedUrl("/pages/user/profile.jsp"))
					.andExpect(model().attribute("ownProfile", true))
					.andExpect(model().attribute("user", user));
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com",roles={"USER"})
	public void testProfileViewOtherUserNotTutor() throws Exception{
		User otherUser = userDao.findByUsername("ginevra.weasley@hogwarts.com");
		this.mockMvc.perform(get("/user/profile?userId="+otherUser.getId()))		
					.andExpect(status().isOk())
					.andExpect(forwardedUrl("/pages/user/profile.jsp"))
					.andExpect(model().attribute("ownProfile", false))
					.andExpect(model().attribute("user", otherUser))
					.andExpect(model().attributeDoesNotExist("lectures"));
	}
	
	@Test
	@WithMockUser(username="ginevra.weasley@hogwarts.com",roles={"USER"})
	public void testProfileViewOtherUserUserNotFoundException() throws Exception{
		this.mockMvc.perform(get("/user/profile?userId=-1"))		
					.andExpect(status().isOk())
					.andExpect(forwardedUrl("/pages/exception.jsp"))
					.andExpect(model().attribute("exception_message", "User not found."));
	}
	
	@Test
	@WithMockUser(username="ginevra.weasley@hogwarts.com",roles={"USER"})
	public void testProfileViewOtherUserTutor() throws Exception{
		User otherUser = userDao.findByUsername("percy.weasley@hogwarts.com");
		this.mockMvc.perform(get("/user/profile?userId="+otherUser.getId()))		
					.andExpect(status().isOk())
					.andExpect(forwardedUrl("/pages/user/profile.jsp"))
					.andExpect(model().attribute("ownProfile", false))
					.andExpect(model().attribute("user", otherUser))
					.andExpect(model().attributeExists("lectures"));
	}

	@Test
	@WithMockUser(username="fred.weasley@hogwarts.com",roles={"USER"})
	public void testProfileViewLastLecture() throws Exception{
		User user = userDao.findByUsername("fred.weasley@hogwarts.com");
		List<TutorLecture> tutorLectures = tutorLectureDao.findByTutor(user);
		this.mockMvc.perform(post("/user/profile")
					.param("action", "deleteLecture")
					.param("objectId", tutorLectures.get(0).getId().toString()))
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