package ch.ututor.utils;

import org.springframework.web.servlet.ModelAndView;

public class ExceptionHelper {
	/**
	 *  @param model				mustn't be null
	 *	@param exceptionMessage		mustn't be null
	 *  @return ModelAndView with added exceptionMessage
	 */
	public static ModelAndView addExpectedException( ModelAndView model, String exceptionMessage ) {
		assert( model != null && exceptionMessage != null );
		model.addObject( "exception_message", exceptionMessage );
		return model;
	}

	/**
	 *	@param exceptionMessage		mustn't be null
	 *	@return ModelAndView "exception" with added exceptionMessage
	 */
	public static ModelAndView addUnexpectedException( String exceptionMessage ) {
		return addExpectedException( new ModelAndView( "exception" ), exceptionMessage );
	}
}
