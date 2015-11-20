package ch.ututor.exceptions.custom;

import ch.ututor.exceptions.CustomException;

public class TimeSlotException extends CustomException {

	private static final long serialVersionUID = 4015362615792891220L;

	public TimeSlotException( String s ) {
        super( s );
    }
}