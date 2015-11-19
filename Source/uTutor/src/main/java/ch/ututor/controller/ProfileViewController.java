package ch.ututor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.ututor.exceptions.CustomException;
import ch.ututor.model.User;
import ch.ututor.service.interfaces.AuthenticatedUserLoaderService;
import ch.ututor.service.interfaces.ExceptionService;
import ch.ututor.service.interfaces.TimeSlotService;
import ch.ututor.service.interfaces.TutorService;
import ch.ututor.service.interfaces.UserService;

@Controller
public class ProfileViewController {
	
	@Autowired 	 private AuthenticatedUserLoaderService authenticatedUserLoaderService;
	@Autowired 	 private UserService userService;
	@Autowired   private TutorService tutorService;
	@Autowired   private TimeSlotService timeSlotService;
	@Autowired   private ExceptionService exceptionService;
	
	/**
	 * 	@return	ModelAndView of a user profile
	 */
    @RequestMapping( value = { "/user/profile" }, method = RequestMethod.GET )
    public ModelAndView viewProfile( @RequestParam( value = "userId", required = false ) Long userId ) {
    	ModelAndView model = new ModelAndView( "user/profile" );
    	User user = null;
    	User authUser = authenticatedUserLoaderService.getAuthenticatedUser();
    	
    	if( userId == null || authUser.getId().longValue() == userId.longValue() ) {
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
    		model.addObject( "timeSlotList", timeSlotService.getTimeSlotsByUser(user) );
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
    	}else if( action.equals( "requestTimeSlot" ) ){
    		//TODO: the authenticated user requested the timeslot with the id saved in objectId
    		//check if state of timeslot AVAILABLE - if so save and send request message to tutor in timeslot otherwise throw exception
    	}else if( action.equals( "deleteTimeSlot" ) ){
    		//TODO: the authenticated user wants to delete the timeslot with the id saved in objectId
    		//check if state of timeslot is AVAILABLE and tutor equals authenticated user - if so delete timeslot otherwise throw exception (or do nothing???)
    	}else if( action.equals( "acceptTimeSlot" ) ){
    		//TODO: the authenticated user wants to accept a request for the timeslot with the id saved in objectId
    		//check if state of timeslot is REQUEST and tutor equals authenticated user - if so, add authenticated user as student and set state to ACCEPTED otherwise throw exception (or do nothing???)
    	}else if( action.equals( "rejectTimeSlot" ) ){
    		//TODO: the authenticated user wants to reject a request for the timeslot with the id saved in objectId
    		//check if state of timeslot is REQUEST and tutor equals authenticated user - if so, set state to AVAILABLE otherwise throw exception (or do nothing???)
    	}
    	
    	
    	ModelAndView model = new ModelAndView( "redirect:/user/profile" );
    	return model;
    }
}