package ch.ututor.tests.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.ututor.exceptions.custom.UserNotFoundException;
import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;
import ch.ututor.service.interfaces.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/test/resources/userService.xml"})
public class UserServiceTest {

	@Autowired UserDao userDao;
	@Autowired UserService userService;
	
	@Test(expected = UserNotFoundException.class)
	public void testUserNotFoundById() {
		when(userDao.findById(any(Long.class))).thenReturn(null);
		userService.load(1L);
	}
	
	@Test(expected = UserNotFoundException.class)
	public void testUserNotFoundByUsername() {
		when(userDao.findByUsername(any(String.class))).thenReturn(null);
		userService.load("");
	}
	
	@Test
	public void testUserLoad() {
		when(userDao.findById(any(Long.class))).thenReturn(new User());
		User user = userService.load(1L);
		assertNotNull(user);
		
		when(userDao.findByUsername(any(String.class))).thenReturn(new User());
		user = userService.load("");
		assertNotNull(user);
	}
	
	@Test
	public void testUserSave() { 
		when(userDao.save(any(User.class))).then(returnsFirstArg());
		User user = new User();
		user.setId(1L);
		user.setFirstName("John");
		user.setLastName("Doe");
		User savedUser = userService.save(user);
		assertEquals(savedUser, user);
	}
}
