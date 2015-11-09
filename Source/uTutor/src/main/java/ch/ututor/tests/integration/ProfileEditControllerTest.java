package ch.ututor.tests.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

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

import ch.ututor.model.Message;
import ch.ututor.model.User;
import ch.ututor.model.dao.MessageDao;
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
	private MockMvc mockMvc;
	private User user;
	

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
		user.setIsTutor(true);
		user.setDescription("Hi I'm Carl Carlsson");
		user = userDao.save(user);
		
		this.mockMvc
			.perform(get("/user/profile/edit"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/profile-edit.jsp"))
			.andExpect(model().attribute("isTutor", true))
			.andExpect(model().attribute("description", "Hi I'm Carl Carlsson"));
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
