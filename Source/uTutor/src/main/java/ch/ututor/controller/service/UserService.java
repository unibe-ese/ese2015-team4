package ch.ututor.controller.service;

import ch.ututor.model.User;

public interface UserService {
	
	public User load(Long id);
	public User load(String username);
}