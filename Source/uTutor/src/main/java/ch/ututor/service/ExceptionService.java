package ch.ututor.service;

import org.springframework.web.servlet.ModelAndView;

public interface ExceptionService {
	
	public ModelAndView addException( ModelAndView model, String exceptionMessage );
	
}
