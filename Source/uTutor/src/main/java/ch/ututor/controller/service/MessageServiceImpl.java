package ch.ututor.controller.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import ch.ututor.controller.exceptions.UserNotFoundException;
import ch.ututor.controller.pojos.NewMessageForm;
import ch.ututor.model.Message;
import ch.ututor.model.User;
import ch.ututor.model.dao.MessageDao;

/**
 * 	This class provides the methods for the message center.
 * 	Amongst others the implementations to send, delete and 
 *	display messages.
 */

@Service
public class MessageServiceImpl implements MessageService {
	
	@Autowired    
	UserService userService;
	
	@Autowired    
	AuthenticatedUserService authUserService;
	
	@Autowired    
	MessageDao messageDao;
	
	/**
	 * 	Adds a receiver name, a correct formated subject and an NewMessageForm to a ModelAndView
	 */
	public ModelAndView addNewMessageDataToModel( ModelAndView model, Long receiverId, String messageSubject, NewMessageForm newMessageForm, boolean hasErrors ) {
		if(messageSubject != null && !hasErrors){
        	messageSubject = "AW:" + messageSubject;
		}
        	
        try{
			User receiver = userService.load( receiverId );
			model.addObject( "receiverName" , receiver.getFirstName() + " " + receiver.getLastName());
	        model.addObject( "newMessageForm" , newMessageForm );
	        model.addObject("messageSubject", messageSubject);
	        return model;
		}catch( UserNotFoundException e ){
			model = new ModelAndView("exception");
			model.addObject("exception_message", "Receiver not found!");
			return model;
		}
	}
	
	public Message sendMessage( NewMessageForm newMessageForm, User receiver ){
		User sender = authUserService.getAuthenticatedUser();
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
		
		return messageDao.save( message );
	}
	
	/**
	 *	Returns the messages of an user for the specified view (inbox, outbox or trash)
	 */
	public List<Message> getMessagesByView( User user, String view ){
		view = validateView( view );
		List<Message> messages = null;
		
		if ( view.equalsIgnoreCase( "inbox" ) ){
			messages = messageDao.findByReceiverAndReceiverDeletedOrderByDateAndTimeDesc( user, false );
		}
		
		if ( view.equalsIgnoreCase( "outbox" ) ){
			messages = messageDao.findBySenderAndSenderDeletedOrderByDateAndTimeDesc( user,  false );

		}
		
		if ( view.equalsIgnoreCase( "trash" ) ){
			messages = messageDao.findBySenderAndSenderDeletedOrReceiverAndReceiverDeletedOrderByDateAndTimeDesc( user,  true,  user,  true);		
		}
		
		return messages;
	}
	
	public void deleteMessage( Long messageId, User user ){
		Message message = messageDao.findById( messageId );
		
		if ( user.equals( message.getReceiver() ) ){
			message.setReceiverDeleted( true );
		} else {
			message.setSenderDeleted( true );
		}
		
		message = messageDao.save( message );
	}
	
	public Message getMessage( Long messageId ){
		return messageDao.findById( messageId );
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
