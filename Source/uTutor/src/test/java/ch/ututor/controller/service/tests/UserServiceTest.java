package ch.ututor.controller.service.tests;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.ututor.controller.exceptions.custom.UserNotFoundException;
import ch.ututor.controller.service.UserService;
import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/test/userService.xml"})
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
}
