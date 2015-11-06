package ch.ututor.controller.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ch.ututor.controller.exceptions.form.ProfilePictureException;
import ch.ututor.model.User;

public interface ProfilePictureService {
	public boolean validateUploadedPicture(MultipartFile file) throws ProfilePictureException;
	public byte[] resizeProfilePicture(byte[] picture);
	public ModelAndView addUserDataToModel( ModelAndView model, User user );
}