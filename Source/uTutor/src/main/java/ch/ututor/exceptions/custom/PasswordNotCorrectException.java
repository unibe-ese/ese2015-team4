package ch.ututor.exceptions.custom;

import ch.ututor.exceptions.CustomException;

public class PasswordNotCorrectException extends CustomException {

	private static final long serialVersionUID = -5482997069396142709L;

	public PasswordNotCorrectException( String s ) {
        super( s );
    }	
}