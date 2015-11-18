package ch.ututor.exceptions.custom;

import ch.ututor.exceptions.CustomException;

public class UserAlreadyExistsException extends CustomException {
	
	private static final long serialVersionUID = -2263457675928173657L;

	public UserAlreadyExistsException( String s ) {
        super( s );
    }
}