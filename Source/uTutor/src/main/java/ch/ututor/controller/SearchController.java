package ch.ututor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.ututor.controller.exceptions.CustomException;
import ch.ututor.controller.service.ExceptionService;
import ch.ututor.controller.service.SearchService;

/**
 *	This class handles the search requests.
 */

@Controller
public class SearchController {
	
	@Autowired	SearchService searchService;
	@Autowired	ExceptionService exceptionService;
	
	/**
	 *	@return			A ModelAndView with the search results or an error message
	 *					if no lectures are found.
	 */
	@RequestMapping("/search")
    public ModelAndView search(@RequestParam(value = "query") String query) {
		
		ModelAndView model = new ModelAndView("search");
    	model.addObject("query", query);
    	
    	try{
    		model.addObject("results", searchService.searchByLecture(query));
    	}catch(CustomException e){
    		exceptionService.addException( model, e.getMessage() );
    	}
    	
        return model;
    }
}