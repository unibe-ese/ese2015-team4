package ch.ututor.controller.exceptions.form;

import ch.ututor.controller.exceptions.FormException;

public class NoResultFoundException extends FormException {

	private static final long serialVersionUID = 972294203651882749L;

	public NoResultFoundException( String s ) {
        super( s );
    }
	
}
