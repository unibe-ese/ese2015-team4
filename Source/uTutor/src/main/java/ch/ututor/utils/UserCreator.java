package ch.ututor.utils;

import ch.ututor.model.User;


public class UserCreator {
	
	public static User createStudent(String firstName, String lastName, String username, String password){
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUsername(username);
		user.setPassword(password);
		
		return user;
	}
	
	public static User becomeTutor(User user, String description, Float price){
		user.setDescription(description);
		user.setPrice(price);
		user.setIsTutor(true);
		
		return user;
	}
}
