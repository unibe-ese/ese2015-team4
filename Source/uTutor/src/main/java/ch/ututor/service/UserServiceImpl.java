package ch.ututor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.exceptions.custom.UserNotFoundException;
import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;
import ch.ututor.service.interfaces.UserService;


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired   private UserDao userDao;
	
	public User load( Long id ) {
		User user = userDao.findById( id );
		
		if( user == null ) {
			throw new UserNotFoundException( "User not found." );
		}
		
		return user;
	}
	
	public User load( String username ) {
		User user = userDao.findByUsername( username );
		
		if( user == null ) {
			throw new UserNotFoundException( "User not found." );
		}
		
		return user;
	}
}