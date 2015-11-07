package ch.ututor.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import ch.ututor.tests.unit.*;

@RunWith(Suite.class)
@SuiteClasses({AuthenticatedUserServiceTest.class, ProfilePictureServiceTest.class, SignupServiceTest.class, 
	ExceptionServiceTest.class, SearchServiceTest.class, UserServiceTest.class, TutorServiceTest.class})
public class UnitTestSuite {
 
}
