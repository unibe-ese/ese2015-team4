package ch.ututor.controller.pojos;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class BecomeTutorForm extends AddLectureForm {
	
	@NotBlank(message = "Please enter a valid price!")
	@Size(max = 8, message = "No more than 8 digits allowed!")
	private String price;
	
	@Size(max = 1000, message = "No more than 1000 characters allowed!")
	private String description;

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
}