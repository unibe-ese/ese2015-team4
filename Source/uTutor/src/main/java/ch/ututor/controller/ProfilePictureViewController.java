package ch.ututor.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.ututor.model.User;
import ch.ututor.model.dao.UserDao;

@Controller
public class ProfilePictureViewController {
	
	@Autowired    UserDao userDao;
	@Autowired	  ServletContext servletContext;
    
    /** 
     * Returns the profile picture of a user saved in the database or the default picture if no profile picture was set before.
     * @url /user/img/avatar.jpg?userId=<code>user.id</code>
     * @param userId	the unique id of the user for wich the profile picture should be load, should not be null
     * @return byte Array with the binary data of the profile pic (image/jpeg)
     * @throws IOException if default profile picture is not found/readable.
     */
    @RequestMapping(value = "/user/img/avatar", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] pictureJpeg(@RequestParam(value = "userId") Long userId) throws IOException {
    	User user = userDao.findById(userId);
    	if(!user.hasProfilePic()){
    		InputStream image = servletContext.getResourceAsStream("/img/default_avatar.jpg");
    		return IOUtils.toByteArray(image);
    	}
    	return user.getProfilePic(); 			
    }
}