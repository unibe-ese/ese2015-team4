package ch.ututor.controller.exceptions.form;

import ch.ututor.controller.exceptions.FormException;

public class PasswordNotCorrectException extends FormException {

	private static final long serialVersionUID = -5482997069396142709L;

	public PasswordNotCorrectException( String s ) {
        super( s );
    }	
}