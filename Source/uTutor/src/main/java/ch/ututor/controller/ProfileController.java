package ch.ututor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProfileController {
    
    @RequestMapping(value={"/profile"}, method = RequestMethod.GET)
    public ModelAndView profile(@RequestParam("userId") Long userId) {
    	ModelAndView model = new ModelAndView("profile");
    	
        return model;
    }

}
