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
import ch.ututor.controller.pojos.ProfileEditForm;
import ch.ututor.controller.service.AuthenticatedUserLoaderService;
import ch.ututor.controller.service.AuthenticatedUserService;
import ch.ututor.controller.service.ExceptionService;
import ch.ututor.model.User;

/**
 *	This class handles the requests concerning the change of profile data
 *	(except the profile picture)
 */

@Controller
public class ProfileEditController {
	
	@Autowired 	  AuthenticatedUserLoaderService authenticatedUserLoaderService;
	@Autowired 	  AuthenticatedUserService authenticatedUserService;
	@Autowired 	  ExceptionService exceptionService;
	
	/**
	 *	Returns a ModelAndView containing a form to update the profile data.
	 */
    @RequestMapping(value={"/user/profile/edit"}, method = RequestMethod.GET)
    public ModelAndView displayProfileEditForm() {
    	ModelAndView model = new ModelAndView("user/profile-edit");
    	User user = authenticatedUserLoaderService.getAuthenticatedUser();
    	model.addObject( authenticatedUserService.preFillProfileEditForm( new ProfileEditForm() ) );
    	model.addObject( "isTutor", user.getIsTutor() );
    	return model;
    }
    
    /**
     * Updates the profile data based on the information entered in the profileEditForm
     * 
     *	@return	A ModelAndView of the own profile if the update has succeeded.
	 *			Otherwise a ModelAndView with a profile edit form containing error or exception messages.
     */
    @RequestMapping( value = {"/user/profile/edit"}, method = RequestMethod.POST )
    public ModelAndView updateProfileData( @Valid ProfileEditForm profileEditForm, BindingResult result, RedirectAttributes redirectAttributes ){
    	ModelAndView model = new ModelAndView("user/profile-edit");
    	User user = authenticatedUserLoaderService.getAuthenticatedUser();
    	
    	if (!result.hasErrors()) {
            try{
            	authenticatedUserService.updateUserData( profileEditForm );
            	model = new ModelAndView("redirect:/user/profile");
            } catch (CustomException e ){
            	model = exceptionService.addException( model, e.getMessage() );
            }
        }
    	
    	model.addObject("isTutor", user.getIsTutor());
    	return model;
    } 
}