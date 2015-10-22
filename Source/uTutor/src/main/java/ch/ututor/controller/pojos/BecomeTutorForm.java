package ch.ututor.controller.pojos;

import org.hibernate.validator.constraints.NotBlank;

public class BecomeTutorForm {
	
	@NotBlank(message = "Please enter a valid price!")
	private String price;
	
	@NotBlank(message = "Please enter a lecture name!")
	private String lecture;
	
	private String description;
	
	private Float grade;

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getLecture() {
		return lecture;
	}

	public void setLecture(String lecture) {
		this.lecture = lecture;
	}

	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
		
	public Float getGrade() {
		return grade;
	}

	public void setGrade(Float grade) {
		this.grade = grade;
	}
	
}
