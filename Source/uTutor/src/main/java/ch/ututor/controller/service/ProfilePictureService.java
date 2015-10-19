package ch.ututor.controller.service;

import org.springframework.web.multipart.MultipartFile;

import ch.ututor.controller.exceptions.form.ProfilePictureException;

public interface ProfilePictureService {
	public boolean validateUploadedPicture(MultipartFile file) throws ProfilePictureException;
	public byte[] resizePicture(byte[] picture);
}
