package ch.ututor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ch.ututor.controller.exceptions.FormException;
import ch.ututor.controller.service.AuthenticatedUserService;
import ch.ututor.controller.service.ExceptionService;
import ch.ututor.controller.service.ProfilePictureService;
import ch.ututor.model.User;

/**
 *	This class handles all requests concerning the adaption of the user's profile picture.  
 */

@Controller
public class ProfilePictureEditController {
	
	@Autowired 	  AuthenticatedUserService authenticatedUserService;
	@Autowired 	  ProfilePictureService profilePictureService;
	@Autowired 	  ExceptionService exceptionService;
	
	/**
	 *	@return a ModelAndView containing the needed information to display the profile picture.
	 */
    @RequestMapping(value="/user/profile/picture", method = RequestMethod.GET)
    public ModelAndView displayProfilePicturePage(){
    	ModelAndView model = new ModelAndView( "user/profile-picture" );
    	User user = authenticatedUserService.getAuthenticatedUser();
    	model = profilePictureService.addUserDataToModel( model, user );
    	return model;
    }
    
    /**
     *	@return A ModelAndView of the own profile if the update or the cleanup was successful.
	 *			Else a ModelAndView to edit the profile picture.
     */
    @RequestMapping(value="/user/profile/picture", method = RequestMethod.POST)
    public ModelAndView uploadNewProfilePicture(@RequestParam("action") String action, @RequestParam("picture") MultipartFile file){
    	User user = authenticatedUserService.getAuthenticatedUser();
    	ModelAndView model = new ModelAndView("user/profile-picture");
    	
    	if( action.equals( "upload" )){
    		try{
    			authenticatedUserService.updateProfilePicture( file );
        		return new ModelAndView( "redirect:/user/profile" );
            }catch(FormException e){
               	model = exceptionService.addException( model, e.getMessage() );
            }catch(Exception e){
            	model = exceptionService.addException( model, "Unknown Exception: " + e.getMessage() );
            }
    	} else if ( action.equals("delete" )){
    		authenticatedUserService.removeProfilePicture();
    		return new ModelAndView("redirect:/user/profile");
    	}   	
    	
    	model = profilePictureService.addUserDataToModel( model, user );
    	return model;
    }
}