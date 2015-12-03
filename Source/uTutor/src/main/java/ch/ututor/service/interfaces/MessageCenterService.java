package ch.ututor.service.interfaces;

import java.util.List;

import ch.ututor.model.Message;
import ch.ututor.pojos.NewMessageForm;

public interface MessageCenterService {
	public List<Message> getMessagesByView(String view);
	public long normalizeLong(String longString);
	public String normalizeString(String string);
	public String normalizeView(String view);
	public Message deleteMessage(long messageId);
	public NewMessageForm prefillNewMessageForm(long receiverId);
	public NewMessageForm prefillReplyMessageForm(long replyToMessageId);
	public Message sendMessage(NewMessageForm newMessageForm);
	public Message sendMessage(Message message);
	public Message setRead(long messageId);
	public Long getNumberOfNewMessagesForAuthenticatedUser();
}
