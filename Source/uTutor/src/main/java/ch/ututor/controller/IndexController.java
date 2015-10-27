package ch.ututor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ch.ututor.controller.pojos.SignUpForm;

@Controller
public class IndexController {
	
	@RequestMapping("/")
    public ModelAndView index() {
    	ModelAndView model = new ModelAndView("index");
    	model.addObject("signUpForm", new SignUpForm() );
        return model;
    }
}