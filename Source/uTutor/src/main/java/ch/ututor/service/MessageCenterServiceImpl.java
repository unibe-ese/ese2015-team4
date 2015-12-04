package ch.ututor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.exceptions.custom.MessageNotFoundException;
import ch.ututor.model.Message;
import ch.ututor.model.User;
import ch.ututor.model.dao.MessageDao;
import ch.ututor.pojos.NewMessageForm;
import ch.ututor.service.interfaces.AuthenticatedUserLoaderService;
import ch.ututor.service.interfaces.MessageCenterService;
import ch.ututor.service.interfaces.UserService;

/**
 *	This class offers methods to display, send and delete messages. It can be used to get the
 *	messages for a specific message center view (inbox, outbox or trash) for the authenticated 
 *	user. Furthermore it offers operations to reply to received messages.   
 *	 
 */

@Service
public class MessageCenterServiceImpl implements MessageCenterService{

	@Autowired 	private AuthenticatedUserLoaderService authenticatedUserLoaderService;
	@Autowired	private UserService userService;
	@Autowired	private MessageDao messageDao;
	
	/**
	 * Returns messages for the logged in user depending on the given view
	 * 
	 * @param view	should not be null. Should be "inbox", "outbox" or "trash", otherwise messages for "inbox" are returned.  
	 */
	public List<Message> getMessagesByView( String view ){
		assert( view != null );
		
		User user = authenticatedUserLoaderService.getAuthenticatedUser();
		if ( view.equalsIgnoreCase( "outbox" ) ){
			return messageDao.findBySenderAndSenderDeletedOrderByDateAndTimeDesc( user,  false );
		} else if ( view.equalsIgnoreCase( "trash" ) ){
			return messageDao.findBySenderAndSenderDeletedOrReceiverAndReceiverDeletedOrderByDateAndTimeDesc( user,  true,  user,  true);
		} else {
			return messageDao.findByReceiverAndReceiverDeletedOrderByDateAndTimeDesc( user, false );
		}
	}

	/**
	 * Attempts to convert a string to long
	 * 
	 * @return	converted long or 0 if conversion fails
	 */
	public long normalizeLong( String longString ) {
		try {
			return Long.parseLong( longString );
		} catch( NumberFormatException e ) {
			return 0L;
		}
	}
	
	/**
	 * Prevents that a string is null
	 * 
	 * @return	Trimmed string if not null, "" otherwise
	 */
	public String normalizeString( String string ) {
		if( string == null ){
			return "";
		}
		return string.trim();
	}

	/**
	 * Prevents that a string is null @see normalizeString
	 * and that the string has a correct value for the message center view.
	 * 
	 * @return the given value, if it equals "inbox", "outbox" or "trash". "inbox" otherwise.
	 */
	public String normalizeView( String view ) {
		view = normalizeString( view );
		if( !view.equals( "inbox" ) && !view.equals( "outbox" ) && !view.equals( "trash" ) ) {
			return "inbox";
		}
		return view;
	}

	/**
	 * Sets the senderDeleted flag if the logged in user is the sender of the message object
	 * or sets the receiverDeleted flag if the logged in user is the receiver of the message object.
	 * 
	 * @param messageId	The id of the message which is to mark as deleted.
	 */
	public Message deleteMessage( long messageId ) {
		User user = authenticatedUserLoaderService.getAuthenticatedUser();
		Message message = messageDao.findById(messageId);
		if( message != null ){
			if ( user.equals( message.getReceiver() ) ) {
				message.setReceiverDeleted( true );
			} else if ( user.equals( message.getSender() ) ) {
				message.setSenderDeleted( true );
			}
			message = messageDao.save( message );
		}
		return message;
	}
	
	/**
	 * Prefills the NewMessageForm.
	 * 
	 * @param receiverId	The id of the user which is the receiver
	 */
	public NewMessageForm prefillNewMessageForm( long receiverId ) {
		User user = userService.load( receiverId );
		NewMessageForm newMessageForm = new NewMessageForm();
		prefillReceiverToMessageForm( newMessageForm, user );
		return newMessageForm;
	}

	/**
	 * Prefills the NewMessageForm as Reply to a message
	 * 
	 * @param replyToMessageId		id of the message to which the reply is.
	 */
	public NewMessageForm prefillReplyMessageForm( long replyToMessageId ) {
		Message message = messageDao.findById( replyToMessageId );
		NewMessageForm newMessageForm = new NewMessageForm();
		if( message == null ){
			throw new MessageNotFoundException( "Message not found." );
		}
		prefillReceiverToMessageForm( newMessageForm, message.getSender() );
		newMessageForm.setSubject( "Re: " + message.getSubject() );
		return newMessageForm;
	}

	/**
	 * Saves the given NewMessageForm as a Message
	 */
	public Message sendMessage( NewMessageForm newMessageForm ) {
		User sender = authenticatedUserLoaderService.getAuthenticatedUser();
		User receiver = userService.load( newMessageForm.getReceiverId() );
		Message message = new Message();
		message.setReceiver( receiver );
		message.setSender( sender );
		message.setMessage( newMessageForm.getMessage() );
		message.setSubject( newMessageForm.getSubject() );
		return sendMessage( message );
	}
	
	//TODO: assert korrekt?
	/**
	 *	@param message	should not be null
	 */
	public Message sendMessage( Message message ) {
		assert( message!= null );
		
		messageDao.save( message );
		return message;
	}
	
	//TODO: assert korrekt?
	/**
	 * @param newMessageForm	should not be null
	 * @param user				sholud not be null
	 */
	private void prefillReceiverToMessageForm( NewMessageForm newMessageForm, User user ){
		assert( newMessageForm != null );
		assert( user != null );
		
		newMessageForm.setReceiverId( user.getId() );
		newMessageForm.setReceiverDisplayName( user.getFirstName() + " " + user.getLastName() );
	}
	
	/**
	 *	Sets the read flag for the message passed as parameter (by id) if the currently logged in user
	 *	is the receiver of the message
	 */
	public Message setRead(long messageId) {
		Message message = messageDao.findById( messageId );
		if( message != null ) {
			User receiver = authenticatedUserLoaderService.getAuthenticatedUser();
			if( message.getReceiver().equals( receiver ) ) {
				message.setIsRead( true );
				messageDao.save( message );
			}
		}
		return message;
	}
	
	/**
	 *	Returns the number of unread messages of the authenticated user
	 */
	public Long getNumberOfNewMessagesForAuthenticatedUser() {
		User user = authenticatedUserLoaderService.getAuthenticatedUser();
		return messageDao.countByReceiverAndIsRead( user, false );
	}
}
