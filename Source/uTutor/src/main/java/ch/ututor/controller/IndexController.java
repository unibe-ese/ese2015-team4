package ch.ututor.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.ututor.controller.exceptions.UserAlreadyExistsException;
import ch.ututor.controller.pojos.SignUpForm;
import ch.ututor.controller.service.LoginService;
import ch.ututor.model.User;

@Controller
public class IndexController {
    
	@Autowired
    LoginService loginService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
    	ModelAndView model = new ModelAndView("index");
    	model.addObject("signUpForm", new SignUpForm() );
        return model;
    }
	
	 @RequestMapping( value = "/signup", method = RequestMethod.GET )
	 public ModelAndView signup() {
	    	ModelAndView model = new ModelAndView("signup");
	    	model.addObject("signUpForm", new SignUpForm() );
	        return model;
	 }
	
    @RequestMapping( value = "/signup", method = RequestMethod.POST )
    public ModelAndView signup(@Valid SignUpForm signupForm, BindingResult result, RedirectAttributes redirectAttributes){
    	ModelAndView model;
    	model = new ModelAndView("signup");
    	if (!result.hasErrors()) {
            try{
            	User user = loginService.saveForm( signupForm );
            	model.addObject("redirectUrl","/profile?userId="+user.getId());
            	//model = new ModelAndView( "profile" );
            	//model.addObject("user", user);
            } catch ( UserAlreadyExistsException e ){
            	//model = new ModelAndView("index");
            	model.addObject("page_error", e.getMessage());
            }
        } else {
        	//model = new ModelAndView("index");
        }   	
    	
    	return model;
    }
    
    @RequestMapping(value={"/profile"}, method = RequestMethod.GET)
    public ModelAndView profile(@RequestParam("userId") Long userId) {
    	ModelAndView model = new ModelAndView("profile");
    	
        return model;
    }

}
