package ch.ututor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.ututor.exceptions.CustomException;
import ch.ututor.service.interfaces.SearchService;
import ch.ututor.utils.ExceptionHelper;

@Controller
public class SearchController {
	
	@Autowired	private SearchService searchService;
	
	/**
	 *	@return		ModelAndView with the search results or an error message if no lectures are found.
	 */
	@RequestMapping( "/search" )
    public ModelAndView search( @RequestParam( value = "query" ) String query,
    		@RequestParam(value = "sort", required=false) String sort) {
		
		ModelAndView model = new ModelAndView( "search" );
    	model.addObject( "query", query );
    	model.addObject( "sort", sort );
    	
    	try {
    		model.addObject( "results", searchService.searchByLecture( query, sort ) );
    	} catch( CustomException e ) {
    		ExceptionHelper.addException( e.getMessage(), model );
    	}
    	
        return model;
    }
}