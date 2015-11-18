package ch.ututor.service.interfaces;

import ch.ututor.model.User;

public interface AuthenticatedUserLoaderService {
	public User getAuthenticatedUser();
}
