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
     * Handles the profile action for deleting a tutor's lecture
     * @param lectureId	the id of the lecture which has to be deleted (posted as objectId)
     */
    @RequestMapping( value = "/user/profile", method = RequestMethod.POST, params = "action=deleteLecture" )
    public ModelAndView deleteLecture(	@RequestParam( "objectId" ) Long lectureId,
    	    												@RequestParam( value = "userId", required = false ) Long userId,
    	    												final RedirectAttributes redirectAttributes ) {
    	try{
    		
			tutorService.deleteTutorLecture( lectureId );
			FlashMessage.addMessage(redirectAttributes, "Lecture successfully deleted.", FlashMessage.Type.SUCCESS);
			
    	}catch( CustomException e ){
    		
    		return createProfileActionException( e, userId );
    		
    	}
    	return getActionRedirect( userId );
    	
    }
    
    /**
     * Handles the profile action for request a tutor's time-slot
     * @param timeSlotsId	the id of the time-slot which has to be requested (posted as objectId)
     */
    @RequestMapping( value = "/user/profile", method = RequestMethod.POST, params = "action=requestTimeSlot" )
    public ModelAndView requestTimeSlot(	@RequestParam( "objectId" ) Long timeSlotId,
    	    												@RequestParam( value = "userId", required = false ) Long userId,
    	    												final RedirectAttributes redirectAttributes ) {
    	try{
    		
    		timeSlotService.requestForTimeSlot( timeSlotId );
			FlashMessage.addMessage(redirectAttributes, "Request successfully sent.", FlashMessage.Type.SUCCESS);
			
    	}catch( CustomException e ){
    		
    		return createProfileActionException( e, userId );
    		
    	}
    	return getActionRedirect( userId );
    	
    }
    
    /**
     * Handles the profile action for deleting a tutor's time-slot
     * @param timeSlotsId	the id of the time-slot which has to be deleted (posted as objectId)
     */
    @RequestMapping( value = "/user/profile", method = RequestMethod.POST, params = "action=deleteTimeSlot" )
    public ModelAndView deleteTimeSlot(	@RequestParam( "objectId" ) Long timeSlotId,
    	    												@RequestParam( value = "userId", required = false ) Long userId,
    	    												final RedirectAttributes redirectAttributes ) {
    	try{
    		
    		timeSlotService.deleteTimeSlot( timeSlotId );
			FlashMessage.addMessage(redirectAttributes, "Time-slot successfully deleted.", FlashMessage.Type.SUCCESS);
			
    	}catch( CustomException e ){
    		
    		return createProfileActionException( e, userId );
    		
    	}
    	return getActionRedirect( userId );
    	
    }

    /**
     * Handles the profile action for accepting a time-slot request
     * @param timeSlotsId	the id of the requested time-slot which has to be accepted (posted as objectId)
     */
    @RequestMapping( value = "/user/profile", method = RequestMethod.POST, params = "action=acceptTimeSlot" )
    public ModelAndView acceptTimeSlot(	@RequestParam( "objectId" ) Long timeSlotId,
    	    												@RequestParam( value = "userId", required = false ) Long userId,
    	    												final RedirectAttributes redirectAttributes ) {
    	try{
    		
    		timeSlotService.acceptTimeSlotRequest( timeSlotId );
			FlashMessage.addMessage(redirectAttributes, "Time-slot accepted.", FlashMessage.Type.INFO);
			
    	}catch( CustomException e ){
    		
    		return createProfileActionException( e, userId );

    	}
    	return getActionRedirect( userId );
    	
    }

    /**
     * Handles the profile action for rejecting a time-slot request
     * @param timeSlotsId	the id of the requested time-slot which has to be rejected (posted as objectId)
     */
    @RequestMapping( value = "/user/profile", method = RequestMethod.POST, params = "action=rejectTimeSlot" )
    public ModelAndView rejectTimeSlot(	@RequestParam( "objectId" ) Long timeSlotId,
    	    												@RequestParam( value = "userId", required = false ) Long userId,
    	    												final RedirectAttributes redirectAttributes ) {
    	try{
    		
    		timeSlotService.rejectTimeSlotRequest( timeSlotId );
			FlashMessage.addMessage(redirectAttributes, "Time-slot rejected.", FlashMessage.Type.INFO);
			
    	}catch( CustomException e ){
    		
    		return createProfileActionException( e, userId );
    		
    	}
    	return getActionRedirect( userId );
    	
    }

    /**
     * Handles the profile action for rating a time-slot
     * @param timeSlotIdRating	String containing the id of the time-slot which has to be rated (posted as objectId) and the rating value.
     * 							Has to be in the format [timeSlotId]-[ratingValue] (e.g. "123-3")
     */
    @RequestMapping( value = "/user/profile", method = RequestMethod.POST, params = "action=rateTimeSlot" )
    public ModelAndView rateTimeSlot(	@RequestParam( "objectId" ) String timeSlotIdRating,
    	    												@RequestParam( value = "userId", required = false ) Long userId,
    	    												final RedirectAttributes redirectAttributes ) {
    	assert(timeSlotIdRating.contains("-"));
    	
    	Long timeSlotId = getTimeSlotIdFromString( timeSlotIdRating );
    	Integer rating = getRatingFromString( timeSlotIdRating );
    	
    	try{
    		
    		timeSlotService.rateTimeSlot( timeSlotId, rating );
			FlashMessage.addMessage(redirectAttributes, "Time-slot rated.", FlashMessage.Type.INFO);
			
    	}catch( CustomException e ){
    		
    		return createProfileActionException( e, userId );
    		
    	}
    	return getActionRedirect( userId );
    	
    }
    
    
    private Long getTimeSlotIdFromString( String timeSlotIdRating ){	
   		try{
   			String[] arr = timeSlotIdRating.split("-");
   			return Long.parseLong(arr[0]);
       	}catch( Exception e ){
       		return null;
       	}
    }
    
    private Integer getRatingFromString( String timeSlotIdRating ){	
   		try{
   			String[] arr = timeSlotIdRating.split("-");
   			return Integer.parseInt(arr[1]);
       	}catch( Exception e ){
       		return null;
       	}
    }
    
    private ModelAndView createProfileActionException( CustomException e, Long userId ){
		ModelAndView model = createUserProfileView( userId );
		return ExceptionHelper.addException( e.getMessage(), model );
    }
    
    private ModelAndView getActionRedirect( Long userId ){
    	if( userId== null ){
    		userId=authenticatedUserLoaderService.getAuthenticatedUser().getId();
    	}
    	return new ModelAndView( "redirect:/user/profile/?userId=" + userId );
    }
    
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