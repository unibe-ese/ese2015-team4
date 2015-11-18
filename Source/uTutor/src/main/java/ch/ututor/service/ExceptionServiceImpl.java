package ch.ututor.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import ch.ututor.service.interfaces.ExceptionService;

@Service
public class ExceptionServiceImpl implements ExceptionService {
	
	/**
	 *	@param exceptionMessage		mustn't be null
	 */
	public ModelAndView addException( ModelAndView model, String exceptionMessage ) {
		assert( exceptionMessage != null );
		
		if ( model == null ){
			model = new ModelAndView( "exception" );
		}
		
		model.addObject( "exception_message", exceptionMessage );
		return model;
	}
}