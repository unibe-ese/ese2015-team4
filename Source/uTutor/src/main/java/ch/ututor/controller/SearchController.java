package ch.ututor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.ututor.controller.exceptions.FormException;
import ch.ututor.controller.service.SearchService;

@Controller
public class SearchController {
	@Autowired    SearchService searchService;
	@RequestMapping("/search")
    public ModelAndView search(@RequestParam(value = "query") String query) {
    	ModelAndView model = new ModelAndView("search");
    	model.addObject("query", query);
    	try{
    		model.addObject("results", searchService.searchByLecture(query));
    	}catch(FormException e){
    		model.addObject("exception_message", e.getMessage());
    	}
        return model;
    }
	
}
