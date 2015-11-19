package ch.ututor.exceptions.custom;

import ch.ututor.exceptions.CustomException;

public class InvalidDateException extends CustomException {		
		
	private static final long serialVersionUID = 972294203651882749L;

	public InvalidDateException( String s ) {
		super( s );
	}	
}
