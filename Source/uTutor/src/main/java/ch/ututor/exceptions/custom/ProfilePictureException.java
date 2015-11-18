package ch.ututor.exceptions.custom;

import ch.ututor.exceptions.CustomException;

public class ProfilePictureException extends CustomException {

	private static final long serialVersionUID = 7986997997378408797L;

	public ProfilePictureException( String s ) {
        super( s );
    }
}