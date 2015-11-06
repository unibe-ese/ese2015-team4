package ch.ututor.controller.service;

import org.springframework.web.servlet.ModelAndView;

import ch.ututor.controller.pojos.NewMessageForm;
import ch.ututor.model.Message;
import ch.ututor.model.User;

public interface MessageService {
	
	public ModelAndView addNewMessageDataToModel( ModelAndView model, Long receiverId, String messageSubject, NewMessageForm newMessageForm, boolean hasErrors);
	
	public void sendMessage( NewMessageForm newMessageForm, User receiver );
	
	public ModelAndView getMessagesByView( User user, String view );
	
	public Message getMessageByMessageId( Long messageId );
	
	public void deleteMessage( Long messageId, User user );
	
	public String validateView( String view );
	
}
