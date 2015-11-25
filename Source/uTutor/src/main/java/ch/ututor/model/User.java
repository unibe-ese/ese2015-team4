package ch.ututor.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
public class User{
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(unique=true)
	private String username;
	
	private String password;
	private String firstName;
	private String lastName;
	
	@Column( length=512000 )
	private byte[] profilePic;

	private boolean isTutor = false;
	
	@Column( length=1048 )
	private String description;	
	
	private Float price = 0F;
	
	private Integer rating;
	
	public Long getId(){
		return id;
	}
	
	public void setId( Long id ){
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername( String username ) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword( String password ) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		this.password = passwordEncoder.encode( password );
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName( String firstName ) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName( String lastName ) {
		this.lastName = lastName;
	}
	
    public byte[] getProfilePic() {
        return profilePic;
    }
 
    public void setProfilePic( byte[] profilePic ) {
        this.profilePic = profilePic;
    }
    
    public boolean hasProfilePic() {
    	if( profilePic == null ) {
    		return false;
    	} else {
    		return true;
    	}
    }
    
    public void setIsTutor( boolean isTutor ) {
    	this.isTutor = isTutor;
    }
    
    public boolean getIsTutor() {
    	return isTutor;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public void setDescription( String description ) {
    	if(description == null || !description.equals("-")){
    		this.description = description;
    	}
    }
    
    public float getPrice() {
		return price;
	}
    
	public void setPrice(float price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}
}