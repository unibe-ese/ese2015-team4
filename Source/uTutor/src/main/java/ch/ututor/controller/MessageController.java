package ch.ututor.controller;
 
import java.util.List;

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
import ch.ututor.controller.service.AuthenticatedUserService;
import ch.ututor.controller.service.MessageService;
import ch.ututor.controller.service.UserService;
import ch.ututor.model.Message;
import ch.ututor.model.User;
 
@Controller
public class MessageController {
     
    @Autowired    UserService userService;
    @Autowired 	  AuthenticatedUserService authenticatedUserService;
    @Autowired	  MessageService messageService;
    
    @RequestMapping(value={"/user/message/new"}, method = RequestMethod.GET)
    public ModelAndView newMessage(@RequestParam(value = "receiverId") Long receiverId) {
        ModelAndView model = new ModelAndView("/user/new-message");
        
        model = messageService.addReceiverNameAndFormToModel( model, receiverId, new NewMessageForm() );
        
        return model;
    }
     
    @RequestMapping(value={"/user/message/new"}, method = RequestMethod.POST)
    public ModelAndView send(@RequestParam(value = "receiverId") Long receiverId, @Valid NewMessageForm newMessageForm, BindingResult result, RedirectAttributes redirectAttributes ){
    	ModelAndView model;
    	User receiver;
    	
    	if ( !result.hasErrors() ){
    		try{
    			receiver = userService.load( receiverId );
    		}catch( UserNotFoundException e ){
    			model = new ModelAndView("exception");
    			model.addObject("exception_message","Receiver not found!");
    			return model;
    		}
    		
    		Message message = messageService.saveMessage( newMessageForm, receiver );
    		receiverId = message.getReceiver().getId();
    		
    		model=new ModelAndView( "redirect:/user/profile?userId=" + receiverId );   		
    	} else {
    		model = new ModelAndView( "/user/new-message");
    		model = messageService.addReceiverNameAndFormToModel(model, receiverId, newMessageForm);
    	}
    	return model;
    }
    
    @RequestMapping(value={"/user/message"}, method = RequestMethod.GET)
    public ModelAndView message( @RequestParam(value = "view", required=false) String view ){
    	User user = authenticatedUserService.getAuthenticatedUser();
    	view = validateView( view );
    	
    	return getMessageView( user, view );
    }
    
    @RequestMapping(value={"/user/message"}, method = RequestMethod.POST)
    public ModelAndView viewOrDelete( @RequestParam(value = "view", required=false) String view , @RequestParam("action") String action, @RequestParam("objectId") String objectId ){
    	User user = authenticatedUserService.getAuthenticatedUser();
    	view = validateView( view );
    	Long messageId = Long.parseLong( objectId );
    	
    	if ( action.equals( "view" ) ){
    		ModelAndView model = new ModelAndView( "/user/view-message" );
    		model.addObject("message", messageService.getMessage( messageId ) );
    		model.addObject("userId", user.getId() );
    		return model;
    	}
    	if ( action.equals( "delete") ){
    		messageService.deleteMessage( messageId, user );
        	return getMessageView( user, view);
    	}
    	
    	ModelAndView model = new ModelAndView( "redirect:/user/message?view=" + view );
    	return model;
    }
    
    private String validateView( String view ){
    	if( view == null ){
    		view = "inbox";
    	}
    	if( !view.equalsIgnoreCase( "inbox" ) && !view.equalsIgnoreCase( "outbox" ) && !view.equalsIgnoreCase( "trash" ) ){
    		view = "inbox";
    	}
    	return view;
    }
    
    private ModelAndView getMessageView( User user, String view ){
    	ModelAndView model = new ModelAndView("/user/message");
    	model.addObject( "view", view );
    	model.addObject( "results", messageService.getMessages( user, view ) );
    	return model;
    }
    
}