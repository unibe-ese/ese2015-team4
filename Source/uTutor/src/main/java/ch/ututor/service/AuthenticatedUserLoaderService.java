package ch.ututor.service;

import ch.ututor.model.User;

public interface AuthenticatedUserLoaderService {
	public User getAuthenticatedUser();
}
