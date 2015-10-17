package ch.ututor.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.ututor.controller.exceptions.FormException;
import ch.ututor.controller.pojos.SignUpForm;
import ch.ututor.controller.service.SignupService;
import ch.ututor.model.User;

@Controller
public class SignUpController {
    
	@Autowired
    SignupService signupService;
	
	 @RequestMapping( value = "/signup", method = RequestMethod.GET )
	 public ModelAndView signup() {
	    	ModelAndView model = new ModelAndView("signup");
	    	model.addObject("signUpForm", new SignUpForm() );
	        return model;
	 }
	 

	
    @RequestMapping( value = "/signup", method = RequestMethod.POST )
    public ModelAndView signup(@Valid SignUpForm signupForm, BindingResult result, RedirectAttributes redirectAttributes, HttpServletRequest request){
    	ModelAndView model=new ModelAndView("signup");
    	if (!result.hasErrors()) {
            try{
            	signupService.saveForm(signupForm);
            	model=new ModelAndView("redirect:/login?username="+signupForm.getEmail());
            } catch (FormException e ){
            	model.addObject("exception_message", e.getMessage());
            } catch (Exception e){
            	model.addObject("exception_message", e.getMessage());
            }
        }
    	
    	return model;
    }
}
