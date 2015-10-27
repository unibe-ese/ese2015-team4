package ch.ututor.controller.exceptions.form;

import ch.ututor.controller.exceptions.FormException;

public class TutorLectureAlreadyExistsException extends FormException {

	private static final long serialVersionUID = 972294203651882749L;

	public TutorLectureAlreadyExistsException( String s ) {
        super( s );
    }
}