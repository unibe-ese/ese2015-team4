package ch.ututor.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.ututor.controller.exceptions.CustomException;
import ch.ututor.controller.pojos.SignUpForm;
import ch.ututor.controller.service.ExceptionService;
import ch.ututor.controller.service.SignupService;

@Controller
public class SignUpController {
    
	@Autowired	private SignupService signupService;
	@Autowired	private ExceptionService exceptionService;
	
	@RequestMapping( value = "/signup", method = RequestMethod.GET )
	public ModelAndView displaySignUpForm() {
	    	ModelAndView model = new ModelAndView( "signup" );
	    	model.addObject( "signUpForm", new SignUpForm() );
	        return model;
	}
	
	/**
	 *	@return	A ModelAndView containing the login form if the registration has been successfully.
	 *			Otherwise the sign up form with the exception messages.
	 */
	@RequestMapping( value = "/signup", method = RequestMethod.POST )
    public ModelAndView createUserAccount( 	@Valid SignUpForm signupForm, 
    										BindingResult result, 
    										RedirectAttributes redirectAttributes) {
		
    	ModelAndView model = new ModelAndView( "signup" );
    	if ( !result.hasErrors() ) {
            try {
            	signupService.createUserAccount( signupForm );
            	model = new ModelAndView( "redirect:/login?username=" + signupForm.getEmail() );
            } catch ( CustomException e ){
            	model = exceptionService.addException( model, e.getMessage() );
            }
        }
    	return model;
    }
}