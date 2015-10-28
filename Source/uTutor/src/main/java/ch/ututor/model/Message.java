package ch.ututor.model;
 
import java.util.Date;
 
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
 
@Entity
public class Message {
     
    @Id
    @GeneratedValue
    private Long id;
     
    @ManyToOne
    private User sender;
     
    @ManyToOne
    private User receiver;
     
    @NotNull
    private Date dateTime;
     
    @NotNull
    private String subject;
     
    @NotNull
    private String message;
    
    private boolean read;
    private boolean senderDeleted;
    private boolean receiverDeleted;
 
	public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public User getSender() {
        return sender;
    }
 
    public void setSender(User sender) {
        this.sender = sender;
    }
 
    public User getReceiver() {
        return receiver;
    }
 
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
 
    public Date getDateTime() {
        return dateTime;
    }
 
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
 
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
    
    public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isSenderDeleted() {
		return senderDeleted;
	}

	public void setSenderDeleted(boolean senderDeleted) {
		this.senderDeleted = senderDeleted;
	}

	public boolean isReceiverDeleted() {
		return receiverDeleted;
	}

	public void setReceiverDeleted(boolean receiverDeleted) {
		this.receiverDeleted = receiverDeleted;
	}
    
}