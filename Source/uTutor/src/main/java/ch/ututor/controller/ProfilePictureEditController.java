package ch.ututor.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.ututor.exceptions.CustomException;
import ch.ututor.model.User;
import ch.ututor.service.interfaces.AuthenticatedUserLoaderService;
import ch.ututor.service.interfaces.AuthenticatedUserService;
import ch.ututor.service.interfaces.ProfilePictureService;
import ch.ututor.utils.ExceptionHelper;
import ch.ututor.utils.FlashMessage;

/**
 *	This class handles all requests concerning the adaption of the user's profile picture.  
 */

@Controller
public class ProfilePictureEditController {
	
	@Autowired 	 private AuthenticatedUserLoaderService authenticatedUserLoaderService;
	@Autowired 	 private AuthenticatedUserService authenticatedUserService;
	@Autowired 	 private ProfilePictureService profilePictureService;
	
	/**
	 *	@return ModelAndView containing the needed information to display the profile picture.
	 */
    @RequestMapping( value = "/user/profile/picture", method = RequestMethod.GET )
    public ModelAndView displayProfilePicturePage() {
    	ModelAndView model = new ModelAndView( "user/profile-picture" );
    	User user = authenticatedUserLoaderService.getAuthenticatedUser();
    	model = addProfilePictureInfoToModel( model, user );
    	return model;
    }
    
    /**
     *	@return ModelAndView of the own profile if the update or the cleanup was successful.
	 *			Otherwise a ModelAndView to edit the profile picture.
     */
    @RequestMapping (value = "/user/profile/picture", method = RequestMethod.POST )
    public ModelAndView uploadNewProfilePicture(	@RequestParam( "action" ) String action, 
    												@RequestParam( "picture" ) MultipartFile file,
    												final RedirectAttributes redirectAttributes ) {
    	
    	User user = authenticatedUserLoaderService.getAuthenticatedUser();
    	ModelAndView model = new ModelAndView( "user/profile-picture" );
    	
    	if( action.equals( "upload" )){
    		try {
    			authenticatedUserService.updateProfilePicture( file );
    			FlashMessage.addMessage(redirectAttributes, "Profile picture successfully updated.", FlashMessage.Type.SUCCESS);
        		return new ModelAndView( "redirect:/user/profile" );
            } catch( CustomException e ) {
               	model = ExceptionHelper.addException( e.getMessage(), model );
            } catch( IOException e ) {
            	model = ExceptionHelper.addException( e.getMessage() );
            }
    	} else if ( action.equals( "delete" ) ) {
    		authenticatedUserService.removeProfilePicture();
    		return new ModelAndView( "redirect:/user/profile" );
    	}   	
    	
    	model = addProfilePictureInfoToModel( model, user );
    	return model;
    }
    
	/**
	 *	@param model	mustn't be null
	 *	@param user		mustn't be null
	 */
	private ModelAndView addProfilePictureInfoToModel( ModelAndView model, User user ){
		assert( model != null );
		assert( user != null );
		
		model.addObject( "userId", user.getId() );
    	model.addObject( "hasProfilePic", user.hasProfilePic() );
    	return model;
	}
}