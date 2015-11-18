package ch.ututor.tests.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import ch.ututor.exceptions.custom.PasswordNotCorrectException;
import ch.ututor.exceptions.custom.PasswordRepetitionException;
import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;
import ch.ututor.pojos.ChangePasswordForm;
import ch.ututor.pojos.ProfileEditForm;
import ch.ututor.service.interfaces.AuthenticatedUserLoaderService;
import ch.ututor.service.interfaces.AuthenticatedUserService;
import ch.ututor.service.interfaces.ProfilePictureService;
import ch.ututor.utils.MultipartFileMocker;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/test/authenticatedUserService.xml"})
public class AuthenticatedUserServiceTest {
	@Autowired AuthenticatedUserService authenticatedUserService;
	@Autowired AuthenticatedUserLoaderService authenticatedUserLoaderService;
	@Autowired ProfilePictureService profilePictureService;
	@Autowired UserDao userDao;
	
	
	private User setupUser(){
		User user = new User();
		user.setFirstName("Harry");
		user.setLastName("Potter");
		user.setDescription("Harry first appears in Harry Potter and the Philosopher's Stone.");
		user.setPassword("Nine and Three-Quarters");
		return user;
	}
	
	private ProfileEditForm setupProfileEditForm(){
		ProfileEditForm profileEditForm = new ProfileEditForm();
		profileEditForm.setFirstName("Harry");
		profileEditForm.setLastName("Potter");
		profileEditForm.setDescription("Harry first appears in Harry Potter and the Philosopher's Stone.");
		return profileEditForm;
	}
	
	private void setupMockAuthenticatedUser(Boolean emptyUser, Boolean isTutor){
		User user = new User();
		if(!emptyUser){
			user = setupUser();
		}
		user.setIsTutor(isTutor);
		setupMockAuthenticatedUser(user);
	}
	
	private void setupMockAuthenticatedUser(User user){
		when(authenticatedUserLoaderService.getAuthenticatedUser()).thenReturn(user);
	}
	
	
	@Before
	public void setupUserDao(){
		when(userDao.save(any(User.class))).then(returnsFirstArg());
	}
	
	
	@Test
	public void preFillProfileEditFormStudentTest(){
		setupMockAuthenticatedUser(false, false);
		ProfileEditForm profileEditForm = authenticatedUserService.preFillProfileEditForm(new ProfileEditForm());
		assertEquals("Harry", profileEditForm.getFirstName());
		assertEquals("Potter", profileEditForm.getLastName());
		assertNull(profileEditForm.getDescription());
	}
	
	@Test
	public void preFillProfileEditFormTutorTest(){
		setupMockAuthenticatedUser(false, true);
		
		ProfileEditForm profileEditForm = authenticatedUserService.preFillProfileEditForm(new ProfileEditForm());
		assertEquals("Harry", profileEditForm.getFirstName());
		assertEquals("Potter", profileEditForm.getLastName());
		assertEquals("Harry first appears in Harry Potter and the Philosopher's Stone.", profileEditForm.getDescription());
	}
	
	@Test
	public void updateUserDataStudentTest(){	
		setupMockAuthenticatedUser(true, false);
		ProfileEditForm profileEditForm = setupProfileEditForm();
		
		User user = authenticatedUserService.updateUserData(profileEditForm);
		assertEquals("Harry", user.getFirstName());
		assertEquals("Potter", user.getLastName());
		assertNull(user.getDescription());
	}
	
	@Test
	public void updateUserDataTutorTest(){	
		setupMockAuthenticatedUser(true, true);
		ProfileEditForm profileEditForm = setupProfileEditForm();
		
		User user = authenticatedUserService.updateUserData(profileEditForm);
		assertEquals("Harry", user.getFirstName());
		assertEquals("Potter", user.getLastName());
		assertEquals("Harry first appears in Harry Potter and the Philosopher's Stone.", user.getDescription());
	}
	
	@Test
	public void updatePasswordAcceptedTest(){
		setupMockAuthenticatedUser(false, false);
		ChangePasswordForm changePasswordForm = new ChangePasswordForm();
		changePasswordForm.setOldPassword("Nine and Three-Quarters");
		changePasswordForm.setNewPassword("Obliviate");
		changePasswordForm.setNewPasswordRepeat("Obliviate");
		User user = authenticatedUserService.updatePassword(changePasswordForm);
		assertTrue(BCrypt.checkpw("Obliviate", user.getPassword()));
	}
	
	@Test(expected = PasswordNotCorrectException.class)
	public void updatePasswordExistingWrongTest(){
		setupMockAuthenticatedUser(false, false);
		ChangePasswordForm changePasswordForm = new ChangePasswordForm();
		changePasswordForm.setOldPassword("Eight and Three-Quarters");
		authenticatedUserService.updatePassword(changePasswordForm);
	}
	
	@Test(expected = PasswordRepetitionException.class)
	public void updatePasswordRepetitionWrongTest(){
		setupMockAuthenticatedUser(false, false);
		ChangePasswordForm changePasswordForm = new ChangePasswordForm();
		changePasswordForm.setOldPassword("Nine and Three-Quarters");
		changePasswordForm.setNewPassword("Obliviate");
		changePasswordForm.setNewPasswordRepeat("Obiviate");
		authenticatedUserService.updatePassword(changePasswordForm);
	}
	
	@Test
	public void updateProfilePictureTest() throws IOException{
		setupMockAuthenticatedUser(true, false);
		MultipartFile file = MultipartFileMocker.mockJpeg("src/main/webapp/WEB-INF/test/images/350x100.jpg");
		when(profilePictureService.validateUploadedPicture(any(MultipartFile.class))).thenReturn(true);
		when(profilePictureService.resizeProfilePicture(file.getBytes())).then(returnsFirstArg());
		User user = authenticatedUserService.updateProfilePicture(file);
		assertEquals(file.getBytes(), user.getProfilePic());
	}
	
	@Test
	public void removeProfilePictureTest() throws IOException{
		MultipartFile file = MultipartFileMocker.mockJpeg("src/main/webapp/WEB-INF/test/images/350x100.jpg");
		User emtpyUser=new User();
		emtpyUser.setProfilePic(file.getBytes());
		setupMockAuthenticatedUser(emtpyUser);
		User user = authenticatedUserService.removeProfilePicture();
		assertNull(user.getProfilePic());
	}
	
	@Test
	public void isTutorStudentTest(){
		User emptyUser = new User();
		emptyUser.setIsTutor(false);
		setupMockAuthenticatedUser(emptyUser);
		assertFalse(authenticatedUserService.getIsTutor());
	}

	@Test
	public void isTutorTutorTest(){
		User emptyUser = new User();
		emptyUser.setIsTutor(true);
		setupMockAuthenticatedUser(emptyUser);
		assertTrue(authenticatedUserService.getIsTutor());
	}
}
