package ch.ututor.pojos;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Future;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

public class AddTimeslotsForm {
	@NotBlank( message = "Please enter a valid date!" )
	@Pattern( regexp = "((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])",
			  message = "Please enter a valid date (yyyy-mm-dd)!" )
	private String date;
	
	@NotEmpty( message = "Please select at least one time-slot!" )
	private List<String> timeslots;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<String> getTimeslots() {
		return timeslots;
	}

	public void setTimeslots(List<String> timeslots) {
		this.timeslots = timeslots;
	}
}
