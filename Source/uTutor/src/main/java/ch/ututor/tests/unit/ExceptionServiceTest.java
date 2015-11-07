package ch.ututor.tests.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

import ch.ututor.controller.service.ExceptionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/test/exceptionService.xml"})
public class ExceptionServiceTest {

	@Autowired ExceptionService exceptionService;
	
	private ModelAndView exception;
	private String exceptionMessage;
	
	@Test
	public void testCustomException() {
		exception = new ModelAndView("index");
		exceptionMessage = "Something went wrong in index!";
		
		ModelAndView model = exceptionService.addException(exception, exceptionMessage);
		
		assertEquals(exceptionMessage, model.getModel().get("exception_message"));
		assertEquals(exception.getViewName(), model.getViewName());
	}
	
	@Test
	public void testDefaultException() {
		exceptionMessage = "Something went wrong somewhere!";
		
		ModelAndView model = exceptionService.addException(null, exceptionMessage);
		
		assertEquals(exceptionMessage, model.getModel().get("exception_message"));
		assertEquals("exception", model.getViewName());
	}
}
