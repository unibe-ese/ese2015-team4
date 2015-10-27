package ch.ututor.controller.pojos;

import org.hibernate.validator.constraints.NotBlank;

public class BecomeTutorForm extends AddLectureForm {
	
	@NotBlank(message = "Please enter a valid price!")
	private String price;
	
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