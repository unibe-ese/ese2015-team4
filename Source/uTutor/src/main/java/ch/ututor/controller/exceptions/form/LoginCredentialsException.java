package ch.ututor.controller.exceptions.form;

import ch.ututor.controller.exceptions.FormException;

public class LoginCredentialsException extends FormException {

	private static final long serialVersionUID = -3028455843611115109L;

	public LoginCredentialsException( String s ) {
        super( s );
    }
}
