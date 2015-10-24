package ch.ututor.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import ch.ututor.controller.exceptions.NoLecturesFoundException;
import ch.ututor.controller.service.TutorService;
import ch.ututor.controller.service.UserService;

@Entity
public class User{
	@Id
	@GeneratedValue
	private Long id;
	
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	@Column(length=512000)
	private byte[] profilePic;
	
	private boolean isTutor;
	private String description;	
	private float price;
	
	
	public Long getId(){
		return id;
	}
	
	public void setId( Long id ){
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		this.password = passwordEncoder.encode(password);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
    public byte[] getProfilePic() {
        return profilePic;
    }
 
    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }
    public boolean hasProfilePic(){
    	if(profilePic==null){
    		return false;
    	}else{
    		return true;
    	}
    }
    
    public void setIsTutor(boolean isTutor){
    	this.isTutor=isTutor;
    }
    
    public boolean getIsTutor(){
    	return isTutor;
    }

    public String getDescription(){
    	if(getIsTutor()){
    		if(description==null){
    			description="";
    		}
    		return description;
    	}
    	return null;
    }
    
    public String getDescriptionHtml(){
    	return getDescription().replace("\n", "<br/>");
    }
    
    public void setDescription(String description){
    	this.description = description;
    }
    
    public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	
}
