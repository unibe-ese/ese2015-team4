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

import ch.ututor.controller.exceptions.UserNotFoundException;
import ch.ututor.controller.pojos.NewMessageForm;
import ch.ututor.controller.pojos.SignUpForm;
import ch.ututor.controller.service.AuthenticatedUserService;
import ch.ututor.controller.service.UserService;
import ch.ututor.model.User;
 
@Controller
public class MessageController {
     
    @Autowired    UserService userService;
    @Autowired 	  AuthenticatedUserService authenticatedUserService;
    
     
    @RequestMapping(value={"/user/message/new"}, method = RequestMethod.GET)
    public ModelAndView newMessage(@RequestParam(value = "receiverId") Long receiverId) {
        ModelAndView model = new ModelAndView("/user/new-message");
        
        addReceiverNameAndFormToModel(model, receiverId, new NewMessageForm());
        
        return model;
    }
     
    @RequestMapping(value={"/user/message/new"}, method = RequestMethod.POST)
    public ModelAndView send(@RequestParam(value = "receiverId") Long receiverId, @Valid NewMessageForm newMessageForm, BindingResult result, RedirectAttributes redirectAttributes ){
    	ModelAndView model;
    	
    	if ( !result.hasErrors() ){
    		//TODO: get the receiver over receiverId. Create/Save message.
    		
    		
    		//TODO: decide where to redirect after message is send
    		model=new ModelAndView("redirect:/user/profile/");   		
    	} else {
    		model = new ModelAndView( "/user/new-message");
    		addReceiverNameAndFormToModel(model, receiverId, newMessageForm);
    	}
    	return model;
    }
    
    
    
    
    //TODO: DAS HIER GEHÖRT WOHL EHER IN EINEN SERVICE
    private void addReceiverNameAndFormToModel(ModelAndView model, Long receiverId, NewMessageForm newMessageForm){
        User receiver;
        
        if ( receiverId == null ){
        	model = new ModelAndView("exception");
			model.addObject("exception_message","Receiver not found!");
        }
        
        try{
			receiver = userService.load( receiverId );
			model.addObject("receiverName",receiver.getFirstName() + " " + receiver.getLastName());
	        model.addObject("newMessageForm", newMessageForm );
		}catch(UserNotFoundException e){
			model = new ModelAndView("exception");
			model.addObject("exception_message","Receiver not found!");
		}
    }
}