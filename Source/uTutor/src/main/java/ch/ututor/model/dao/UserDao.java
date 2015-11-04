package ch.ututor.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.ututor.model.User;

/**
 *	Implements methods to find an user by his username or id
 */

public interface UserDao extends CrudRepository<User,Long> {
	
	public User findByUsername( String username );
	public User findById(Long id);
}