package ch.ututor.tests.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.*;

import java.util.Date;
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
import ch.ututor.model.dao.TimeSlotDao;
import ch.ututor.service.interfaces.AuthenticatedUserService;
import ch.ututor.service.interfaces.TutorService;
import ch.ututor.service.interfaces.UserService;
import ch.ututor.utils.TimeHelper;

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
	@Autowired private TimeSlotDao timeSlotDao;
	
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
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotDao.findByTutorAndStatusOrderByBeginDateTimeDesc(user, TimeSlot.Status.AVAILABLE);
		this.mockMvc.perform(post("/user/profile")
					.param("action", "requestTimeSlot")
					.param("objectId", "" + timeslots.get(0).getId()))
					.andExpect(status().isFound())
					.andExpect(redirectedUrl("/user/profile/?userId=1"))
					.andExpect(flash().attribute("flash_message", "Request successfully sent."));
		
		this.mockMvc.perform(get("/user/profile"))		
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/profile.jsp"))
			.andExpect(model().attribute("ownProfile", true))
			.andExpect(model().attribute("timeSlotList", hasItem(hasProperty("tutor", is(user)))))
			.andExpect(model().attribute("timeSlotList", hasItem(hasProperty("status", is(TimeSlot.Status.REQUESTED)))));
	}
	
	@Test
	@WithMockUser(username="fred.weasley@hogwarts.com",roles={"USER"})
	public void testSendTimeSlotRequestNotSuccessfullState() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotDao.findByTutorAndStatusOrderByBeginDateTimeDesc(user, TimeSlot.Status.REQUESTED);
		this.mockMvc.perform(post("/user/profile")
					.param("action", "requestTimeSlot")
					.param("objectId", "" + timeslots.get(0).getId()))
					.andExpect(status().isOk())
					.andExpect(model().attributeExists("exception_message"));
	}
	
	@Test
	@WithMockUser(username="fred.weasley@hogwarts.com",roles={"USER"})
	public void testSendTimeSlotRequestNotSuccessfullPast() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotDao.findByTutorAndStatusOrderByBeginDateTimeAsc(user, TimeSlot.Status.AVAILABLE);
		this.mockMvc.perform(post("/user/profile")
					.param("action", "requestTimeSlot")
					.param("objectId", "" + timeslots.get(0).getId()))
					.andExpect(status().isOk())
					.andExpect(model().attributeExists("exception_message"));
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com",roles={"USER"})
	public void testDeleteTimeSlot() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotDao.findByTutorAndStatusOrderByBeginDateTimeDesc(user, TimeSlot.Status.AVAILABLE);
		this.mockMvc.perform(post("/user/profile")
					.param("action", "deleteTimeSlot")
					.param("objectId", "" + timeslots.get(0).getId()))
					.andExpect(status().isFound())
					.andExpect(redirectedUrl("/user/profile/?userId=4"))
					.andExpect(flash().attribute("flash_message", "Time-slot successfully deleted."));
		
		this.mockMvc.perform(get("/user/profile"))		
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/profile.jsp"))
			.andExpect(model().attribute("ownProfile", true))
			.andExpect(model().attributeExists("timeSlotList"))
			.andExpect(model().attribute("timeSlotList", hasSize(5)));
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com",roles={"USER"})
	public void testDeleteTimeSlotNotSuccessfull() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotDao.findByTutorAndStatusOrderByBeginDateTimeDesc(user, TimeSlot.Status.REQUESTED);
		this.mockMvc.perform(post("/user/profile")
					.param("action", "deleteTimeSlot")
					.param("objectId", "" + timeslots.get(0).getId()))
					.andExpect(status().isOk())
					.andExpect(model().attributeExists("exception_message"));
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com",roles={"USER"})
	public void testAcceptTimeSlot() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotDao.findByTutorAndStatusOrderByBeginDateTimeDesc(user, TimeSlot.Status.REQUESTED);
		this.mockMvc.perform(post("/user/profile")
					.param("action", "acceptTimeSlot")
					.param("objectId", "" + timeslots.get(0).getId()))
					.andExpect(status().isFound())
					.andExpect(redirectedUrl("/user/profile/?userId=4"))
					.andExpect(flash().attribute("flash_message", "Time-slot accepted."));
		
		this.mockMvc.perform(get("/user/profile"))		
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/profile.jsp"))
			.andExpect(model().attribute("ownProfile", true))
			.andExpect(model().attributeExists("timeSlotList"))
			.andExpect(model().attribute("timeSlotList", hasSize(6)))
			.andExpect(model().attribute("timeSlotList", hasItem(hasProperty("tutor", is(user)))))
			.andExpect(model().attribute("timeSlotList", hasItem(hasProperty("status", is(TimeSlot.Status.ACCEPTED)))));
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com",roles={"USER"})
	public void testAcceptTimeSlotNotSuccessfullState() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotDao.findByTutorAndStatusOrderByBeginDateTimeDesc(user, TimeSlot.Status.ACCEPTED);
		this.mockMvc.perform(post("/user/profile")
					.param("action", "acceptTimeSlot")
					.param("objectId", "" + timeslots.get(0).getId()))
					.andExpect(status().isOk())
					.andExpect(model().attributeExists("exception_message"));
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com",roles={"USER"})
	public void testAcceptTimeSlotNotSuccessfullPast() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotDao.findByTutorAndStatusOrderByBeginDateTimeAsc(user, TimeSlot.Status.REQUESTED);
		this.mockMvc.perform(post("/user/profile")
					.param("action", "acceptTimeSlot")
					.param("objectId", "" + timeslots.get(0).getId()))
					.andExpect(status().isOk())
					.andExpect(model().attributeExists("exception_message"));
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com",roles={"USER"})
	public void testRejectTimeSlot() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotDao.findByTutorAndStatusOrderByBeginDateTimeDesc(user, TimeSlot.Status.REQUESTED);
		this.mockMvc.perform(post("/user/profile")
					.param("action", "rejectTimeSlot")
					.param("objectId", "" + timeslots.get(0).getId() ))
					.andExpect(status().isFound())
					.andExpect(redirectedUrl("/user/profile/?userId=4"))
					.andExpect(flash().attribute("flash_message", "Time-slot rejected."));
		
		this.mockMvc.perform(get("/user/profile"))		
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/profile.jsp"))
			.andExpect(model().attribute("ownProfile", true))
			.andExpect(model().attributeExists("timeSlotList"))
			.andExpect(model().attribute("timeSlotList", hasSize(6)))
			.andExpect(model().attribute("timeSlotList", hasItem(hasProperty("tutor", is(user)))))
			.andExpect(model().attribute("timeSlotList", hasItem(hasProperty("status", is(TimeSlot.Status.AVAILABLE)))));
	}
	
	@Test
	@WithMockUser(username="percy.weasley@hogwarts.com",roles={"USER"})
	public void testRejectTimeSlotNotSuccessfull() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotDao.findByTutorAndStatusOrderByBeginDateTimeDesc(user, TimeSlot.Status.AVAILABLE);
		this.mockMvc.perform(post("/user/profile")
					.param("action", "rejectTimeSlot")
					.param("objectId", "" + timeslots.get(0).getId() ))
					.andExpect(status().isOk())
					.andExpect(model().attributeExists("exception_message"));
	}
	
	@Test
	@WithMockUser(username="ginevra.weasley@hogwarts.com",roles={"USER"})
	public void testRateTimeSlot() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotDao.findByTutorAndStatusOrderByBeginDateTimeAsc(user, TimeSlot.Status.ACCEPTED);
		this.mockMvc.perform(post("/user/profile")
				.param("action", "rateTimeSlot")
				.param("objectId", "" + timeslots.get(0).getId() +"-1" ))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/user/profile/?userId=3"))
				.andExpect(flash().attribute("flash_message", "Time-slot rated."));
		
		this.mockMvc.perform(post("/user/profile")
					.param("action", "rateTimeSlot")
					.param("objectId", "" + timeslots.get(0).getId() +"-5" ))
					.andExpect(status().isFound())
					.andExpect(redirectedUrl("/user/profile/?userId=3"))
					.andExpect(flash().attribute("flash_message", "Time-slot rated."));
		
		this.mockMvc.perform(get("/user/profile"))		
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/pages/user/profile.jsp"))
			.andExpect(model().attribute("ownProfile", true))
			.andExpect(model().attributeExists("timeSlotList"))
			.andExpect(model().attribute("timeSlotList", hasSize(2)))
			.andExpect(model().attribute("timeSlotList", hasItem(hasProperty("tutor", is(user)))))
			.andExpect(model().attribute("timeSlotList", hasItem(hasProperty("status", is(TimeSlot.Status.ACCEPTED)))))
			.andExpect(model().attribute("timeSlotList", hasItem(hasProperty("rating", is(5)))));
	}
	
	@Test
	@WithMockUser(username="ginevra.weasley@hogwarts.com",roles={"USER"})
	public void testRateTimeSlotNotSuccessfullFuture() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotDao.findByTutorAndStatusOrderByBeginDateTimeDesc(user, TimeSlot.Status.ACCEPTED);
		this.mockMvc.perform(post("/user/profile")
					.param("action", "rateTimeSlot")
					.param("objectId", "" + timeslots.get(0).getId() +"-1" ))
					.andExpect(status().isOk())
					.andExpect(model().attributeExists("exception_message"));
	}
	
	@Test
	@WithMockUser(username="ginevra.weasley@hogwarts.com",roles={"USER"})
	public void testRateTimeSlotNotSuccessfullInvalidRating() throws Exception{
		User user = userService.load("percy.weasley@hogwarts.com");
		List<TimeSlot> timeslots = timeSlotDao.findByTutorAndStatusOrderByBeginDateTimeAsc(user, TimeSlot.Status.ACCEPTED);
		this.mockMvc.perform(post("/user/profile")
					.param("action", "rateTimeSlot")
					.param("objectId", "" + timeslots.get(0).getId() +"-0" ))
					.andExpect(status().isOk())
					.andExpect(model().attributeExists("exception_message"));
		this.mockMvc.perform(post("/user/profile")
				.param("action", "rateTimeSlot")
				.param("objectId", "" + timeslots.get(0).getId() +"-6" ))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("exception_message"));
	}
}