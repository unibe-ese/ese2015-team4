package ch.ututor.model.dao;


import org.springframework.data.repository.CrudRepository;
import ch.ututor.model.User;

public interface UserDao extends CrudRepository<User,Long> {
	
	public User findByEmail( String email );
	public User findById(Long id);
	
}
