package ch.ututor.model;
 
import java.util.Date;
 

import javax.persistence.Column;
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
    private Date dateAndTime =  new Date();
     
    @NotNull
    private String subject;
     
    @NotNull
    @Column(length=1048)
    private String message;
    
    private boolean isRead = false;
    private boolean senderDeleted = false;
    private boolean receiverDeleted = false;
 
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
 
    public Date getDateAndTime() {
        return dateAndTime;
    }
 
    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
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
    
    public boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(boolean isRead) {
		this.isRead = isRead;
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