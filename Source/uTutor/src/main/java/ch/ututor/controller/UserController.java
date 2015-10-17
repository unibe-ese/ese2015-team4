package ch.ututor.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.ututor.controller.exceptions.FormException;
import ch.ututor.controller.pojos.ProfileEditForm;
import ch.ututor.controller.pojos.SignUpForm;
import ch.ututor.controller.service.UserService;
import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;

@Controller
public class UserController {
	
	@Autowired    UserDao userDao;
	@Autowired 	  UserService userService;
 
	
    @RequestMapping(value={"/user/profile"}, method = RequestMethod.GET)
    public ModelAndView profile(@RequestParam(value = "userId", required=false) Long userId) {
    	ModelAndView model = new ModelAndView("profile");
    	User user;
    	if(userId==null){
    		model.addObject("ownProfile",true);
    		user = userService.getAuthenticatedUser();
    	}else{
    		if(userService.getAuthenticatedUser().getId()==userId){
    			model.addObject("ownProfile",true);
    		}else{
    			model.addObject("ownProfile",false);
    		}
    		user = userDao.findById(userId);
    	}
    	model.addObject(user);
        return model;
    }
    
    @RequestMapping(value={"/user/profile/edit"}, method = RequestMethod.GET)
    public ModelAndView edit() {
    	ModelAndView model = new ModelAndView("profile-edit");
    	User user = userService.getAuthenticatedUser();
    	model.addObject(user);
    	model.addObject(new ProfileEditForm());
    	
        return model;
    }
    
    @RequestMapping( value = {"/user/profile/edit"}, method = RequestMethod.POST )
    public ModelAndView edit(@Valid ProfileEditForm profileEditForm, BindingResult result, RedirectAttributes redirectAttributes){
    	ModelAndView model=new ModelAndView("profile-edit");
    	if (!result.hasErrors()) {
            try{
            	userService.update( profileEditForm, userService.getAuthenticatedUser());
            	model=new ModelAndView("redirect:/user/profile");
            } catch (FormException e ){
            	model.addObject("exception_message", e.getMessage());
            }
        }
    	
    	return model;
    }
}