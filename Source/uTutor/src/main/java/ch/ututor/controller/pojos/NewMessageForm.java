package ch.ututor.controller.pojos;
 
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
 
public class NewMessageForm {
    
	//TODO: check correct display of long subjects and user names in message-view
	
	private long receiverId;
	private String receiverDisplayName;
	
	@NotBlank(message = "Please enter a subject!")
	@Size(max = 50, message = "No more than 50 characters allowed!")
	private String subject;
     
    @NotBlank(message = "Please enter a message body!")
    @Size(max = 1000, message = "No more than 1000 characters allowed!")
    private String message;
 
    public String getSubject() {
        return subject;
    }
 
    public void setSubject(String subject) {
        this.subject = subject;
    }
 
    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }

	public long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(long receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverDisplayName() {
		return receiverDisplayName;
	}

	public void setReceiverDisplayName(String receiverDisplayName) {
		this.receiverDisplayName = receiverDisplayName;
	}
     
}