package ch.ututor.exceptions.custom;

import ch.ututor.exceptions.CustomException;

public class NoLecturesFoundException extends CustomException {
	
	private static final long serialVersionUID = -981241841670205897L;

	public NoLecturesFoundException( String s ){
		super( s );
	}
}