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
import ch.ututor.controller.service.ProfilePictureService;
import ch.ututor.model.User;

@Controller
public class ProfilePictureEditController {
	@Autowired 	  AuthenticatedUserService authenticatedUserService;
	@Autowired 	  ProfilePictureService profilePictureService;
    
    @RequestMapping(value="/user/profile/picture", method = RequestMethod.GET)
    public ModelAndView picture(){
    	ModelAndView model=new ModelAndView("user/profile-picture");
    	User user=authenticatedUserService.getAuthenticatedUser();
    	model.addObject("userId",user.getId());
    	model.addObject("hasProfilePic",user.hasProfilePic());
    	return model;
    }
    
    @RequestMapping(value="/user/profile/picture", method = RequestMethod.POST)
    public ModelAndView uploadPicture(@RequestParam("action") String action, @RequestParam("picture") MultipartFile file){
    	User user=authenticatedUserService.getAuthenticatedUser();
    	String exceptionMessage = null;
    	if(action.equals("upload")){
    		try{
    			authenticatedUserService.updateProfilePicture(file);
        		return new ModelAndView("redirect:/user/profile");
            } catch (FormException e){
               	exceptionMessage=e.getMessage(); 
            } catch (Exception e){
              	exceptionMessage="Unknown Exception: "+e.getMessage();
            }
    	}else if(action.equals("delete")){
    		authenticatedUserService.removeProfilePicture();
    		return new ModelAndView("redirect:/user/profile");
    	}
    	
    	ModelAndView model=new ModelAndView("user/profile-picture");
    	model.addObject("userId",user.getId());
    	model.addObject("hasProfilePic",user.hasProfilePic());
    	model.addObject("exception_message",exceptionMessage);
    	return model;
    }
}