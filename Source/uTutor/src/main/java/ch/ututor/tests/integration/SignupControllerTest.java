package ch.ututor.tests.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
"file:src/main/webapp/WEB-INF/config/springMVC.xml",
"file:src/main/webapp/WEB-INF/config/springData.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class SignupControllerTest {
	
	@Autowired private WebApplicationContext wac;
	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void testGetSignupForm() throws Exception{
		this.mockMvc.perform(
				get("/signup"))
					.andExpect(status().isOk())
					.andExpect(forwardedUrl("/pages/signup.jsp"))
					.andExpect(model().attributeExists("signUpForm"));
		
	}
	
	@Test
	public void testValidSignupForm() throws Exception{
		this.mockMvc.perform(
				post("/signup")
					.param("firstName", "Ron")
					.param("lastName", "Weasley")
					.param("email", "ron.weasley@hogwarts.com")
					.param("password", "12345678")
					.param("passwordRepeat", "12345678"))
				.andExpect(model().hasNoErrors())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/login?username=ron.weasley@hogwarts.com"));
	}
	
	@Test
	public void testErrorsSignupForm() throws Exception{
		this.mockMvc.perform(
				post("/signup")
					.param("firstName", "")
					.param("lastName", "")
					.param("email", "")
					.param("password", "")
					.param("passwordRepeat", ""))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("/pages/signup.jsp"))
				.andExpect(model().attributeHasFieldErrors("signUpForm", "firstName"))
				.andExpect(model().attributeHasFieldErrors("signUpForm", "lastName"))
				.andExpect(model().attributeHasFieldErrors("signUpForm", "email"))
				.andExpect(model().attributeHasFieldErrors("signUpForm", "password"));
	}
}

