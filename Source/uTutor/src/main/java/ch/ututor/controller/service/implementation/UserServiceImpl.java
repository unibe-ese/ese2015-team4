package ch.ututor.controller.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.controller.exceptions.UserNotFoundException;
import ch.ututor.controller.service.UserService;
import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;

/**
 *	This class provides methods to find a specific user.
 */

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired    UserDao userDao;
	
	public User load(Long id) throws UserNotFoundException{
		User user = userDao.findById(id);
		
		if(user == null){
			throw new UserNotFoundException("User not found.");
		}
		
		return user;
	}
	
	public User load(String username){
		User user = userDao.findByUsername(username);
		
		if(user == null){
			throw new UserNotFoundException("User not found.");
		}
		
		return user;
	}
}