package ch.ututor.controller.service.implementation;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ch.ututor.controller.exceptions.form.ProfilePictureException;
import ch.ututor.controller.service.ProfilePictureService;
import ch.ututor.model.User;

/**
 *	This class provides methods to handle uploaded profile pictures.
 */

@Service
public class ProfilePictureServiceImpl implements ProfilePictureService{
	
	final static int PROFILE_PICTURE_MAX_WIDTH = 350;
	final static int PROFILE_PICTURE_MAX_HEIGHT = 350;
	
	/**
	 *	@throws	ProfilePictureException if file is null, empty or not an image
	 */
	public boolean validateUploadedPicture(MultipartFile file) {
		if(file == null || file.isEmpty() ){
			throw new ProfilePictureException("Please select a file.");
		}else if(!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/jpg")){
			throw new ProfilePictureException("Only JPG files allowed.");
		}
		return true;
	}
	
	/**
	 *	@param picture	Should not be null
	 *
	 *	@return		A byte array with the binary data of the resized picture
	 * 				or null if an error occurs.
	 */
	public byte[] resizeProfilePicture(byte[] picture) {
		assert( picture != null );
		
		try{
			InputStream in = new ByteArrayInputStream(picture);
			BufferedImage originalImage = ImageIO.read(in);
			int type;
			
			if (originalImage.getType() == 0){
				type = BufferedImage.TYPE_INT_ARGB;
			} else {
				type = originalImage.getType();
			}
				
			BufferedImage resizeImageJpg = resizeImage(originalImage, type);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
			ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
			writer.setOutput(ios);
			ImageWriteParam param = writer.getDefaultWriteParam();
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(0.8F);
			writer.write(resizeImageJpg);
			
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;
		} catch( IOException e ){
			return null;
		}		
	}
	
	private BufferedImage resizeImage(BufferedImage originalImage, int type){
		int newW=PROFILE_PICTURE_MAX_WIDTH;
		int newH=PROFILE_PICTURE_MAX_HEIGHT;
		if(originalImage.getWidth() < newW && originalImage.getHeight() < newH){
			newW=originalImage.getWidth();
			newH=originalImage.getHeight();
		}else{
			if(originalImage.getWidth() > originalImage.getHeight()){
				float ratio = (float) newW / (float) originalImage.getWidth();
				newH = (int) ((float) originalImage.getHeight() * ratio);
			}else{
				float ratio = (float) newH / (float) originalImage.getHeight();
				newW = (int) ((float) originalImage.getWidth() * ratio);
			}
		}
		BufferedImage resizedImage = new BufferedImage(newW, newH, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, newW, newH, null);
		g.dispose();
	
		return resizedImage;
	}
	
	/**
	 *	@param model	Should not be null
	 *	@param user		Should not be null
	 */
	public ModelAndView addProfilePictureInfoToModel( ModelAndView model, User user ){
		assert( model != null );
		assert( user != null );
		
		model.addObject( "userId", user.getId() );
    	model.addObject( "hasProfilePic", user.hasProfilePic() );
    	return model;
	}
	
}