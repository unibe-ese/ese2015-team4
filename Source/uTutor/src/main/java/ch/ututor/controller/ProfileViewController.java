package ch.ututor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.ututor.exceptions.CustomException;
import ch.ututor.model.User;
import ch.ututor.service.interfaces.AuthenticatedUserLoaderService;
import ch.ututor.service.interfaces.TimeSlotService;
import ch.ututor.service.interfaces.TutorService;
import ch.ututor.service.interfaces.UserService;
import ch.ututor.utils.ExceptionHelper;
import ch.ututor.utils.FlashMessage;

@Controller
public class ProfileViewController {
	
	@Autowired 	 private AuthenticatedUserLoaderService authenticatedUserLoaderService;
	@Autowired 	 private UserService userService;
	@Autowired   private TutorService tutorService;
	@Autowired   private TimeSlotService timeSlotService;
	
	/**
	 * 	@return	ModelAndView of a user profile
	 */
    @RequestMapping( value = { "/user/profile" }, method = RequestMethod.GET )
    public ModelAndView viewProfile( @RequestParam( value = "userId", required = false ) Long userId ) {
    	return createUserProfileView( userId );
    }
    
    /**
     *	@param action	Should be 'deleteLecture'
     *	@return ModelAndView of the user's profile
     *	@throws NumberFormatException if objectId is not a valid Long
     */
    @RequestMapping( value = "/user/profile", method = RequestMethod.POST )
    public ModelAndView handleProfileActions(	@RequestParam( "action" ) String action, 
    												@RequestParam( "objectId" ) String objectIdString,
    												@RequestParam( value = "userId", required = false ) Long userId,
    												final RedirectAttributes redirectAttributes ) {
    	
    	
    	if( userId== null ){
    		userId=authenticatedUserLoaderService.getAuthenticatedUser().getId();
    	}
    	ModelAndView model = new ModelAndView( "redirect:/user/profile/?userId=" + userId );
 
    	long objectId;
    	int  rating = 0;
    	
    	if( objectIdString.contains("-") ){
    		String[] arr = objectIdString.split("-");
    		objectIdString = arr[0];
    		try{
        		rating = Integer.parseInt( arr[1] );
        	}catch( NumberFormatException e ){
        		return ExceptionHelper.addException(e.getMessage());
        	}
    	}

    	try{
    		objectId = Long.parseLong( objectIdString );
    	}catch( NumberFormatException e ){
    		return ExceptionHelper.addException(e.getMessage());
    	}
    	
    	try{
    		if( action.equals( "deleteLecture" ) ){
    			
    			tutorService.deleteTutorLecture( objectId );
    			FlashMessage.addMessage(redirectAttributes, "Lecture successfully deleted.", FlashMessage.Type.SUCCESS);
    			
    		}else if( action.equals( "requestTimeSlot" ) ){
    	
    			timeSlotService.requestForTimeSlot( objectId );
    			FlashMessage.addMessage(redirectAttributes, "Request successfully sent.", FlashMessage.Type.SUCCESS);
    			
    		}else if( action.equals( "deleteTimeSlot" ) ){
    			
    			timeSlotService.deleteTimeSlot( objectId );
    			FlashMessage.addMessage(redirectAttributes, "Time-slot successfully deleted.", FlashMessage.Type.SUCCESS);
    			
    		}else if( action.equals( "acceptTimeSlot" ) ){
    			
    			timeSlotService.acceptTimeSlotRequest( objectId );
    			FlashMessage.addMessage(redirectAttributes, "Time-slot accepted.", FlashMessage.Type.INFO);

    		}else if( action.equals( "rejectTimeSlot" ) ){
    			
    			timeSlotService.rejectTimeSlotRequest( objectId );
    			FlashMessage.addMessage(redirectAttributes, "Time-slot rejected.", FlashMessage.Type.INFO);
    			
    		}else if( action.equals( "rateTimeSlot" ) ){
    			
    			timeSlotService.rateTimeSlot( objectId, rating );
    			FlashMessage.addMessage(redirectAttributes, "Time-slot rated.", FlashMessage.Type.INFO);
    			
    		}
    	}catch( CustomException e ){
    		model = createUserProfileView( userId );
    		return ExceptionHelper.addException( e.getMessage(), model );
    	}
    	return model;
    }
    
    //TODO: move to UserService
    private ModelAndView createUserProfileView( Long userId ){
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
    			return ExceptionHelper.addException( e.getMessage() );
    		}
    	}
    	
    	if( user != null ) {
    		model.addObject( user );
    		if( user.getIsTutor() ) {
    			try {
    				model.addObject( "lectures", tutorService.findLecturesByTutor( user ) );
    			} catch(CustomException e) {
    				model = ExceptionHelper.addException( e.getMessage(), model );
    			}
    		}
    		model.addObject( "timeSlotList", timeSlotService.getTimeSlotsByUser(user) );
    	}
        return model;
    }
}