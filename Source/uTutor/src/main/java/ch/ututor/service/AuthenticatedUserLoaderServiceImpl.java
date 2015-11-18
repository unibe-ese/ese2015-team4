package ch.ututor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ch.ututor.model.User;
import ch.ututor.service.interfaces.AuthenticatedUserLoaderService;
import ch.ututor.service.interfaces.UserService;

@Service
public class AuthenticatedUserLoaderServiceImpl implements AuthenticatedUserLoaderService {
	
	@Autowired 	private UserService userService;

	public User getAuthenticatedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.load( auth.getName() );
	}
}