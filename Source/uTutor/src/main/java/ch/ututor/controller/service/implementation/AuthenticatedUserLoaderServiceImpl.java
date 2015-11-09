package ch.ututor.controller.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ch.ututor.controller.service.AuthenticatedUserLoaderService;
import ch.ututor.controller.service.UserService;
import ch.ututor.model.User;

@Service
public class AuthenticatedUserLoaderServiceImpl implements AuthenticatedUserLoaderService {
	
	@Autowired 	private UserService userService;

	public User getAuthenticatedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.load( auth.getName() );
	}
}