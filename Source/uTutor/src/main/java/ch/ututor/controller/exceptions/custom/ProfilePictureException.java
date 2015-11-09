package ch.ututor.controller.exceptions.custom;

import ch.ututor.controller.exceptions.CustomException;


public class ProfilePictureException extends CustomException {

	private static final long serialVersionUID = 7986997997378408797L;

	public ProfilePictureException( String s ) {
        super( s );
    }
}