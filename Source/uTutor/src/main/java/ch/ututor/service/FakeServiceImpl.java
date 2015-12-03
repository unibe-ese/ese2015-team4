package ch.ututor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.model.User;
import ch.ututor.service.interfaces.AuthenticatedUserLoaderService;

@Service
public class FakeServiceImpl implements FakeService {
	@Autowired FakeServiceOther fakeServiceOther;
	
	
	@Override
	public void doSomething() {
		fakeServiceOther.doSomething();
	}
	
}
