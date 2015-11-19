/**
 * Represents one hour of tutor time
 *
 */

package ch.ututor.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Table(
		uniqueConstraints = @UniqueConstraint(
				columnNames = {"tutor_id", "beginDateTime"}
))

@Entity
public class TimeSlot {
	public static enum Status {
		AVAILABLE, REQUESTED, ACCEPTED
	}
	
	@Id
	@GeneratedValue
	private long id;
	
	private Status status = Status.AVAILABLE;
	
	private Date beginDateTime;
	
	@ManyToOne
	private User tutor;
	
	@ManyToOne
	private User student;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public Date getBeginDateTime() {
		return beginDateTime;
	}
	
	public void setBeginDateTime(Date beginDateTime) {
		this.beginDateTime = beginDateTime;
	}
	
	public User getTutor() {
		return tutor;
	}
	
	public void setTutor(User tutor) {
		this.tutor = tutor;
	}
	
	public User getStudent() {
		return student;
	}
	
	public void setStudent(User student) {
		this.student = student;
	}
}
