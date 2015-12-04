package ch.ututor.tests.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.ArrayList;
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

	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	@WithMockUser(username="ginevra.weasley@hogwarts.com", roles={"USER"})
	public void testDisplayBecomeTutorForm() throws Exception{
		
		this.mockMvc
			.perform(get("/user/become-tutor"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/become-tutor.jsp"))
			.andExpect(model().attributeExists("becomeTutorForm"));
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com", roles={"USER"})
	public void testGetIsAlreadyTutor() throws Exception{		
		this.mockMvc
			.perform(get("/user/become-tutor"))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/user/profile"));
	
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com", roles={"USER"})
	public void testPostIsAlreadyTutor() throws Exception{
		this.mockMvc
			.perform(post("/user/become-tutor"))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/user/profile"));
	}
	
	@Test
	@WithMockUser(username="ginevra.weasley@hogwarts.com", roles={"USER"})
	public void testInvalidBecomeTutorForm() throws Exception{
		
		this.mockMvc
			.perform(post("/user/become-tutor"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/become-tutor.jsp"))
			.andExpect(model().attributeHasFieldErrors("becomeTutorForm", "price", "description", "lecture"));
	}
	
	@Test
	@WithMockUser(username="ginevra.weasley@hogwarts.com", roles={"USER"})
	public void testValidBecomeTutorForm() throws Exception{
		
		this.mockMvc
			.perform(post("/user/become-tutor")
					.param("grade", "4")
					.param("lecture", "Transformation")
					.param("price", "50")
					.param("description", "Hi I'm Ginny"))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/user/profile"));
	}
	
	@Test
	@WithMockUser(username="ginevra.weasley@hogwarts.com", roles={"USER"})
	public void testInvalidPriceExceptionNoFloat() throws Exception{
		
		this.mockMvc
			.perform(post("/user/become-tutor")
					.param("grade", "4")
					.param("lecture", "Defense against the dark arts")
					.param("price", "hello")
					.param("description", "Hi I'm Ginny"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/become-tutor.jsp"))
			.andExpect(model().attribute("exception_message", "Please enter a valid price!"));
	}
	
	@Test
	@WithMockUser(username="ginevra.weasley@hogwarts.com", roles={"USER"})
	public void testInvalidPriceExceptionLessThanZero() throws Exception{
		
		this.mockMvc
			.perform(post("/user/become-tutor")
					.param("grade", "4")
					.param("lecture", "Defense against the dark arts")
					.param("price", "-5")
					.param("description", "Hi I'm Hermione"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/become-tutor.jsp"))
			.andExpect(model().attribute("exception_message", "Please enter a price greater than zero!"));
	}
	
	@Test
	@WithMockUser(username="ginevra.weasley@hogwarts.com", roles={"USER"})
	public void testGetAddLecturesIsNoTutor() throws Exception{
		
		this.mockMvc
			.perform(get("/user/add-lecture"))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/user/become-tutor"));
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com", roles={"USER"})
	public void testValidAddLecture() throws Exception{		
		this.mockMvc
			.perform(get("/user/add-lecture"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/add-lecture.jsp"))
			.andExpect(model().attributeExists("addLectureForm"));
	}
	
	@Test
	@WithMockUser(username="ginevra.weasley@hogwarts.com", roles={"USER"})
	public void testPostAddLecturesIsNoTutor() throws Exception{
		
		this.mockMvc
			.perform(post("/user/add-lecture"))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/user/become-tutor"));
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com", roles={"USER"})
	public void testInvalidAddLectureForm() throws Exception{
		this.mockMvc
			.perform(post("/user/add-lecture"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrors("addLectureForm", "lecture"))
			.andExpect(forwardedUrl("/pages/user/add-lecture.jsp"));
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com", roles={"USER"})
	public void testValidAddLectureForm() throws Exception{
		this.mockMvc
			.perform(post("/user/add-lecture")
					.param("grade", "4")
					.param("lecture", "Charms"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/user/profile"));
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com", roles={"USER"})
	public void testTutorLectureAlreadyExistsException() throws Exception{
		this.mockMvc
			.perform(post("/user/add-lecture")
					.param("grade", "4")
					.param("lecture", "Apparition"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("/pages/user/add-lecture.jsp"))
				.andExpect(model().attribute("exception_message", "You've already registered this lecture!"));
	}
	
	@Test
	@WithMockUser(username="ginevra.weasley@hogwarts.com", roles={"USER"})
	public void testGetAddTimeSlotsIsNoTutor() throws Exception{
		this.mockMvc
			.perform(get("/user/add-timeslots"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/user/become-tutor"));
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com", roles={"USER"})
	public void testTutorAddTimeSlots() throws Exception{
		this.mockMvc
			.perform(get("/user/add-timeslots"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("/pages/user/add-timeslots.jsp"))
				.andExpect(model().attributeExists("addTimeslotsForm"))
				.andExpect(model().attribute("possibleTimeslots", getPossibleTimeslots()));
	}

	private List<String> getPossibleTimeslots() {
		List<String> possibleTimeslots = new ArrayList<String>();
		for(Integer i = 6; i < 23; i++){
			String hour = i.toString();
			
			if(i < 10){
				hour = "0" + i;
			}
			possibleTimeslots.add(hour + ":00 - " + hour + ":59");
		}
		return possibleTimeslots;
	}
	
	@Test
	@WithMockUser(username="ginevra.weasley@hogwarts.com", roles={"USER"})
	public void testPostAddTimeSlotsIsNoTutor() throws Exception{
		this.mockMvc
			.perform(post("/user/add-timeslots"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/user/become-tutor"));
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com", roles={"USER"})
	public void testValidAddTimeSlot() throws Exception{
		this.mockMvc
			.perform(post("/user/add-timeslots")
					.param("date", "2015-12-24")
					.param("timeslots", "08:00 - 08:59")
					.param("timeslots", "09:00 - 09:59"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/user/profile"));
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com", roles={"USER"})
	public void testInvalidTimeslotsParseException() throws Exception{
		this.mockMvc
		.perform(post("/user/add-timeslots")
				.param("date", "2015-12-24")
				.param("timeslots", "No Timeslot"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/exception.jsp"))
			.andExpect(model().attribute("exception_message", "Unparseable date: \"2015-12-24 No Ti\""));
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com", roles={"USER"})
	public void testInvalidDateCustomException() throws Exception{
		this.mockMvc
			.perform(post("/user/add-timeslots")
					.param("date", "2014-12-24")
					.param("timeslots", "08:00 - 08:59"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("/pages/user/add-timeslots.jsp"))
				.andExpect(model().attribute("exception_message", "Please enter a future date!"));
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com", roles={"USER"})
	public void testInvalidAddTimeslotsForm() throws Exception{
		this.mockMvc
		.perform(post("/user/add-timeslots"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/add-timeslots.jsp"))
			.andExpect(model().attribute("possibleTimeslots", getPossibleTimeslots()));
	}
}