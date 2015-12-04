package ch.ututor.utils;

import org.springframework.web.servlet.ModelAndView;

/**
 *	Offers methods to add an exception to an existing ModelAndView or
 *	to create a new ModelAndView with an exception message
 */

public class ExceptionHelper {
	/**
	 *  @param model				mustn't be null
	 *	@param exceptionMessage		mustn't be null
	 *  @return ModelAndView with added exceptionMessage
	 */
	public static ModelAndView addException( String exceptionMessage, ModelAndView model ) {
		assert( model != null && exceptionMessage != null );
		
		model.addObject( "exception_message", exceptionMessage );
		
		return model;
	}

	/**
	 *	@param exceptionMessage		mustn't be null
	 *	@return ModelAndView "exception" with added exceptionMessage
	 */
	public static ModelAndView addException( String exceptionMessage ) {
		assert( exceptionMessage != null );
		
		return addException( exceptionMessage, new ModelAndView( "exception" ) );
	}
}
