package ch.ututor.service.interfaces;

import ch.ututor.model.User;

public interface UserService {
	
	public User load(Long id);
	public User load(String username);
	public User save(User user);
}