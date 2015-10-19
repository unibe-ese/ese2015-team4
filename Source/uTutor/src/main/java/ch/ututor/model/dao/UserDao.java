package ch.ututor.model.dao;


import org.springframework.data.repository.CrudRepository;

import ch.ututor.model.User;

public interface UserDao extends CrudRepository<User,Long> {
	
	public User findByUsername( String username );
	public User findById(Long id);
	
}
