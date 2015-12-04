package ch.ututor.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.ututor.exceptions.CustomException;
import ch.ututor.exceptions.custom.MessageNotFoundException;
import ch.ututor.exceptions.custom.UserNotFoundException;
import ch.ututor.pojos.NewMessageForm;
import ch.ututor.service.interfaces.AuthenticatedUserLoaderService;
import ch.ututor.service.interfaces.MessageCenterService;
import ch.ututor.utils.ExceptionHelper;
import ch.ututor.utils.FlashMessage;

@Controller
public class MessageCenterController {
	
	@Autowired private MessageCenterService messageCenterService;
	@Autowired private AuthenticatedUserLoaderService authenticatedUserLoaderService;
	
	/**
	 * Adds the messages corresponding to the view parameter.
	 * 
	 * @param view			should be "inbox", "outbox" or "trash". Otherwise the default ("inbox") will be used.
	 * @param messageId 	id of the message for which the setRead will be called.
	 * @return 				A ModelAndView with a List of Message objects corresponding to the view parameter.
	 */
	@RequestMapping( value={"/user/messagecenter"}, method = RequestMethod.GET )
    public ModelAndView displayMessageCenter(	@RequestParam( value = "view", required = false ) String view,
    										@RequestParam( value = "messageId", required = false ) String messageIdString ) {
		
		messageCenterService.setRead( messageCenterService.normalizeLong( messageIdString ) );
		view = messageCenterService.normalizeView( view );
        ModelAndView model = new ModelAndView( "user/messagecenter" );
        model.addObject( "messageList", messageCenterService.getMessagesByView( view ) );
        model.addObject( "userId", authenticatedUserLoaderService.getAuthenticatedUser().getId());
        return model;
    }
	
	/**
	 * Delete message by id
	 * 
	 * @param messageId			the id of the message which should be deleted (posted as objectId).
	 * @param viewString		the view which will be called after action handling
	 * @param showString		the index of the message which will be displayed after action handling.
	 * @return 					ModelAndView with a redirect to the message center.
	 */
	@RequestMapping( value="/user/messagecenter", method = RequestMethod.POST, params = "action=delete" )
	public ModelAndView deleteMessage( 	@RequestParam( value = "action" ) String actionString,
												@RequestParam( value = "objectId" ) Long messageId,
												@RequestParam( value = "view", required = false ) String viewString,
												@RequestParam( value = "show", required = false ) String showString,
												final RedirectAttributes redirectAttributes) {
			
		String view = messageCenterService.normalizeView( viewString );
		long show = messageCenterService.normalizeLong( showString );
		
		messageCenterService.deleteMessage( messageId );
		FlashMessage.addMessage(redirectAttributes, "Message successfully deleted.", FlashMessage.Type.SUCCESS);
		
		return new ModelAndView( "redirect:/user/messagecenter/?view=" + view + "&show=" + show );
	}
	
	/**
	 * Prepares and adds the form for sending a new message to the ModelAndView
	 * 
	 * @param 	receiverIdString	id of the receiver
	 * @return 	ModelAndView for sending a new message or ModelAndView with an exception message.
	 */
	@RequestMapping( value={"/user/messagecenter/new"}, method = RequestMethod.GET )
    public ModelAndView displayNewMessageForm( 	@RequestParam(value = "receiverId") String receiverIdString,
    											HttpServletRequest request ) {
		
		long receiverId = messageCenterService.normalizeLong( receiverIdString );
        try {
            ModelAndView model = new ModelAndView( "user/new-message" );
        	model.addObject( "newMessageForm" , messageCenterService.prefillNewMessageForm( receiverId ));
        	return model;
        } catch( CustomException e ) {
        	return ExceptionHelper.addException( e.getMessage() );
        }
    }
	
	/**
	 * Prepares and adds the form for replying to a message.
	 * 
	 * @param 	messageIdString		id of the message to which shall be replied.
	 * @return 	ModelAndView for sending a reply to a message or ModelAndView with an exception message.
	 */
	@RequestMapping( value={"/user/messagecenter/reply"}, method = RequestMethod.GET )
    public ModelAndView displayReplyToMessageForm( @RequestParam(value = "replyToMessageId" ) String messageIdString,
    											HttpServletRequest request ) {
		long messageId = messageCenterService.normalizeLong( messageIdString );
        try {
            ModelAndView model = new ModelAndView( "user/new-message" );
        	model.addObject( "newMessageForm" , messageCenterService.prefillReplyMessageForm( messageId ) );
        	return model;
        } catch( MessageNotFoundException e ) {
        	return ExceptionHelper.addException( e.getMessage() );
        } catch( UserNotFoundException e ) {
        	return ExceptionHelper.addException( e.getMessage() );
        }
    }
	
	/**
	 * @return 	ModelAndView with the form attached if form validation was not successful.
	 *  		ModelAndView with an exception message sending was not successful.
	 *  		ModelAndView redirect to the message center if message sending was successful.
	 */
	@RequestMapping( value={"/user/messagecenter/new", "/user/messagecenter/reply"}, method = RequestMethod.POST )
    public ModelAndView sendMessage( 	@Valid NewMessageForm newMessageForm, 
    												BindingResult result,
    												HttpServletRequest request,
    												final RedirectAttributes redirectAttributes ) {
		if ( !result.hasErrors() ) {
			try {
				messageCenterService.sendMessage( newMessageForm );
				FlashMessage.addMessage(redirectAttributes, "Message successfully sent.", FlashMessage.Type.SUCCESS);
				return new ModelAndView( "redirect:/user/messagecenter/?view=outbox" );
			} catch( UserNotFoundException e ) {
		        return ExceptionHelper.addException( e.getMessage() );
		    }
		}
		
		ModelAndView model = new ModelAndView( "user/new-message" );
		model.addObject(newMessageForm);
		return model;
    }
	
	/**
	 * Counts the number of unread mails for the authenticated user 
	 * 
	 * @return A string containing the number of unread mails 
	 */
	@RequestMapping( value={"/user/num-new-messages"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public String getNumberOfNewMessagesForAuthenticatedUser(){
		return messageCenterService.getNumberOfNewMessagesForAuthenticatedUser().toString();
	}

}
