package ch.ututor.controller.service;

import java.util.List;

import ch.ututor.controller.pojos.NewMessageForm;
import ch.ututor.model.Message;

public interface MessageCenterService {
	public List<Message> getMessagesByView(String view);
	public long normalizeLong(String longString);
	public String normalizeString(String string);
	public String normalizeView(String view);
	public void deleteMessage(long messageId);
	public NewMessageForm prefillNewMessageForm(long receiverId);
	public NewMessageForm prefillReplyMessageForm(long replyToMessageId);
	public Message sendMessage(NewMessageForm newMessageForm);
}
