package ch.ututor.controller.exceptions.custom;

import ch.ututor.controller.exceptions.CustomException;

public class MessageNotFoundException extends CustomException {
	
	private static final long serialVersionUID = 972294203651882749L;

	public MessageNotFoundException( String s ) {
        super( s );
    }	
}
