package ch.ututor.controller.service;

import ch.ututor.controller.exceptions.UserNotFoundException;
import ch.ututor.model.User;

public interface UserService {
	public User load(long id) throws UserNotFoundException;
	public User load(String username) throws UserNotFoundException;
}
