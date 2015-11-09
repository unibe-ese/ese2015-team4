package ch.ututor.controller.exceptions.custom;

import ch.ututor.controller.exceptions.CustomException;

public class NoLecturesFoundException extends CustomException {
	
	private static final long serialVersionUID = -981241841670205897L;

	public NoLecturesFoundException( String s ){
		super( s );
	}
}