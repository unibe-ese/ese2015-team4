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
import ch.ututor.controller.pojos.BecomeTutorForm;
import ch.ututor.controller.service.AuthenticatedUserService;
import ch.ututor.controller.service.TutorService;
import ch.ututor.model.User;

@Controller
public class TutorController {
	
	@Autowired TutorService tutorService;
	@Autowired AuthenticatedUserService authenticatedUserService;
	
	@RequestMapping(value={"/user/become-tutor"}, method = RequestMethod.GET)
    public ModelAndView becomeTutor() {
		if(authenticatedUserService.getAuthenticatedUser().getIsTutor()){
			return new ModelAndView("redirect:/user/profile");
		}
    	ModelAndView model = new ModelAndView("become-tutor");
    	model.addObject("becomeTutorForm", new BecomeTutorForm() );
        return model;
    }
	
	@RequestMapping(value={"/user/become-tutor"}, method = RequestMethod.POST)
    public ModelAndView becomeTutor(@Valid BecomeTutorForm becomeTutorForm, BindingResult result, RedirectAttributes redirectAttributes) {
		if(authenticatedUserService.getAuthenticatedUser().getIsTutor()){
			return new ModelAndView("redirect:/user/profile");
		}
    	ModelAndView model = new ModelAndView("become-tutor");
		if (!result.hasErrors()) {
			try{
				tutorService.saveForm( becomeTutorForm );
				return new ModelAndView("redirect:/user/profile");
			} catch ( FormException e ){
				model.addObject("exception_message", e.getMessage());
			}
        }
	
    	return model;
    }
	
}