package ch.ututor.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.ututor.exceptions.CustomException;
import ch.ututor.pojos.SignUpForm;
import ch.ututor.service.interfaces.SignupService;
import ch.ututor.utils.ExceptionHelper;
import ch.ututor.utils.FlashMessage;

@Controller
public class SignUpController {
    
	@Autowired	private SignupService signupService;
	
	@RequestMapping( value = "/signup", method = RequestMethod.GET )
	public ModelAndView displaySignUpForm() {
	    	ModelAndView model = new ModelAndView( "signup" );
	    	model.addObject( "signUpForm", new SignUpForm() );
	        return model;
	}
	
	/**
	 *	@return	ModelAndView containing the login form if the registration has been successfully.
	 *			Otherwise the SignUpForm with exception messages.
	 */
	@RequestMapping( value = "/signup", method = RequestMethod.POST )
    public ModelAndView createUserAccount( 	@Valid SignUpForm signupForm, 
    										BindingResult result, 
    										RedirectAttributes redirectAttributes) {
		
    	ModelAndView model = new ModelAndView( "signup" );
    	if ( !result.hasErrors() ) {
            try {
            	signupService.createUserAccount( signupForm );
            	FlashMessage.addMessage(redirectAttributes, "Account successfully created.", FlashMessage.Type.SUCCESS);
            	model = new ModelAndView( "redirect:/login?username=" + signupForm.getEmail() );
            } catch ( CustomException e ){
            	model = ExceptionHelper.addException( e.getMessage(), model );
            }
        }
    	return model;
    }
}