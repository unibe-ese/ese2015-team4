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

import ch.ututor.controller.exceptions.FormException;
import ch.ututor.controller.pojos.EditForm;
import ch.ututor.controller.pojos.SignUpForm;
import ch.ututor.controller.service.EditService;
import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;

@Controller
public class ProfileController {
	
	@Autowired    UserDao userDao;
	@Autowired EditService editService;
    
    @RequestMapping(value={"/profile"}, method = RequestMethod.GET)
    public ModelAndView profile(@RequestParam("userId") Long userId) {
    	ModelAndView model = new ModelAndView("profile");
    	User user = userDao.findById(userId);
    	model.addObject("user", user);
    	
        return model;
    }
    
    @RequestMapping(value={"/edit"}, method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam("userId") Long userId) {
    	ModelAndView model = new ModelAndView("edit");
    	User user = userDao.findById(userId);
    	model.addObject("user", user);
    	model.addObject("editForm", new EditForm());
    	
        return model;
    }
    
    @RequestMapping( value = {"/edit"}, method = RequestMethod.POST )
    public ModelAndView edit(@RequestParam("userId") Long userId, @Valid EditForm editForm, BindingResult result, RedirectAttributes redirectAttributes){
    	ModelAndView model=new ModelAndView("edit");
    	if (!result.hasErrors()) {
            try{
            	User user = editService.update( editForm, userId);
            	model=new ModelAndView("redirect:/profile?userId="+user.getId());
            } catch (FormException e ){
            	model.addObject("edit_exception", e.getMessage());
            }
        }
    	
    	return model;
    }
    
    

}
