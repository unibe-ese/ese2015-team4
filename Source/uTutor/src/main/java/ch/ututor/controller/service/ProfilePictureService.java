package ch.ututor.controller.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ch.ututor.model.User;

public interface ProfilePictureService {
	public boolean validateUploadedPicture(MultipartFile file);
	public byte[] resizeProfilePicture(byte[] picture);
	public ModelAndView addProfilePictureInfoToModel( ModelAndView model, User user );
}