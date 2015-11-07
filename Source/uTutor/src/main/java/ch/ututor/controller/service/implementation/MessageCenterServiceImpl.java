package ch.ututor.controller.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.controller.pojos.NewMessageForm;
import ch.ututor.controller.service.AuthenticatedUserLoaderService;
import ch.ututor.controller.service.MessageCenterService;
import ch.ututor.controller.service.UserService;
import ch.ututor.model.Message;
import ch.ututor.model.User;
import ch.ututor.model.dao.MessageDao;
import ch.ututor.controller.exceptions.form.MessageNotFoundException;

@Service
public class MessageCenterServiceImpl implements MessageCenterService{

	@Autowired AuthenticatedUserLoaderService authenticatedUserLoaderService;
	@Autowired UserService userService;
	@Autowired MessageDao messageDao;
	
	public List<Message> getMessagesByView(String view) {
		User user = authenticatedUserLoaderService.getAuthenticatedUser();
		if ( view.equalsIgnoreCase( "outbox" ) ){
			return messageDao.findBySenderAndSenderDeletedOrderByDateAndTimeDesc( user,  false );
		} else if ( view.equalsIgnoreCase( "trash" ) ){
			return messageDao.findBySenderAndSenderDeletedOrReceiverAndReceiverDeletedOrderByDateAndTimeDesc( user,  true,  user,  true);
		} else {
			return messageDao.findByReceiverAndReceiverDeletedOrderByDateAndTimeDesc( user, false );
		}
	}

	
	public long normalizeLong(String longString) {
		try{
			return Long.parseLong(longString);
		}catch(Exception e){
			return 0L;
		}
	}
	
	public String normalizeString(String string) {
		if(string==null){
			return "";
		}
		return string;
	}

	public String normalizeView(String view) {
		view = normalizeString(view);
		if(!view.equals("inbox") && !view.equals("outbox") && !view.equals("trash")){
			return "inbox";
		}
		return view;
	}


	public void deleteMessage(long messageId) {
		User user = authenticatedUserLoaderService.getAuthenticatedUser();
		Message message = messageDao.findById(messageId);
		if( message != null ){
			if ( user.equals( message.getReceiver() ) ){
				message.setReceiverDeleted( true );
			} else if ( user.equals( message.getSender() ) ) {
				message.setSenderDeleted( true );
			}
			message = messageDao.save( message );
		}
	}
	
	public NewMessageForm prefillNewMessageForm(long receiverId) {
		User user = userService.load(receiverId);
		NewMessageForm newMessageForm = new NewMessageForm();
		prefillUserToMessageForm(newMessageForm, user);
		return newMessageForm;
	}


	public NewMessageForm prefillReplyMessageForm(long replyToMessageId) {
		Message message = messageDao.findById(replyToMessageId);
		NewMessageForm newMessageForm = new NewMessageForm();
		if(message==null){
			throw new MessageNotFoundException("Message not found.");
		}
		prefillUserToMessageForm(newMessageForm, message.getSender());
		newMessageForm.setSubject("Re: " + message.getSubject());
		return newMessageForm;
	}
	
	private void prefillUserToMessageForm(NewMessageForm newMessageForm, User user){
		newMessageForm.setReceiverId(user.getId());
		newMessageForm.setReceiverDisplayName(user.getFirstName() + " " + user.getLastName());
	}


	public Message sendMessage(NewMessageForm newMessageForm) {
		User sender = authenticatedUserLoaderService.getAuthenticatedUser();
		User receiver = userService.load(newMessageForm.getReceiverId());
		Message message = new Message();
		message.setReceiver(receiver);
		message.setSender(sender);
		message.setMessage(newMessageForm.getMessage());
		message.setSubject(newMessageForm.getSubject());
		return messageDao.save(message);
	}
}