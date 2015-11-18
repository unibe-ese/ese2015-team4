package ch.ututor.exceptions.custom;

import ch.ututor.exceptions.CustomException;

public class InvalidPriceException extends CustomException {
	
	private static final long serialVersionUID = 972294203651882749L;

	public InvalidPriceException( String s ) {
        super( s );
    }	
}