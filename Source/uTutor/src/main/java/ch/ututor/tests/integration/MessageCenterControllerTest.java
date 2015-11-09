package ch.ututor.tests.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasProperty;

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
public class MessageCenterControllerTest {

	@Autowired
	private WebApplicationContext wac;
	@Autowired
	UserDao userDao;
	@Autowired
	MessageDao messageDao;
	private MockMvc mockMvc;
	private Message message;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		message = new Message();
		message.setReceiver(userDao.findByUsername("test@user.ch"));
		message.setSender(userDao.findByUsername("test@user.ch"));
	}

	@Test
	@WithMockUser(username = "test@user.ch", roles = { "USER" })
	public void testValidMessageCenter() throws Exception {

		this.mockMvc.perform(get("/user/messagecenter"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("messageList"))
				.andExpect(forwardedUrl("/pages/user/messagecenter.jsp"));
	}

	@Test
	@WithMockUser(username = "test@user.ch", roles = { "USER" })
	public void testValidOutboxMessageCenter() throws Exception {

		this.mockMvc.perform(get("/user/messagecenter?view=outbox"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("messageList"))
				.andExpect(forwardedUrl("/pages/user/messagecenter.jsp"));
	}

	@Test
	@WithMockUser(username = "test@user.ch", roles = { "USER" })
	public void testValidTrashMessageCenter() throws Exception {

		this.mockMvc.perform(get("/user/messagecenter?view=trash"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("messageList"))
				.andExpect(forwardedUrl("/pages/user/messagecenter.jsp"));
	}

	@Test
	@WithMockUser(username = "test@user.ch", roles = { "USER" })
	public void testDeleteMessageStandardRedirect() throws Exception {

		this.mockMvc
				.perform(
						post("/user/messagecenter?action=delete&objectId="
								+ message.getId()))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/user/messagecenter/?view=inbox&show=0"));
	}

	@Test
	@WithMockUser(username = "test@user.ch", roles = { "USER" })
	public void testDeleteMessageRedirectToOutbox() throws Exception {

		this.mockMvc
				.perform(
						post("/user/messagecenter?action=delete&objectId="
								+ message.getId() + "&view=outbox"))
				.andExpect(status().isFound())
				.andExpect(
						redirectedUrl("/user/messagecenter/?view=outbox&show=0"));
	}

	@Test
	@WithMockUser(username = "test@user.ch", roles = { "USER" })
	public void testDeleteMessageRedirectToTrash() throws Exception {

		this.mockMvc
				.perform(
						post("/user/messagecenter?action=delete&objectId="
								+ message.getId() + "&view=trash"))
				.andExpect(status().isFound())
				.andExpect(
						redirectedUrl("/user/messagecenter/?view=trash&show=0"));
	}

	@Test
	@WithMockUser(username = "test@user.ch", roles = { "USER" })
	public void testInvalidDeleteMessage() throws Exception {

		this.mockMvc
				.perform(
						post("/user/messagecenter?action=hogwarts&objectId="
								+ message.getId()))
				.andExpect(status().isFound())
				.andExpect(
						redirectedUrl("/user/messagecenter/?view=inbox&show=0"));
	}

	@Test
	@WithMockUser(username = "test@user.ch", roles = { "USER" })
	public void testValidNewMessage() throws Exception {
		User user = userDao.findByUsername("test@user.ch");

		this.mockMvc
				.perform(
						get("/user/messagecenter/new?receiverId="
								+ user.getId())).andExpect(status().isOk())
				.andExpect(forwardedUrl("/pages/user/new-message.jsp"))
				.andExpect(model().attributeExists("newMessageForm"));
	}

	@Test
	@WithMockUser(username = "test@user.ch", roles = { "USER" })
	public void testNewMessageUserNotFoundException() throws Exception {

		this.mockMvc
				.perform(get("/user/messagecenter/new?receiverId="))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("exception_message"))
				.andExpect(
						model().attribute("exception_message",
								"User not found."));
	}

	@Test
	@WithMockUser(username = "test@user.ch", roles = { "USER" })
	public void testReplyMessageMessageNotFoundException() throws Exception {

		this.mockMvc
				.perform(get("/user/messagecenter/reply?replyToMessageId="))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("/pages/exception.jsp"))
				.andExpect(model().attribute("exception_message", "Message not found."));
	}
	
	@Test
	@WithMockUser(username = "test@user.ch", roles = { "USER" })
	public void testReplyMessageUserNotFoundException() throws Exception {
		User user = userDao.findByUsername("test@user.ch");
			
		this.mockMvc
				.perform(get("/user/messagecenter/reply?replyToMessageId=" + message.getId()))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("/pages/user/new-message.jsp"))
				.andExpect(model().attributeExists("newMessageForm"))
				.andExpect(model().attribute("newMessageForm", hasProperty("sender", is(user))));
	}
	
	@Test
	@WithMockUser(username="test@user.ch", roles = {"USER"})
	public void testValidSendNewMessage() throws Exception{
		User user = userDao.findByUsername("test@user.ch");
		
		this.mockMvc
			.perform(post("/user/messagecenter/new")
				.param("receiverId", "" +user.getId())
				.param("receiverDisplayName", user.getFirstName() + " " +user.getLastName())
				.param("subject", "Hi")
				.param("message", "Hi there I'm using UTutor"))
				.andExpect(redirectedUrl("/user/messagecenter/?view=outbox"));
	}
	
	@Test
	@WithMockUser(username="test@user.ch", roles = {"USER"})
	public void testValidReplyToMessage() throws Exception{
		User user = userDao.findByUsername("test@user.ch");
		
		this.mockMvc
			.perform(post("/user/messagecenter/reply")
				.param("receiverId", "" +user.getId())
				.param("receiverDisplayName", user.getFirstName() + " " +user.getLastName())
				.param("subject", "Hi")
				.param("message", "Hi there I'm using UTutor"))
				.andExpect(redirectedUrl("/user/messagecenter/?view=outbox"));
	}
	
	@Test
	@WithMockUser(username="test@user.ch", roles = {"USER"})
	public void testInvalidNewMessageForm() throws Exception{
		
		this.mockMvc
			.perform(post("/user/messagecenter/new")
				.param("receiverId", "")
				.param("receiverDisplayName", "")
				.param("subject", "")
				.param("message", ""))
				.andExpect(forwardedUrl("/pages/user/new-message.jsp"))
				.andExpect(model().attributeHasFieldErrors("newMessageForm", "receiverId"))
				.andExpect(model().attributeHasFieldErrors("newMessageForm", "subject"))
				.andExpect(model().attributeHasFieldErrors("newMessageForm", "message"));
	}
	
	@Test
	@WithMockUser(username="test@user.ch", roles = {"USER"})
	public void testInvalidReplyMessageForm() throws Exception{
		
		this.mockMvc
			.perform(post("/user/messagecenter/reply")
				.param("receiverId", "")
				.param("receiverDisplayName", "")
				.param("subject", "")
				.param("message", ""))
				.andExpect(forwardedUrl("/pages/user/new-message.jsp"))
				.andExpect(model().attributeHasFieldErrors("newMessageForm", "receiverId"))
				.andExpect(model().attributeHasFieldErrors("newMessageForm", "subject"))
				.andExpect(model().attributeHasFieldErrors("newMessageForm", "message"));
	}
	
	@Test
	@WithMockUser(username="test@user.ch", roles = {"USER"})
	public void testSendMessageUserNotFoundException() throws Exception{
		User user = userDao.findByUsername("test@user.ch");
		
		this.mockMvc
			.perform(post("/user/messagecenter/new")
				.param("receiverId", "-1")
				.param("receiverDisplayName", user.getFirstName() + " " +user.getLastName())
				.param("subject", "Hi")
				.param("message", "Hi there I'm using UTutor"))
				.andExpect(forwardedUrl("/pages/exception.jsp"))
				.andExpect(model().attribute("exception_message", "User not found."));
	}
}
