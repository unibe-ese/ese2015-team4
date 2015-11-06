package ch.ututor.controller.service.implementation;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import ch.ututor.controller.exceptions.UserNotFoundException;
import ch.ututor.controller.exceptions.form.MessageNotFoundException;
import ch.ututor.controller.pojos.NewMessageForm;
import ch.ututor.controller.service.AuthenticatedUserLoaderService;
import ch.ututor.controller.service.ExceptionService;
import ch.ututor.controller.service.MessageService;
import ch.ututor.controller.service.UserService;
import ch.ututor.model.Message;
import ch.ututor.model.User;
import ch.ututor.model.dao.MessageDao;

/**
 * 	This class provides the methods for the message center.
 *  Implementations to send, delete and display messages.
 */

@Service
public class MessageServiceImpl implements MessageService {
	
	@Autowired    
	UserService userService;
	
	@Autowired    
	AuthenticatedUserLoaderService authenticatedUserLoaderService;
	
	@Autowired    
	MessageDao messageDao;
	
	@Autowired
	ExceptionService exceptionService;
	
	/**
	 * 	Adds a receiver name, a correct formated subject and an NewMessageForm to a ModelAndView
	 *	
	 *	@param model			should not be null
	 *	@param newMessageForm	should not be null
	 *
	 *	@return model	A ModelAndView which contains the passed information or 
	 *					displays an error page if the receiver isn't found. 
	 */
	public ModelAndView addNewMessageDataToModel( ModelAndView model, Long receiverId, String messageSubject, NewMessageForm newMessageForm, boolean hasErrors ) {
		assert ( model != null );
		assert ( newMessageForm != null );
		
		if( messageSubject != null && !hasErrors ){
        	messageSubject = "AW:" + messageSubject;
		}
        	
        try{
			User receiver = userService.load( receiverId );
			model.addObject( "receiverName" , receiver.getFirstName() + " " + receiver.getLastName() );
	        model.addObject( "newMessageForm" , newMessageForm );
	        model.addObject("messageSubject", messageSubject );
	        return model;
		} catch ( UserNotFoundException e ){
			return exceptionService.addException( null, "Receiver not found!" );
		}
	}
	
	/**
	 *	@param newMessageForm	should not be null
	 *	@param receiver			should not be null
	 */
	public void sendMessage( NewMessageForm newMessageForm, User receiver ){
		assert( newMessageForm != null );
		assert( receiver != null );
		
		User sender = authenticatedUserLoaderService.getAuthenticatedUser();
		Date dateTime = new Date();
		
		Message message = new Message();
		message.setSender( sender );
		message.setReceiver( receiver );
		message.setDateAndTime( dateTime );
		message.setSubject( newMessageForm.getSubject() );
		message.setMessage( newMessageForm.getMessage() );
		message.setIsRead( false );
		message.setSenderDeleted( false );
		message.setReceiverDeleted( false );
		
		messageDao.save( message );
	}
	
	/**
	 *	Returns the messages of an user for the specified view (inbox, outbox or trash)
	 *	
	 *	@param user		should not be null
	 */
	public ModelAndView getMessagesByView( User user, String view ){
		assert( user != null );
		
		view = validateView( view );
		ModelAndView model = new ModelAndView( "/user/message" );
		model.addObject( "view", view );
		
		if ( view.equalsIgnoreCase( "outbox" ) ){
			model.addObject( "results", messageDao.findBySenderAndSenderDeletedOrderByDateAndTimeDesc( user,  false ) );
		} else if ( view.equalsIgnoreCase( "trash" ) ){
			model.addObject( "results", messageDao.findBySenderAndSenderDeletedOrReceiverAndReceiverDeletedOrderByDateAndTimeDesc( user,  true,  user,  true) );
		} else {
			model.addObject( "results", messageDao.findByReceiverAndReceiverDeletedOrderByDateAndTimeDesc( user, false ) );		
		}
		
		return model;
	}
	
	/**
	 *	@param user			should not be null
	 */
	public void deleteMessage( Long messageId, User user ){
		assert( user != null );
		
		Message message = messageDao.findById( messageId );
		
		if( message != null ){
			if ( user.equals( message.getReceiver() ) ){
				message.setReceiverDeleted( true );
			} else {
				message.setSenderDeleted( true );
			}
			message = messageDao.save( message );
		}
	}
	
	/**
	 *	@param messageId	should not be null
	 *	
	 *	@throws MessageNotFoundException	if the message with the required id doesn't exist
	 */
	public Message getMessageByMessageId( Long messageId ){
		assert( messageId != null );
		
		Message message = messageDao.findById( messageId );
		
		if ( message == null ){
			throw new MessageNotFoundException("Message not found!");
		}
		
		return message;
	}
	
	public String validateView( String view ){
    	if( view == null ){
    		return "inbox";
    	}
    	
    	if( !view.equalsIgnoreCase( "inbox" ) && !view.equalsIgnoreCase( "outbox" ) && !view.equalsIgnoreCase( "trash" ) ){
    		return "inbox";
    	}
    	
    	return view;
    }
	
}
