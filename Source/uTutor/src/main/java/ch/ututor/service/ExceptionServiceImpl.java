package ch.ututor.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import ch.ututor.service.interfaces.ExceptionService;

//TODO: overload addException()

@Service
public class ExceptionServiceImpl implements ExceptionService {
	
	/**
	 *  @param model				mustn't be null
	 *	@param exceptionMessage		mustn't be null
	 *  @return ModelAndView with added exceptionMessage
	 */
	public ModelAndView addException( ModelAndView model, String exceptionMessage ) {
		assert( model != null && exceptionMessage != null );
		model.addObject( "exception_message", exceptionMessage );
		return model;
	}

	/**
	 *	@param exceptionMessage		mustn't be null
	 *	@return ModelAndView "exception" with added exceptionMessage
	 */
	@Override
	public ModelAndView addException( String exceptionMessage ) {
		return addException( new ModelAndView( "exception" ), exceptionMessage );
	}
}