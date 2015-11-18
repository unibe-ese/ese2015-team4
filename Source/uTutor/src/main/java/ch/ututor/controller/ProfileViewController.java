package ch.ututor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.ututor.exceptions.CustomException;
import ch.ututor.model.User;
import ch.ututor.service.AuthenticatedUserLoaderService;
import ch.ututor.service.ExceptionService;
import ch.ututor.service.TutorService;
import ch.ututor.service.UserService;

@Controller
public class ProfileViewController {
	
	@Autowired 	 private AuthenticatedUserLoaderService authenticatedUserLoaderService;
	@Autowired 	 private UserService userService;
	@Autowired   private TutorService tutorService;
	@Autowired   private ExceptionService exceptionService;
	
	/**
	 * 	@return	ModelAndView of a user profile
	 */
    @RequestMapping( value = { "/user/profile" }, method = RequestMethod.GET )
    public ModelAndView viewProfile( @RequestParam( value = "userId", required = false ) Long userId ) {
    	ModelAndView model = new ModelAndView( "user/profile" );
    	User user = null;
    	User authUser = authenticatedUserLoaderService.getAuthenticatedUser();
    	
    	if( userId == null || authUser.getId() == userId ) {
    		model.addObject( "ownProfile", true );
    		user = authUser;
    	} else {
    		model.addObject( "ownProfile", false );
    		try {
    			user = userService.load( userId );
    		} catch(CustomException e) {
    			model = exceptionService.addException( null, e.getMessage() );
    		}
    	}
    	
    	if( user != null ) {
    		model.addObject( user );
    		if( user.getIsTutor() ) {
    			try {
    				model.addObject( "lectures", tutorService.findLecturesByTutor( user ) );
    			} catch(CustomException e) {
    				model = exceptionService.addException( model, e.getMessage() );
    			}
    		}
    	}
        return model;
    }
    
    /**
     *	@param action	Should be 'deleteLecture'
     *	@return ModelAndView of the user's profile
     *	@throws NumberFormatException if objectId is not a valid Long
     */
    @RequestMapping( value = "/user/profile", method = RequestMethod.POST )
    public ModelAndView deleteLectureFromProfile(	@RequestParam( "action" ) String action, 
    												@RequestParam( "objectId" ) String objectId ) {
    	assert( action.equals( "deleteLecture" ) );
    	
    	Long tutorLectureId = Long.parseLong( objectId );
    	
    	if( action.equals( "deleteLecture" ) ){
    		tutorService.deleteTutorLecture( tutorLectureId );
    	}
    	
    	ModelAndView model = new ModelAndView( "redirect:/user/profile" );
    	return model;
    }
}