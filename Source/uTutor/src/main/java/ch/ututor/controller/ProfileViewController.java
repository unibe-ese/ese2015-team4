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
import ch.ututor.controller.service.TutorService;
import ch.ututor.controller.service.UserService;
import ch.ututor.model.User;

@Controller
public class ProfileViewController {
	
	@Autowired 	  AuthenticatedUserService authenticatedUserService;
	@Autowired 	  UserService userService;
	@Autowired	  ServletContext servletContext;
	@Autowired    TutorService tutorService;
	
    @RequestMapping(value={"/user/profile"}, method = RequestMethod.GET)
    public ModelAndView profile(@RequestParam(value = "userId", required=false) Long userId) {
    	ModelAndView model = new ModelAndView("user/profile");
    	User user = null;
    	User authUser = authenticatedUserService.getAuthenticatedUser();
    	if(userId == null || authUser.getId()==userId){
    		model.addObject("ownProfile",true);
    		user = authUser;
    	}else{
    		model.addObject("ownProfile",false);
    		try{
    			user = userService.load(userId);
    		}catch(UserNotFoundException e){
    			model.addObject("exception_message",e.getMessage());
    		}
    	}
    	
    	if(user!=null){
    		model.addObject(user);
    		if (user.getIsTutor()){
    			try{
    				model.addObject("lectures", tutorService.findLectures(user));
    			}catch(NoLecturesFoundException e){
    				model.addObject("exception_message", e.getMessage());
    			}
    		}
    	}
        return model;
    }
    
    @RequestMapping(value="/user/profile", method = RequestMethod.POST)
    public ModelAndView deleteLecture(@RequestParam("action") String action, @RequestParam("objectId") String objectId){
    	if(action.equals("deleteLecture")){
    		tutorService.deleteTutorLecture(Long.parseLong(objectId));
    	}
    	
    	ModelAndView model = new ModelAndView("redirect:/user/profile");
    	return model;
    }
}