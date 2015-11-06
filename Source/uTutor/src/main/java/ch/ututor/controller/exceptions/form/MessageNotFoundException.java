package ch.ututor.controller.exceptions.form;

import ch.ututor.controller.exceptions.FormException;

public class MessageNotFoundException extends FormException {
	
	private static final long serialVersionUID = 972294203651882749L;

	public MessageNotFoundException( String s ) {
        super( s );
    }	
	
}
