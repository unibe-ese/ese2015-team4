package ch.ututor.controller;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ch.ututor.controller.exceptions.FormException;
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
    	ModelAndView model = new ModelAndView("profile");
    	User user = null;
    	if(userId==null){
    		model.addObject("ownProfile",true);
    		user = authenticatedUserService.getAuthenticatedUser();
    	}else{
    		if(authenticatedUserService.getAuthenticatedUser().getId()==userId){
    			model.addObject("ownProfile",true);
    		}else{
    			model.addObject("ownProfile",false);
    		}
    		try{
    			user = userService.load(userId);
    		}catch(UserNotFoundException e){
    			model.addObject("exception_message",e.getMessage());
    		}
    	}
    	model.addObject("user", user);
    	
    	if ( user.getIsTutor() ){
    		try{
        		model.addObject("lectures", tutorService.findLectures( user ) );
        	}catch( NoLecturesFoundException e ){
        		model.addObject("exception_message", e.getMessage());
        	}
    	}
        return model;
    }
    
    @RequestMapping(value="/user/profile", method = RequestMethod.POST)
    public ModelAndView deleteLecture(@RequestParam("action") String action){
    	
    	Long id = Long.parseLong( action );
    	tutorService.deleteLecture( id );
    	
    	ModelAndView model = new ModelAndView("redirect:/user/profile");
    	return model;
    }
    
}