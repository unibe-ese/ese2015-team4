package ch.ututor.controller;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.ututor.controller.exceptions.NoLecturesFoundException;
import ch.ututor.controller.exceptions.UserNotFoundException;
import ch.ututor.controller.service.AuthenticatedUserService;
import ch.ututor.controller.service.ExceptionService;
import ch.ututor.controller.service.TutorService;
import ch.ututor.controller.service.UserService;
import ch.ututor.model.User;

/**
 *	This class is responsible to handle requests if a profile view is required 
 */

@Controller
public class ProfileViewController {
	
	@Autowired 	  AuthenticatedUserService authenticatedUserService;
	@Autowired 	  UserService userService;
	@Autowired	  ServletContext servletContext;
	@Autowired    TutorService tutorService;
	@Autowired    ExceptionService exceptionService;
	
	/**
	 * 	@return		A ModelAndView of a user profile
	 */
    @RequestMapping(value={"/user/profile"}, method = RequestMethod.GET)
    public ModelAndView viewProfile(@RequestParam(value = "userId", required=false) Long userId) {
    	ModelAndView model = new ModelAndView("user/profile");
    	User user = null;
    	User authUser = authenticatedUserService.getAuthenticatedUser();
    	
    	if( userId == null || authUser.getId() == userId ){
    		model.addObject("ownProfile", true);
    		user = authUser;
    	}else{
    		model.addObject("ownProfile", false);
    		try{
    			user = userService.load(userId);
    		}catch(UserNotFoundException e){
    			model = exceptionService.addException( model, e.getMessage() );
    		}
    	}
    	
    	if(user != null){
    		model.addObject(user);
    		if (user.getIsTutor()){
    			try{
    				model.addObject("lectures", tutorService.findLecturesByTutor(user));
    			}catch(NoLecturesFoundException e){
    				model = exceptionService.addException( model, e.getMessage() );
    			}
    		}
    	}
        return model;
    }
    
    /**
     *	@param action	Should be 'deleteLecture'
     *	@return A ModelAndView of the user's profile
     *	@throws NumberFormatException if objectId is not a valid Long
     */
    @RequestMapping(value="/user/profile", method = RequestMethod.POST)
    public ModelAndView deleteLectureFromProfile(@RequestParam("action") String action, @RequestParam("objectId") String objectId){
    	assert( action.equals("deleteLecture") );
    	
    	Long tutorLectureId = Long.parseLong(objectId);
    	
    	if( action.equals("deleteLecture") ){
    		tutorService.deleteTutorLecture( tutorLectureId );
    	}
    	
    	ModelAndView model = new ModelAndView("redirect:/user/profile");
    	return model;
    }
}