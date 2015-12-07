package ch.ututor.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface ProfilePictureService {
	
	public boolean validateUploadedPicture(MultipartFile file);
	public byte[] resizeProfilePicture(byte[] picture);
	
}