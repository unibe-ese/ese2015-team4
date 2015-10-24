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
import ch.ututor.controller.pojos.ProfileEditForm;
import ch.ututor.controller.service.AuthenticatedUserService;
import ch.ututor.model.User;

@Controller
public class ProfileEditController {
	
	@Autowired 	  AuthenticatedUserService authenticatedUserService;
    
    @RequestMapping(value={"/user/profile/edit"}, method = RequestMethod.GET)
    public ModelAndView edit() {
    	ModelAndView model = new ModelAndView("user/profile-edit");
    	User user = authenticatedUserService.getAuthenticatedUser();
    	model.addObject(user);
    	ProfileEditForm profileEditForm = new ProfileEditForm();
    	model.addObject(profileEditForm);
    	if ( user.getIsTutor() ){
    		profileEditForm.setDescription( user.getDescription() );
    	}
    	return model;
    }
    
    @RequestMapping( value = {"/user/profile/edit"}, method = RequestMethod.POST )
    public ModelAndView edit(@Valid ProfileEditForm profileEditForm, BindingResult result, RedirectAttributes redirectAttributes){
    	ModelAndView model=new ModelAndView("user/profile-edit");
    	if (!result.hasErrors()) {
            try{
            	authenticatedUserService.updateData( profileEditForm );
            	model=new ModelAndView("redirect:/user/profile");
            } catch (FormException e ){
            	model.addObject("exception_message", e.getMessage());
            }
        }
    	return model;
    }   
}