package ch.ututor.controller.exceptions.custom;

import ch.ututor.controller.exceptions.CustomException;

public class UserAlreadyExistsException extends CustomException {
	
	private static final long serialVersionUID = -2263457675928173657L;

	public UserAlreadyExistsException( String s ) {
        super( s );
    }
}