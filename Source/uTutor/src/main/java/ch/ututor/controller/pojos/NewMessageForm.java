package ch.ututor.controller.pojos;

import org.hibernate.validator.constraints.NotBlank;
 
public class NewMessageForm {
	
	private long receiverId;
	private String receiverDisplayName;
	
	@NotBlank( message = "Please enter a subject!" )
	private String subject;
     
    @NotBlank( message = "Please enter a message body!" )
    private String message;
 
    public String getSubject() {
        return subject;
    }
 
    public void setSubject( String subject ) {
        this.subject = subject;
    }
 
    public String getMessage() {
        return message;
    }
 
    public void setMessage( String message ) {
        this.message = message;
    }

	public long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId( long receiverId ) {
		this.receiverId = receiverId;
	}

	public String getReceiverDisplayName() {
		return receiverDisplayName;
	}

	public void setReceiverDisplayName( String receiverDisplayName ) {
		this.receiverDisplayName = receiverDisplayName;
	}
}