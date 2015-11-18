package ch.ututor.tests.controller;

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
	
	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	@WithMockUser (username="ginevra.weasley@hogwarts.com", roles = {"USER"})
	public void testDisplayProfileEditFormStudent() throws Exception{
		
		this.mockMvc
			.perform(get("/user/profile/edit"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/profile-edit.jsp"))
			.andExpect(model().attributeExists("profileEditForm"))
			.andExpect(model().attribute("isTutor", false));
	}

	@Test
	@WithMockUser (username="percy.weasley@hogwarts.com", roles = {"USER"})
	public void testDisplayProfileEditFormToTutor() throws Exception{
		this.mockMvc
			.perform(get("/user/profile/edit"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/profile-edit.jsp"))
			.andExpect(model().attribute("isTutor", true))
			.andExpect(model().attribute("profileEditForm", hasProperty("description", is("Percy Ignatius Weasley (b. 22 August, 1976) was a pure-bloodwizard, the third child of Arthur and Molly Weasley (née Prewett)."))));
	}
	
	@Test
	@WithMockUser (username="ginevra.weasley@hogwarts.com", roles = {"USER"})
	public void testValidProfileEditFormStudent() throws Exception{
		this.mockMvc
			.perform(post("/user/profile/edit")
					.param("firstName", "Tony")
					.param("lastName", "Weak")
					.param("description", "-"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/user/profile?isTutor=false"))
				.andExpect(model().attribute("isTutor", "false"));
				
	}

	@Test
	@WithMockUser (username="percy.weasley@hogwarts.com", roles = {"USER"})
	public void testValidProfileEditFormTutor() throws Exception{
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
	@WithMockUser (username="ginevra.weasley@hogwarts.com", roles = {"USER"})
	public void testInvalidProfileEditFormStudent() throws Exception{		
		this.mockMvc
			.perform(post("/user/profile/edit")
					.param("firstName", "")
					.param("lastName", ""))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("/pages/user/profile-edit.jsp"))
				.andExpect(model().attribute("isTutor", false))
				.andExpect(model().attributeHasFieldErrors("profileEditForm", "firstName", "lastName"));
	}
	
	@Test
	@WithMockUser (username="percy.weasley@hogwarts.com", roles = {"USER"})
	public void testInvalidProfileEditFormTutor() throws Exception{		
		this.mockMvc
			.perform(post("/user/profile/edit")
					.param("firstName", "")
					.param("lastName", "")
					.param("description", ""))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("/pages/user/profile-edit.jsp"))
				.andExpect(model().attribute("isTutor", true))
				.andExpect(model().attributeHasFieldErrors("profileEditForm", "firstName", "lastName", "description"));
	}
}
