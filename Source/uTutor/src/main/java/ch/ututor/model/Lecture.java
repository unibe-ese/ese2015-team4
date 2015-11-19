package ch.ututor.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Lecture {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@NotNull
	@Column(unique=true)
	private String name;

	public Long getId() {
		return id;
	}
	
	public void setId( Long id ) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}
}