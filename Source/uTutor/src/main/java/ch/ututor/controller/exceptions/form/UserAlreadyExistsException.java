package ch.ututor.controller.exceptions.form;

import ch.ututor.controller.exceptions.FormException;

public class UserAlreadyExistsException extends FormException {
	
	private static final long serialVersionUID = -2263457675928173657L;

	public UserAlreadyExistsException( String s ) {
        super( s );
    }
}
