package ch.ututor.tests.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;

import ch.ututor.exceptions.custom.PasswordRepetitionException;
import ch.ututor.exceptions.custom.UserAlreadyExistsException;
import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;
import ch.ututor.pojos.SignUpForm;
import ch.ututor.service.interfaces.SignUpService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalAnswers.*;

/**
* Tests class {@link ch.ututor.service.SignUpServiceImpl}
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/test/resources/signUpService.xml"})
public class SignUpServiceTest {
	
	@Autowired SignUpService signUpService;
	@Autowired UserDao userDao;
	private SignUpForm signUpForm;
	
	@Before
	public void setupSignup(){
		signUpForm = new SignUpForm();
		signUpForm.setFirstName("Harry");
		signUpForm.setLastName("Potter");
		signUpForm.setEmail("harry.potter@hogwarts.com");
		signUpForm.setPassword("12345678");
		signUpForm.setPasswordRepeat("12345678");
		when(userDao.save(any(User.class))).then(returnsFirstArg());
		when(userDao.findByUsername(any(String.class))).thenReturn(null);
	}
	
	@Test
	public void testCreateUserAccountSuccessfull(){
		
		User user = signUpService.createUserAccount(signUpForm);
		
		assertEquals("Harry", user.getFirstName());
		assertEquals("Potter", user.getLastName());
		assertEquals("harry.potter@hogwarts.com", user.getUsername());
		assertTrue(BCrypt.checkpw("12345678", user.getPassword()));
	}
	
	@Test(expected = PasswordRepetitionException.class)
	public void testPasswordRepetitionException(){
		signUpForm.setPasswordRepeat("Irgendwas");
		signUpService.createUserAccount(signUpForm);
	}
	
	@Test(expected = UserAlreadyExistsException.class)
	public void testUserAlreadyExistsException(){
		when(userDao.findByUsername(any(String.class))).thenReturn(new User());
		signUpService.createUserAccount(signUpForm);
	}
}
