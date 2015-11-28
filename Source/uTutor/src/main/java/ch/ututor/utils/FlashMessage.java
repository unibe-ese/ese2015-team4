package ch.ututor.utils;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class FlashMessage {
	public static enum Type {
		INFO, WARNING, SUCCESS
	}
	
	public static void addMessage( RedirectAttributes redirectAttributes, String message, Type type ){
		redirectAttributes.addFlashAttribute("flash_type", type);
		redirectAttributes.addFlashAttribute("flash_message", message);
	}
}
