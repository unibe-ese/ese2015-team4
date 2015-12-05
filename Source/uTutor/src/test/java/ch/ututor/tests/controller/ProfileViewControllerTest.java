package ch.ututor.tests.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

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

import ch.ututor.model.TimeSlot;
import ch.ututor.model.TutorLecture;
import ch.ututor.model.User;
import ch.ututor.service.interfaces.TimeSlotService;
import ch.ututor.service.interfaces.TutorService;
import ch.ututor.service.interfaces.UserService;

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
	@Autowired private UserService userService;
	@Autowired private TutorService tutorService;
	@Autowired private TimeSlotService timeSlotService;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	@WithMockUser(username="ginevra.weasley@hogwarts.com",roles={"USER"})
	public void testProfileViewOwn() throws Exception{
		User user = userService.load("ginevra.weasley@hogwarts.com");
		this.mockMvc.perform(get("/user/profile"))		
					.andExpect(status().isOk())
					.andExpect(forwardedUrl("/pages/user/profile.jsp"))
					.andExpect(model().attribute("ownProfile", true))
					.andExpect(model().attribute("user", user));
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com",roles={"USER"})
	public void testProfileViewOtherUserNotTutor() throws Exception{
		User otherUser = userService.load("ginevra.weasley@hogwarts.com");
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
		User otherUser = userService.load("percy.weasley@hogwarts.com");
		this.mockMvc.perform(get("/user/profile?userId="+otherUser.getId()))		
					.andExpect(status().isOk())
					.andExpect(forwardedUrl("/pages/user/profile.jsp"))
					.andExpect(model().attribute("ownProfile", false))
					.andExpect(model().attribute("user", otherUser))
					.andExpect(model().attributeExists("lectures"));
	}

	@Test
	@WithMockUser(username="fred.weasley@hogwarts.com",roles={"USER"})
	public void testProfileViewLastLectureDeleted() throws Exception{
		User user = userService.load("fred.weasley@hogwarts.com");
		List<TutorLecture> tutorLectures = tutorService.findLecturesByTutor(user);
		for( int i=0; i<tutorLectures.size(); i++ ){
			this.mockMvc.perform(post("/user/profile")
						.param("action", "deleteLecture")
						.param("objectId", tutorLectures.get(i).getId().toString()))
						.andExpect(status().is(302))
						.andExpect(redirectedUrl("/user/profile/?userId=1"));
		}
		
		this.mockMvc.perform(get("/user/profile"))		
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/profile.jsp"))
			.andExpect(model().attribute("ownProfile", true))
			.andExpect(model().attribute("user", user))
			.andExpect(model().attributeDoesNotExist("lectures"))
			.andExpect(model().attributeDoesNotExist("exeption_message"));
	}
	
	@Test
	@WithMockUser(username="fred.weasley@hogwarts.com",roles={"USER"})
	public void testSendTimeSlotRequest() throws Exception{
		User student = userService.load("fred.weasley@hogwarts.com");
		User tutor = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotService.getTimeSlotsByTutorAndState(tutor, TimeSlot.Status.AVAILABLE, false);
		postTimeSlotAction("requestTimeSlot", student.getId(), "Request successfully sent.", timeslots.get(0).getId()+"");
		checkProfile(6, tutor, TimeSlot.Status.REQUESTED, null);
	}
	
	@Test
	@WithMockUser(username="fred.weasley@hogwarts.com",roles={"USER"})
	public void testSendTimeSlotRequestNotSuccessfullState() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotService.getTimeSlotsByTutorAndState(user, TimeSlot.Status.REQUESTED, false);
		postTimeSlotActionNotSuccessfull("requestTimeSlot", timeslots.get(0).getId()+"", "The time slot you wanted to request is no longer available!");
	}
	
	@Test
	@WithMockUser(username="fred.weasley@hogwarts.com",roles={"USER"})
	public void testSendTimeSlotRequestNotSuccessfullPast() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotService.getTimeSlotsByTutorAndState(user, TimeSlot.Status.AVAILABLE, true);
		postTimeSlotActionNotSuccessfull("requestTimeSlot", timeslots.get(0).getId()+"", "You can not request a past time-slot!");
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com",roles={"USER"})
	public void testDeleteTimeSlot() throws Exception{
		User tutor = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotService.getTimeSlotsByTutorAndState(tutor, TimeSlot.Status.AVAILABLE, false);
		postTimeSlotAction("deleteTimeSlot", tutor.getId(), "Time-slot successfully deleted.", timeslots.get(0).getId()+"");
		checkProfile(5, null, null, null);
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com",roles={"USER"})
	public void testDeleteTimeSlotNotSuccessfull() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotService.getTimeSlotsByTutorAndState(user, TimeSlot.Status.REQUESTED, false);
		postTimeSlotActionNotSuccessfull("deleteTimeSlot", timeslots.get(0).getId()+"", "The time slot you wanted to delete is no longer in the AVAILABLE state.");
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com",roles={"USER"})
	public void testAcceptTimeSlot() throws Exception{
		User tutor = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotService.getTimeSlotsByTutorAndState(tutor, TimeSlot.Status.REQUESTED, false);
		postTimeSlotAction("acceptTimeSlot", tutor.getId(), "Time-slot accepted.", timeslots.get(0).getId()+"");
		checkProfile(6, tutor, TimeSlot.Status.ACCEPTED, null);
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com",roles={"USER"})
	public void testAcceptTimeSlotNotSuccessfullState() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotService.getTimeSlotsByTutorAndState(user, TimeSlot.Status.ACCEPTED, false);
		postTimeSlotActionNotSuccessfull("acceptTimeSlot", timeslots.get(0).getId()+"", "The time slot request you wanted to accept is no longer in the REQUESTED state.");
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com",roles={"USER"})
	public void testAcceptTimeSlotNotSuccessfullPast() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotService.getTimeSlotsByTutorAndState(user, TimeSlot.Status.REQUESTED, true);
		postTimeSlotActionNotSuccessfull("acceptTimeSlot", timeslots.get(0).getId()+"", "You can not accept a time slot request for a date in the past!");
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com",roles={"USER"})
	public void testRejectTimeSlot() throws Exception{
		User tutor = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotService.getTimeSlotsByTutorAndState(tutor, TimeSlot.Status.REQUESTED, false);
		postTimeSlotAction("rejectTimeSlot", tutor.getId(), "Time-slot rejected.", timeslots.get(0).getId()+"");
		checkProfile(6, tutor, TimeSlot.Status.ACCEPTED, null);
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com",roles={"USER"})
	public void testRejectTimeSlotNotSuccessfull() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotService.getTimeSlotsByTutorAndState(user, TimeSlot.Status.AVAILABLE, false);
		postTimeSlotActionNotSuccessfull("rejectTimeSlot", timeslots.get(0).getId()+"", "The time slot request you wanted to reject is no longer in the REQUESTED state.");
	}
	
	@Test
	@WithMockUser(username="ginevra.weasley@hogwarts.com",roles={"USER"})
	public void testRateTimeSlot() throws Exception{
		User tutor = userService.load("percy.weasley@hogwarts.com");
		User student = userService.load("ginevra.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotService.getTimeSlotsByTutorAndState(tutor, TimeSlot.Status.ACCEPTED, true);
		postTimeSlotAction("rateTimeSlot", student.getId(), "Time-slot rated.", timeslots.get(0).getId()+"-1");
		postTimeSlotAction("rateTimeSlot", student.getId(), "Time-slot rated.", timeslots.get(0).getId()+"-5");		
		checkProfile(2, tutor, TimeSlot.Status.ACCEPTED, 5);
	}
	
	@Test
	@WithMockUser(username="ginevra.weasley@hogwarts.com",roles={"USER"})
	public void testRateTimeSlotNotSuccessfullFuture() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotService.getTimeSlotsByTutorAndState(user, TimeSlot.Status.ACCEPTED, false);
		postTimeSlotActionNotSuccessfull("rateTimeSlot", timeslots.get(0).getId()+"-1", "You can not rate a time-slot with a date in the future!");
	}
	
	@Test
	@WithMockUser(username="ginevra.weasley@hogwarts.com",roles={"USER"})
	public void testRateTimeSlotNotSuccessfullInvalidRating() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotService.getTimeSlotsByTutorAndState(user, TimeSlot.Status.ACCEPTED, true);
		postTimeSlotActionNotSuccessfull("rateTimeSlot", timeslots.get(0).getId()+"-0", "Invalid rating. The value must be between 1 and 5!");
		postTimeSlotActionNotSuccessfull("rateTimeSlot", timeslots.get(0).getId()+"-6", "Invalid rating. The value must be between 1 and 5!");
	}
	
	private void postTimeSlotAction(String action, Long userId, String flashMessage, String timeSlotId) throws Exception{
		this.mockMvc.perform(post("/user/profile")
				.param("action", action)
				.param("objectId", timeSlotId))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/user/profile/?userId="+userId))
				.andExpect(flash().attribute("flash_message", flashMessage));
	}
	
	private void postTimeSlotActionNotSuccessfull(String action, String timeSlotId, String exceptionMessage) throws Exception{
		this.mockMvc.perform(post("/user/profile")
				.param("action", action)
				.param("objectId", timeSlotId ))
				.andExpect(status().isOk())
				.andExpect(model().attribute("exception_message", exceptionMessage));
	}
	
	private void checkProfile(Integer size, User tutor, TimeSlot.Status status, Integer rating) throws Exception{
		this.mockMvc.perform(get("/user/profile"))	
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/profile.jsp"))
			.andExpect(model().attribute("ownProfile", true))
			.andExpect(model().attributeExists("timeSlotList"))
			.andExpect(model().attribute("timeSlotList", hasSize( size )));
		if(tutor != null){
			this.mockMvc.perform(get("/user/profile"))
				.andExpect(model().attribute("timeSlotList", hasItem(hasProperty("tutor", is( tutor )))));
		}
		if(status != null){
			this.mockMvc.perform(get("/user/profile"))
			.andExpect(model().attribute("timeSlotList", hasItem(hasProperty("status", is( status )))));
		}
		if(rating != null){
			this.mockMvc.perform(get("/user/profile"))
				.andExpect(model().attribute("timeSlotList", hasItem(hasProperty("rating", is( rating )))));
		}
			
	}
}