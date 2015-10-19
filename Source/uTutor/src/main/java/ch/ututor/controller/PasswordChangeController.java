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
import ch.ututor.controller.pojos.ChangePasswordForm;
import ch.ututor.controller.service.AuthenticatedUserService;

@Controller
public class PasswordChangeController {
	
	@Autowired 	  AuthenticatedUserService authenticatedUserService;
    
    @RequestMapping(value={"/user/password"}, method = RequestMethod.GET)
    public ModelAndView password() {
    	ModelAndView model = new ModelAndView("profile-password");
    	model.addObject(new ChangePasswordForm());    	
        return model;
    }
    
    @RequestMapping( value = {"/user/password"}, method = RequestMethod.POST )
    public ModelAndView password(@Valid ChangePasswordForm changePasswordForm, BindingResult result, RedirectAttributes redirectAttributes){
    	ModelAndView model=new ModelAndView("profile-password");
    	if (!result.hasErrors()) {
            try{
            	authenticatedUserService.updatePassword(changePasswordForm);
            	model=new ModelAndView("redirect:/user/profile");
            } catch (FormException e ){
            	model.addObject("exception_message", e.getMessage());
            }
        }
    	return model;
    }
}