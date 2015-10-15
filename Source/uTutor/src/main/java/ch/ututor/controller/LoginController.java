package ch.ututor.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.ututor.controller.exceptions.FormException;
import ch.ututor.controller.pojos.LoginForm;
import ch.ututor.controller.service.LoginService;
import ch.ututor.model.User;

@Controller
public class LoginController {
    
	@Autowired
    LoginService loginService;
	
	 @RequestMapping( value = "/login", method = RequestMethod.GET )
	 public ModelAndView signup() {
	    	ModelAndView model = new ModelAndView("login");
	    	model.addObject("loginForm", new LoginForm() );
	        return model;
	 }
	
    @RequestMapping( value = "/login", method = RequestMethod.POST )
    public ModelAndView signup(@Valid LoginForm loginForm, BindingResult result, RedirectAttributes redirectAttributes){
    	ModelAndView model=new ModelAndView("login");
    	if (!result.hasErrors()) {
            try{
            	User user = loginService.login( loginForm );
            	model=new ModelAndView("redirect:/profile?userId="+user.getId());
            } catch (FormException e ){
            	model.addObject("login_exception", e.getMessage());
            }
        }
    	
    	return model;
    }
}
