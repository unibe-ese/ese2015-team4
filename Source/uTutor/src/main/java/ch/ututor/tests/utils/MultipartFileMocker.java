package ch.ututor.tests.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileMocker {
	public static MultipartFile mockJpeg(String source) throws IOException{
		return mockType(source, "image/jpeg");
	}
	public static MultipartFile mockEmpty(){
		return new MockMultipartFile("file", new byte[]{});
	}
	public static MultipartFile mockType(String source, String type) throws IOException{
		File file = new File(source);
	    FileInputStream input = new FileInputStream(file);
	    return new MockMultipartFile("file", file.getName(), type, IOUtils.toByteArray(input));
	}
}