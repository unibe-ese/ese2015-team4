package ch.ututor.exceptions.custom;

import ch.ututor.exceptions.CustomException;

public class NoResultFoundException extends CustomException {

	private static final long serialVersionUID = 972294203651882749L;

	public NoResultFoundException( String s ) {
        super( s );
    }
}