package ch.ututor.controller.exceptions.form;

import ch.ututor.controller.exceptions.FormException;

public class PasswordRepetitionException extends FormException {

	private static final long serialVersionUID = -5175416983071593993L;

	public PasswordRepetitionException( String s ) {
        super( s );
    }
	
}
