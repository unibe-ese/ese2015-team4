package ch.ututor.controller.service;

import ch.ututor.model.User;

public interface AuthenticatedUserLoaderService {
	public User getAuthenticatedUser();
}
