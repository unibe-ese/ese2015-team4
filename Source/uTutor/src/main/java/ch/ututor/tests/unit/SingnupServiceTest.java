package ch.ututor.tests.unit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.ututor.controller.exceptions.form.PasswordRepetitionException;
import ch.ututor.controller.exceptions.form.UserAlreadyExistsException;
import ch.ututor.controller.pojos.SignUpForm;
import ch.ututor.controller.service.SignupService;
import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalAnswers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/test/signupService.xml"})
public class SingnupServiceTest {
	
	@Autowired SignupService signupService;
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
		
		User user = signupService.createUserAccount(signUpForm);
		
		assertEquals("Harry", user.getFirstName());
		assertEquals("Potter", user.getLastName());
		assertEquals("harry.potter@hogwarts.com", user.getUsername());
	}
	
	@Test(expected = PasswordRepetitionException.class)
	public void testPasswordRepetitionException(){
		signUpForm.setPasswordRepeat("Irgendwas");
		signupService.createUserAccount(signUpForm);
	}
	
	@Test(expected = UserAlreadyExistsException.class)
	public void testUserAlreadyExistsException(){
		when(userDao.findByUsername(any(String.class))).thenReturn(new User());
		signupService.createUserAccount(signUpForm);
	}
}
