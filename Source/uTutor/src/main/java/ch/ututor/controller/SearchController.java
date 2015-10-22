package ch.ututor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SearchController {
	@RequestMapping("/search")
    public ModelAndView search(@RequestParam(value = "query") String query) {
    	ModelAndView model = new ModelAndView("search");
    	model.addObject("query", query);
        return model;
    }
	
}
