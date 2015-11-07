package ch.ututor.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.ututor.controller.pojos.NewMessageForm;
import ch.ututor.controller.service.ExceptionService;
import ch.ututor.controller.service.MessageCenterService;
import ch.ututor.controller.exceptions.UserNotFoundException;
import ch.ututor.controller.exceptions.form.MessageNotFoundException;

@Controller
public class MessageCenterController {
	@Autowired MessageCenterService messageCenterService;
	@Autowired ExceptionService exceptionService;
	
	@RequestMapping(value={"/user/messagecenter"}, method = RequestMethod.GET)
    public ModelAndView messageCenterView(@RequestParam(value = "view", required = false) String view) {
		if(view==null){
			view="inbox";
		}
        ModelAndView model = new ModelAndView("user/messagecenter");
        model.addObject("messageList", messageCenterService.getMessagesByView(view));
        return model;
    }
	
	@RequestMapping(value="/user/messagecenter", method = RequestMethod.POST)
	public ModelAndView messageCenterAction(
			@RequestParam(value = "action") String actionString,
			@RequestParam(value = "objectId") String objectIdString,
			@RequestParam(value = "view", required = false) String viewString,
			@RequestParam(value = "show", required = false) String showString){
			
		String action = messageCenterService.normalizeString(actionString);
		long objectId = messageCenterService.normalizeLong(objectIdString);
		String view = messageCenterService.normalizeView(viewString);
		long show = messageCenterService.normalizeLong(showString);
		
		if(action.equals("delete")){
			messageCenterService.deleteMessage(objectId);
		}
		
		return new ModelAndView("redirect:/user/messagecenter/?view="+view+"&show="+show);
	}
	
	@RequestMapping(value={"/user/messagecenter/new"}, method = RequestMethod.GET)
    public ModelAndView messageCenterNewView(@RequestParam(value = "receiverId") String receiverIdString, HttpServletRequest request) {
		long receiverId = messageCenterService.normalizeLong(receiverIdString);
        try{
            ModelAndView model = new ModelAndView("user/new-message");
        	model.addObject("newMessageForm", messageCenterService.prefillNewMessageForm(receiverId));
        	return model;
        }catch(MessageNotFoundException e){
        	return exceptionService.addException(null, e.getMessage());
        }catch(UserNotFoundException e){
        	return exceptionService.addException(null, e.getMessage());
        }
    }
	
	@RequestMapping(value={"/user/messagecenter/reply"}, method = RequestMethod.GET)
    public ModelAndView messageCenterReplyView(@RequestParam(value = "replyToMessageId") String messageIdString, HttpServletRequest request) {
		long messageId = messageCenterService.normalizeLong(messageIdString);
        try{
            ModelAndView model = new ModelAndView("user/new-message");
        	model.addObject("newMessageForm", messageCenterService.prefillReplyMessageForm(messageId));
        	return model;
        }catch(MessageNotFoundException e){
        	return exceptionService.addException(null, e.getMessage());
        }catch(UserNotFoundException e){
        	return exceptionService.addException(null, e.getMessage());
        }
    }
	
	@RequestMapping(value={"/user/messagecenter/new", "/user/messagecenter/reply"}, method = RequestMethod.POST)
    public ModelAndView messageCenterMessageSave(@Valid NewMessageForm newMessageForm, BindingResult result, HttpServletRequest request) {
		if ( !result.hasErrors() ){
			try{
				messageCenterService.sendMessage( newMessageForm );
				return new ModelAndView("redirect:/user/messagecenter/?view=outbox");
			}catch(UserNotFoundException e){
		        return exceptionService.addException(null, e.getMessage());
		    }
		}
		ModelAndView model = new ModelAndView("user/new-message");
		model.addObject(newMessageForm);
		return model;
    }
}
