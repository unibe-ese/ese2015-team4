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
public class PasswordChangeControllerTest {
	
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
		user.setPassword("springfield");
		user = userDao.save(user);
	}
	
	@Before
	public void setup() {
		dataSetup();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	@WithMockUser(username="lenny.lenford@simpsons.com", roles={"USER"})
	public void testChangePasswordPage() throws Exception{
		
		this.mockMvc.perform(get("/user/password"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/change-password.jsp"))
			.andExpect(model().attributeExists("changePasswordForm"));
	}
	
	@Test
	@WithMockUser(username="lenny.lenford@simpsons.com", roles={"USER"})
	public void testValidChangePassword() throws Exception{
		
		this.mockMvc.perform(post("/user/password")
					.param("oldPassword", "springfield")
					.param("newPassword", "KrustyTheClown")
					.param("newPasswordRepeat", "KrustyTheClown"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/user/profile"));
	}

	@Test
	@WithMockUser(username="lenny.lenford@simpsons.com", roles={"USER"})
	public void testInvalidChangePasswordForm() throws Exception{
		
		this.mockMvc
			.perform(post("/user/password")
					.param("oldPassword", "")
					.param("newPassword", ""))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/change-password.jsp"))
			.andExpect(model().attributeHasFieldErrors("changePasswordForm", "oldPassword"))
			.andExpect(model().attributeHasFieldErrors("changePasswordForm", "newPassword"));
	}
	
	@Test
	@WithMockUser(username="lenny.lenford@simpsons.com", roles={"USER"})
	public void testPasswordNotCorrectException() throws Exception{
		
		this.mockMvc
			.perform(post("/user/password")
					.param("oldPassword", "not my password"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/change-password.jsp"))
			.andExpect(model().attribute("exception_message", "Entered password doesn't match your actual password."));
	}
	
	@Test
	@WithMockUser(username="lenny.lenford@simpsons.com", roles={"USER"})
	public void testPasswordRepetitionException() throws Exception{
		
		this.mockMvc
			.perform(post("/user/password")
					.param("oldPassword", "springfield")
					.param("newPassword", "KrustyTheClown")
					.param("newPasswordRepeat", "KrustyClown"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("/pages/user/change-password.jsp"))
				.andExpect(model().attribute("exception_message", "The password repetition did not match."));
	}
}