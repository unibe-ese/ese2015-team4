package ch.ututor.exceptions.custom;

import ch.ututor.exceptions.CustomException;

public class PasswordRepetitionException extends CustomException {

	private static final long serialVersionUID = -5175416983071593993L;

	public PasswordRepetitionException( String s ) {
        super( s );
    }	
}