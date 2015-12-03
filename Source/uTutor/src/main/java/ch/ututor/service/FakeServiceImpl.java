package ch.ututor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.model.User;
import ch.ututor.service.interfaces.AuthenticatedUserLoaderService;

@Service
public class FakeServiceImpl implements FakeService {
	@Autowired AuthenticatedUserLoaderService authUserLoader;
	
	
	@Override
	public void doSomething() {
		User user = authUserLoader.getAuthenticatedUser();
	}
	
}
