package ch.ututor.controller.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 *	This class provides needed methods for the spring security framework.
 */

@Component
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	UserDao userDao;
	
	/**
	 *	@throws UsernameNotFoundException if the username doesn't exist in the database
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities());
	}

	private Collection<? extends GrantedAuthority> getAuthorities() {	
		List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		SimpleGrantedAuthority roles = new SimpleGrantedAuthority("ROLE_USER");
		list.add(roles);
		return list;
	}
}