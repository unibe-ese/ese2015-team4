package ch.ututor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ch.ututor.controller.pojos.LoginForm;
import ch.ututor.controller.pojos.SignUpForm;

@Controller
public class IndexController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
    	ModelAndView model = new ModelAndView("index");
    	model.addObject("signUpForm", new SignUpForm() );
    	model.addObject("loginForm", new LoginForm() );
        return model;
    }
}
