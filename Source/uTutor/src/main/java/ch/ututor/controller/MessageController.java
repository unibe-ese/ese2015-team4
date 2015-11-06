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
import ch.ututor.controller.exceptions.form.MessageNotFoundException;
import ch.ututor.controller.pojos.NewMessageForm;
import ch.ututor.controller.service.AuthenticatedUserLoaderService;
import ch.ututor.controller.service.ExceptionService;
import ch.ututor.controller.service.MessageService;
import ch.ututor.controller.service.UserService;
import ch.ututor.model.User;

/**
 *	This class handles requests concerning the message center and returns the
 *	needed views by using the MessageService. 
 */

@Controller
public class MessageController {
     
    @Autowired    UserService userService;
    @Autowired 	  AuthenticatedUserLoaderService authenticatedUserLoaderService;
    @Autowired	  MessageService messageService;
    @Autowired	  ExceptionService exceptionService;
    
    /**
     * Returns a newMessageForm to send a message to the desired receiver with
     * the desired subject.
     */
    @RequestMapping(value={"/user/message/new"}, method = RequestMethod.GET)
    public ModelAndView newMessage(@RequestParam(value = "receiverId") Long receiverId, @RequestParam(value = "messageSubject", required = false) String messageSubject) {
        ModelAndView model = new ModelAndView("/user/new-message");
        model = messageService.addNewMessageDataToModel( model, receiverId, messageSubject, new NewMessageForm(), false );
        return model;
    }
    
    /**
     *	Sends the message entered in the newMessageForm to the designated receiver.
     *
     *	@return	an error page, the profile of the receiver or again the 
     *			newMessageForm if it isn't complete.
     */
    @RequestMapping(value={"/user/message/new"}, method = RequestMethod.POST)
    public ModelAndView sendMessage(@RequestParam(value = "receiverId") Long receiverId, @Valid NewMessageForm newMessageForm, BindingResult result, RedirectAttributes redirectAttributes ){
    	
    	ModelAndView model;
    	User receiver;
    	
    	if ( !result.hasErrors() ){
    		
    		try{
    			receiver = userService.load( receiverId );
    		}catch( UserNotFoundException e ){
    			return exceptionService.addException( null, "Receiver not found!");
    		}
    		
    		messageService.sendMessage( newMessageForm, receiver );
    		model = new ModelAndView( "redirect:/user/profile?userId=" + receiver.getId() );   		
    	
    	} else {
    		model = new ModelAndView( "/user/new-message");
    		model = messageService.addNewMessageDataToModel(model, receiverId, newMessageForm.getSubject(), newMessageForm, result.hasErrors());
    	}
    	return model;
    }
    
    /**
     *	Displays the message center with the desired view (inbox, outbox or trash)
     */
    @RequestMapping(value={"/user/message"}, method = RequestMethod.GET)
    public ModelAndView displayMessageCenter( @RequestParam(value = "view", required=false) String view ){
    	User user = authenticatedUserLoaderService.getAuthenticatedUser();
    	return messageService.getMessagesByView( user, view );
    }
    
    /**
     * @param action	needs to be either 'delete' or 'view'
     * @param objectId	must be a valid Long
     * 
     * @throws NumberFormatException if objectId is not a valid Long
     * 
     * @return	An error page if the messageId doesn't exist. Otherwise the desired message
     * 			or the previous message center view.
     */
    @RequestMapping(value={"/user/message"}, method = RequestMethod.POST)
    public ModelAndView viewOrDeleteMessage( @RequestParam(value = "view", required=false) String view , @RequestParam("action") String action, @RequestParam("objectId") String objectId ){
    	assert( action.equals("delete") || action.equals("show"));
    	
    	User user = authenticatedUserLoaderService.getAuthenticatedUser();
    	view = messageService.validateView( view );
    	Long messageId = Long.parseLong( objectId );
    	
    	if ( action.equals( "show" ) ){
    		
    		try{
    			messageService.getMessageByMessageId( messageId );
    		} catch ( MessageNotFoundException e ){
    			return exceptionService.addException( null, e.getMessage() );
    		}
    		
    		ModelAndView model = new ModelAndView( "/user/view-message" );
    		model.addObject("message", messageService.getMessageByMessageId( messageId ) );
    		model.addObject("userId", user.getId() );
    		return model;
    		
    	} else {
    		messageService.deleteMessage( messageId, user );
        	return messageService.getMessagesByView( user, view);
    	}
    }
    
}