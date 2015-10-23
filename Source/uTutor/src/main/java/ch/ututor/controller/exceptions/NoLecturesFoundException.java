package ch.ututor.controller.exceptions;

public class NoLecturesFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -981241841670205897L;

	public NoLecturesFoundException( String s ){
		super(s);
	}
	
}
