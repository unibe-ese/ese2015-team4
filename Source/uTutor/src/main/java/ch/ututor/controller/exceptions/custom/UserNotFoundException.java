package ch.ututor.controller.exceptions.custom;

import ch.ututor.controller.exceptions.CustomException;

public class UserNotFoundException extends CustomException {

	private static final long serialVersionUID = -981241841670205897L;

	public UserNotFoundException( String s ) {
        super( s );
    }
}