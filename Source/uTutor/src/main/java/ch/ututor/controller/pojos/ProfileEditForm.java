package ch.ututor.controller.pojos;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class ProfileEditForm {	
	
	@NotBlank(message = "Please enter your first name!")
	private String firstName;
	
	@NotBlank(message = "Please enter your last name!")
	private String lastName;
	
	//TODO: check correct display of long description 
	//		-> entire profile information is moved down if the description is too long
	private String description;
	
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
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
}