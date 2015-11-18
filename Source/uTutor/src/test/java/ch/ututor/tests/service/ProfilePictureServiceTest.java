package ch.ututor.tests.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.ututor.exceptions.custom.ProfilePictureException;
import ch.ututor.service.interfaces.ProfilePictureService;
import ch.ututor.utils.MultipartFileMocker;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/test/profilePictureService.xml"})
public class ProfilePictureServiceTest {
	@Autowired ProfilePictureService profilePictureService;
	
	private BufferedImage runImageThroughProfilePictureServiceResize(String source) throws IOException{
		byte[] image = profilePictureService.resizeProfilePicture(MultipartFileMocker.mockJpeg(source).getBytes());
		return byte2image(image);
	}
	
	private BufferedImage byte2image(byte[] data) throws IOException{
		InputStream in = new ByteArrayInputStream(data);
        return ImageIO.read(in);
	}
	
	@Test(expected = ProfilePictureException.class)
	public void validateUploadedPictureExceptionEmpty(){
		profilePictureService.validateUploadedPicture(MultipartFileMocker.mockEmpty());
	}
	
	@Test(expected = ProfilePictureException.class)
	public void validateUploadedPictureExceptionNotJpeg() throws IOException{
		profilePictureService.validateUploadedPicture(MultipartFileMocker.mockType("src/main/webapp/WEB-INF/test/images/notjpeg.png","image/png"));
	}
	
	@Test
	public void validateUploadedPictureSuccessTest() throws IOException{
		assertTrue(profilePictureService.validateUploadedPicture(MultipartFileMocker.mockJpeg("src/main/webapp/WEB-INF/test/images/350x100.jpg")));
	}
	
	@Test
	public void resizeProfilePictureLandscapeNoResizeTest() throws IOException{
		BufferedImage bufferedImage = runImageThroughProfilePictureServiceResize("src/main/webapp/WEB-INF/test/images/350x100.jpg");
		assertEquals(350, bufferedImage.getWidth());
		assertEquals(100, bufferedImage.getHeight());
	}
	
	@Test
	public void resizeProfilePicturePortraitNoResizeTest() throws IOException{
		BufferedImage bufferedImage = runImageThroughProfilePictureServiceResize("src/main/webapp/WEB-INF/test/images/100x350.jpg");
		assertEquals(100, bufferedImage.getWidth());
		assertEquals(350, bufferedImage.getHeight());
	}
	
	@Test
	public void resizeProfilePictureSquareNoResizeTest() throws IOException{
		BufferedImage bufferedImage = runImageThroughProfilePictureServiceResize("src/main/webapp/WEB-INF/test/images/350x350.jpg");
		assertEquals(350, bufferedImage.getWidth());
		assertEquals(350, bufferedImage.getHeight());
	}
	
	@Test
	public void resizeProfilePictureLandscapeResizeTest() throws IOException{
		BufferedImage bufferedImage = byte2image(profilePictureService.resizeProfilePicture(MultipartFileMocker.mockJpeg("src/main/webapp/WEB-INF/test/images/700x200.jpg").getBytes()));
		assertEquals(350, bufferedImage.getWidth());
		assertEquals(100, bufferedImage.getHeight());
	}
	
	@Test
	public void resizeProfilePicturePortraitResizeTest() throws IOException{
		BufferedImage bufferedImage = byte2image(profilePictureService.resizeProfilePicture(MultipartFileMocker.mockJpeg("src/main/webapp/WEB-INF/test/images/200x700.jpg").getBytes()));
		assertEquals(100, bufferedImage.getWidth());
		assertEquals(350, bufferedImage.getHeight());
	}
	
	@Test
	public void resizeProfilePictureSquareResizeTest() throws IOException{
		BufferedImage bufferedImage = byte2image(profilePictureService.resizeProfilePicture(MultipartFileMocker.mockJpeg("src/main/webapp/WEB-INF/test/images/700x700.jpg").getBytes()));
		assertEquals(350, bufferedImage.getWidth());
		assertEquals(350, bufferedImage.getHeight());
	}
}
