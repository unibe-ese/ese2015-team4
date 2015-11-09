package ch.ututor.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.ututor.controller.pojos.NewMessageForm;
import ch.ututor.controller.service.ExceptionService;
import ch.ututor.controller.service.MessageCenterService;
import ch.ututor.controller.exceptions.UserNotFoundException;
import ch.ututor.controller.exceptions.form.MessageNotFoundException;

@Controller
public class MessageCenterController {
	@Autowired MessageCenterService messageCenterService;
	@Autowired ExceptionService exceptionService;
	
	/**
	 * Adds the messages corresponding to the view parameter.
	 * 
	 * @param view	should be "inbox", "outbox", "trash" otherwise the default ("inbox") will be used.
	 * @param messageId id of the message for wich the setRead will be called.
	 * @return A ModelAndView Object with a List<Message> attached, corresponding to the view parameter.
	 */
	@RequestMapping( value={"/user/messagecenter"}, method = RequestMethod.GET )
    public ModelAndView messageCenterView(	@RequestParam( value = "view", required = false ) String view,
    										@RequestParam( value = "messageId", required = false ) String messageIdString ) {
		messageCenterService.setRead( messageCenterService.normalizeLong( messageIdString ) );
		view = messageCenterService.normalizeView( view );
        ModelAndView model = new ModelAndView( "user/messagecenter" );
        model.addObject( "messageList", messageCenterService.getMessagesByView( view ) );
        return model;
    }
	
	/**
	 * General POST action handler
	 * 
	 * @param actionString		should be "delete" otherwise no further action is handled.
	 * @param objectIdString	if action is "delete" this should be a messageId of the message which should be deleted.
	 * @param viewString		the view which will be called after action handling.
	 * @param showString		the index of the message which will be displayed after action handling.
	 * @return A ModelAndView redirect to the message center.
	 */
	@RequestMapping( value="/user/messagecenter", method = RequestMethod.POST )
	public ModelAndView messageCenterAction(
			@RequestParam( value = "action" ) String actionString,
			@RequestParam( value = "objectId" ) String objectIdString,
			@RequestParam( value = "view", required = false ) String viewString,
			@RequestParam( value = "show", required = false ) String showString){
			
		String action = messageCenterService.normalizeString( actionString );
		long objectId = messageCenterService.normalizeLong( objectIdString );
		String view = messageCenterService.normalizeView( viewString );
		long show = messageCenterService.normalizeLong( showString );
		
		if( action.equals( "delete" ) ){
			messageCenterService.deleteMessage( objectId );
		}
		
		return new ModelAndView("redirect:/user/messagecenter/?view="+view+"&show="+show);
	}
	
	/**
	 * Populates and adds the Form to the ModelAndView for sending a new message.
	 * 
	 * @param receiverIdString	The id of the User Object which is the receiver.
	 * @return A ModelAndView for sending a new message or a ModelAndView with an exception message.
	 */
	@RequestMapping( value={"/user/messagecenter/new"}, method = RequestMethod.GET )
    public ModelAndView messageCenterNewView( 	@RequestParam(value = "receiverId") String receiverIdString,
    											HttpServletRequest request) {
		long receiverId = messageCenterService.normalizeLong( receiverIdString );
        try{
            ModelAndView model = new ModelAndView( "user/new-message" );
        	model.addObject( "newMessageForm" , messageCenterService.prefillNewMessageForm( receiverId ));
        	return model;
        }catch( UserNotFoundException e ){
        	return exceptionService.addException( null, e.getMessage() );
        }
    }
	
	/**
	 * Populates and adds the Form to the ModelAndView for sending a reply to a message.
	 * 
	 * @param messageIdString	The id of the Message Object to which the reply is.
	 * @return A ModelAndView for sending a reply to a message or a ModelAndView with an exception message.
	 */
	@RequestMapping( value={"/user/messagecenter/reply"}, method = RequestMethod.GET )
    public ModelAndView messageCenterReplyView( @RequestParam(value = "replyToMessageId" ) String messageIdString,
    											HttpServletRequest request) {
		long messageId = messageCenterService.normalizeLong( messageIdString );
        try{
            ModelAndView model = new ModelAndView( "user/new-message" );
        	model.addObject( "newMessageForm" , messageCenterService.prefillReplyMessageForm( messageId ) );
        	return model;
        }catch( MessageNotFoundException e ){
        	return exceptionService.addException( null, e.getMessage() );
        }catch( UserNotFoundException e ){
        	return exceptionService.addException( null, e.getMessage() );
        }
    }
	
	/**
	 * Handles new messages and reply to messages POSTs
	 * 
	 * @return 	ModelAndView with the form attached if form validation was not successful.
	 *  		ModelAndView with an exception message center if message sending was not successful.
	 *  		ModelAndView redirect to the message center if message sending was successful.
	 */
	@RequestMapping( value={"/user/messagecenter/new", "/user/messagecenter/reply"}, method = RequestMethod.POST )
    public ModelAndView messageCenterMessageSave( 	@Valid NewMessageForm newMessageForm, 
    												BindingResult result,
    												HttpServletRequest request) {
		if ( !result.hasErrors() ){
			try{
				messageCenterService.sendMessage( newMessageForm );
				return new ModelAndView( "redirect:/user/messagecenter/?view=outbox" );
			}catch( UserNotFoundException e ){
		        return exceptionService.addException( null, e.getMessage() );
		    }
		}
		ModelAndView model = new ModelAndView( "user/new-message" );
		model.addObject(newMessageForm);
		return model;
    }
}
