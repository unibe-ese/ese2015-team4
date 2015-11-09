package ch.ututor.controller.exceptions;

public abstract class CustomException extends RuntimeException {

	private static final long serialVersionUID = 8582603042239368291L;

	public CustomException( String s ) {
        super( s );
    }
}
