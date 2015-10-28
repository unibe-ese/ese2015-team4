package ch.ututor.controller.pojos;
 
import org.hibernate.validator.constraints.NotBlank;
 
import ch.ututor.model.User;
 
public class NewMessageForm {
    @NotBlank(message = "Please enter a subject!")
    private String subject;
     
    @NotBlank(message = "Please enter a message body!")
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
     
}