package ch.ututor.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Table(
		uniqueConstraints = @UniqueConstraint(
				columnNames = {"lecture_id", "tutor_id"}
))

@Entity
public class TutorLecture {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	private User tutor;
	
	@ManyToOne
	private Lecture lecture;
	
	@NotNull
	private Float grade = 0F;
	
	public Long getId() {
		return id;
	}
	
	public void setId( Long id ) {
		this.id = id;
	}
	
	public User getTutor() {
		return tutor;
	}
	
	public void setTutor( User tutor ) {
		this.tutor = tutor;
	}
	
	public Lecture getLecture() {
		return lecture;
	}
	
	public void setLecture( Lecture lecture ) {
		this.lecture = lecture;
	}
	
	public float getGrade() {
		return grade;
	}
	
	public void setGrade( float grade ) {
		this.grade = grade;
	}
}