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
import ch.ututor.model.User;
import ch.ututor.pojos.ProfileEditForm;
import ch.ututor.service.interfaces.AuthenticatedUserLoaderService;
import ch.ututor.service.interfaces.AuthenticatedUserService;
import ch.ututor.utils.ExceptionHelper;
import ch.ututor.utils.FlashMessage;

@Controller
public class ProfileEditController {
	
	@Autowired 	 private AuthenticatedUserLoaderService authenticatedUserLoaderService;
	@Autowired 	 private AuthenticatedUserService authenticatedUserService;
	
	/**
	 *	@return ModelAndView containing a form to update the profile data.
	 */
    @RequestMapping( value = {"/user/profile/edit"}, method = RequestMethod.GET )
    public ModelAndView displayProfileEditForm() {
    	ModelAndView model = new ModelAndView( "user/profile-edit" );
    	User user = authenticatedUserLoaderService.getAuthenticatedUser();
    	model.addObject( authenticatedUserService.preFillProfileEditForm( new ProfileEditForm() ) );
    	model.addObject( "isTutor", user.getIsTutor() );
    	return model;
    }
    
    /**
     *  Updates the profile data based on the information entered in the profileEditForm
     * 
     *	@return	ModelAndView of the own profile if the update has succeeded.
	 *			Otherwise ModelAndView with a profile edit form containing error or exception messages.
     */
    @RequestMapping( value = {"/user/profile/edit"}, method = RequestMethod.POST )
    public ModelAndView updateProfileData( 	@Valid ProfileEditForm profileEditForm, 
    										BindingResult result, 
    										RedirectAttributes redirectAttributes ) {
    	ModelAndView model = new ModelAndView( "user/profile-edit" );
    	User user = authenticatedUserLoaderService.getAuthenticatedUser();
    	
    	if ( !result.hasErrors() ) {
            try {
            	authenticatedUserService.updateUserData( profileEditForm );
            	FlashMessage.addMessage(redirectAttributes, "Profile successfully updated.", FlashMessage.Type.SUCCESS);
            	model = new ModelAndView( "redirect:/user/profile" );
            } catch ( CustomException e ) {
            	model = ExceptionHelper.addException( e.getMessage() );
            }
        }
    	
    	model.addObject( "isTutor", user.getIsTutor() );
    	return model;
    } 
}