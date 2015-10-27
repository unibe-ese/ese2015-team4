package ch.ututor.controller.pojos;

import org.hibernate.validator.constraints.NotBlank;

public class ProfileEditForm {	
	
	@NotBlank(message = "Please enter your first name!")
	private String firstName;
	
	@NotBlank(message = "Please enter your last name!")
	private String lastName;
	
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