package ch.ututor.controller.exceptions;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -981241841670205897L;

	public UserNotFoundException( String s ) {
        super( s );
    }
	
}
