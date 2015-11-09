package ch.ututor.tests.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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

import ch.ututor.model.User;
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
public class ProfilePictureEditControllerTest {
	@Autowired
	private WebApplicationContext wac;
	@Autowired
	UserDao userDao;
	
	private MockMvc mockMvc;
	private User user;
	
	private void dataSetup(){
		userDao.deleteAll();
		
		user = new User();
		user.setFirstName("Lenny");
		user.setLastName("Lenford");
		user.setUsername("lenny.lenford@simpsons.com");
		user = userDao.save(user);
	}
	
	@Before
	public void setup() {
		dataSetup();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	@WithMockUser(username = "lenny.lenford@simpsons.com", roles = { "USER" })
	public void testValidProfilePictureEdit() throws Exception {
		this.mockMvc.perform(get("/user/profile/picture"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("userId", user.getId()))
				.andExpect(model().attribute("hasProfilePic", false))
				.andExpect(forwardedUrl("/pages/user/profile-picture.jsp"));
	}
}
