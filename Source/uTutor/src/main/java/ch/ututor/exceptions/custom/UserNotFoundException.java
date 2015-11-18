package ch.ututor.exceptions.custom;

import ch.ututor.exceptions.CustomException;

public class UserNotFoundException extends CustomException {

	private static final long serialVersionUID = -981241841670205897L;

	public UserNotFoundException( String s ) {
        super( s );
    }
}