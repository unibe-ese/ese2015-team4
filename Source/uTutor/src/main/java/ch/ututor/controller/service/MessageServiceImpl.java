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

@Service
public class MessageServiceImpl implements MessageService {
	
	@Autowired    
	UserService userService;
	
	@Autowired    
	AuthenticatedUserService authUserService;
	
	@Autowired    
	MessageDao messageDao;
	
	public ModelAndView addReceiverNameAndFormToModel( ModelAndView model, Long receiverId, NewMessageForm newMessageForm ) {
		User receiver;
        
        try{
			receiver = userService.load( receiverId );
			model.addObject( "receiverName" , receiver.getFirstName() + " " + receiver.getLastName());
	        model.addObject( "newMessageForm" , newMessageForm );
		}catch( UserNotFoundException e ){
			model = new ModelAndView("exception");
			model.addObject("exception_message","Receiver not found!");
		}
        
		return model;
	}
	
	public Message saveMessage( NewMessageForm newMessageForm, User receiver ){
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
	
	public List<Message> getMessages( User user, String view ){
		if ( view.equalsIgnoreCase( "inbox" ) ){
			List<Message> messages = messageDao.findByReceiverAndReceiverDeletedOrderByDateAndTimeDesc( user, false );
			return messages;
		}
		if ( view.equalsIgnoreCase( "outbox" ) ){
			List<Message> messages = messageDao.findBySenderAndSenderDeletedOrderByDateAndTimeDesc( user,  false );
			return messages;
		}
		if ( view.equalsIgnoreCase( "trash" ) ){
			List<Message> messages = messageDao.findBySenderAndSenderDeletedOrReceiverAndReceiverDeletedOrderByDateAndTimeDesc( user,  true,  user,  true);		
			return messages;
		}
		return null;
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
	
}
