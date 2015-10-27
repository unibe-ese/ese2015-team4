package ch.ututor.controller.exceptions;

public class FormException extends RuntimeException {

	private static final long serialVersionUID = 8582603042239368291L;

	public FormException( String s ) {
        super( s );
    }
}
