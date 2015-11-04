package ch.ututor.controller.pojos;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class AddLectureForm {
	
	@NotBlank(message = "Please enter a lecture name!")
	@Size(max = 30, message = "No more than 30 characters allowed!")
	private String lecture;
	
	private Float grade;

	public String getLecture() {
		return lecture;
	}

	public void setLecture(String lecture) {
		this.lecture = lecture;
	}

	public Float getGrade() {
		return grade;
	}

	public void setGrade(Float grade) {
		this.grade = grade;
	}
}