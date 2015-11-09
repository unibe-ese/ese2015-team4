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
import ch.ututor.controller.pojos.ChangePasswordForm;
import ch.ututor.controller.service.AuthenticatedUserService;
import ch.ututor.controller.service.ExceptionService;

@Controller
public class PasswordChangeController {
	
	@Autowired	private AuthenticatedUserService authenticatedUserService;
	@Autowired	private ExceptionService exceptionService;
    
    @RequestMapping( value = {"/user/password"}, method = RequestMethod.GET )
    public ModelAndView displayChangePasswordForm() {
    	ModelAndView model = new ModelAndView("user/change-password");
    	model.addObject( new ChangePasswordForm() );    	
        return model;
    }
    
    /**
     * @return	A model of the user profile if the password has been changed successfully.
     * 			Otherwise a page with the changePasswordForm.
     */
    @RequestMapping( value = {"/user/password"}, method = RequestMethod.POST )
    public ModelAndView changePassword( @Valid 	ChangePasswordForm changePasswordForm, 
    											BindingResult result, 
    											RedirectAttributes redirectAttributes ) {
    	ModelAndView model = new ModelAndView( "user/change-password" );
    	
    	if ( !result.hasErrors() ) {
            try {
            	authenticatedUserService.updatePassword( changePasswordForm );
            	return new ModelAndView( "redirect:/user/profile" );
            } catch ( CustomException e ) {
            	return exceptionService.addException( model, e.getMessage() );
            }
        }
    	
    	return model;
    }
}