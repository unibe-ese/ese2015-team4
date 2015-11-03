package ch.ututor.controller.service;

import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import ch.ututor.controller.pojos.NewMessageForm;
import ch.ututor.model.Message;
import ch.ututor.model.User;

public interface MessageService {
	
	public ModelAndView addFormToModel( ModelAndView model, Long receiverId, String messageSubject, NewMessageForm newMessageForm, boolean hasErrors);
	
	public Message saveMessage( NewMessageForm newMessageForm, User receiver );
	
	public List<Message> getMessages( User user, String view );
	
	public Message getMessage( Long messageId );
	
	public void deleteMessage( Long messageId, User user );
	
}