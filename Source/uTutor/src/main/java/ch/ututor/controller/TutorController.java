package ch.ututor.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.ututor.exceptions.CustomException;
import ch.ututor.pojos.AddLectureForm;
import ch.ututor.pojos.AddTimeslotsForm;
import ch.ututor.pojos.BecomeTutorForm;
import ch.ututor.service.interfaces.AuthenticatedUserService;
import ch.ututor.service.interfaces.ExceptionService;
import ch.ututor.service.interfaces.TimeSlotService;
import ch.ututor.service.interfaces.TutorService;
import ch.ututor.utils.FlashMessage;

@Controller
public class TutorController {
	
	@Autowired 	private TutorService tutorService;
	@Autowired 	private TimeSlotService timeSlotService;
	@Autowired 	private AuthenticatedUserService authenticatedUserService;
	@Autowired	private ExceptionService exceptionService;
	
	/**
	 * @return	A ModelAndView containing a BecomeTutorForm or a ModelAndView of the
	 * 			user's profile if he's already tutor.
	 */
	@RequestMapping( value = {"/user/become-tutor"}, method = RequestMethod.GET )
    public ModelAndView displayBecomeTutorForm() {
		
		if( authenticatedUserService.getIsTutor() ){
			return new ModelAndView( "redirect:/user/profile" );
		}
		
		ModelAndView model = new ModelAndView( "user/become-tutor" );
		model.addObject( tutorService.preFillBecomeTutorForm( new BecomeTutorForm() ) );
		return model;
    }
	
	/**
	 * @return	A ModelAndView with a redirect to the user's profile if he's already tutor or
	 * 			the become tutor process has ended successful. Else a ModelAndView to become tutor.
	 */
	@RequestMapping( value = {"/user/become-tutor"}, method = RequestMethod.POST )
    public ModelAndView becomeTutor(	@Valid BecomeTutorForm becomeTutorForm, 
    									BindingResult result, 
    									RedirectAttributes redirectAttributes) {
		
		if( authenticatedUserService.getIsTutor() ){
			return new ModelAndView( "redirect:/user/profile" );
		}
    	
		ModelAndView model = new ModelAndView( "user/become-tutor" );
		if ( !result.hasErrors() ) {
			try {
				tutorService.becomeTutor( becomeTutorForm );
				FlashMessage.addMessage(redirectAttributes, "Account successfully upgraded to tutor.", FlashMessage.Type.SUCCESS);
				return new ModelAndView( "redirect:/user/profile" );
			} catch ( CustomException e ) {
				model = exceptionService.addException( model, e.getMessage() );
			}
        }
		
    	return model;
    }
	
	/**
	 * @return	If the user's already tutor, the method returns a ModelAndView 
	 * 			with an AddLectureForm to add a new lecture. Otherwise a ModelAndView
	 * 			to become tutor is returned.
	 */
	@RequestMapping( value = {"/user/add-lecture"}, method = RequestMethod.GET )
    public ModelAndView displayAddLectureForm() {
		if( !authenticatedUserService.getIsTutor() ) {
			return new ModelAndView( "redirect:/user/become-tutor" );
		}
		ModelAndView model = new ModelAndView( "user/add-lecture" );
		model.addObject( "addLectureForm", new AddLectureForm() );
    	return model;
    }
	
	/**
	 * @return	A ModelAndView of the own profile if the lecture has been added correctly.
	 * 			Otherwise a ModelAndView to add the lecture again. If the current logged in user
	 * 			isn't yet tutor, a ModelAndView to become tutor is returned.
	 */
	@RequestMapping( value = {"/user/add-lecture"}, method = RequestMethod.POST )
	public ModelAndView addLecture(	@Valid AddLectureForm addLectureForm, 
									BindingResult result, 
									RedirectAttributes redirectAttributes) {
		if( !authenticatedUserService.getIsTutor() ) {
			return new ModelAndView( "redirect:/user/become-tutor" );
		}
		
		ModelAndView model = new ModelAndView( "user/add-lecture" );
		if ( !result.hasErrors() ) {
			try {
				tutorService.addTutorLecture( addLectureForm );
				return new ModelAndView( "redirect:/user/profile" );
			} catch ( CustomException e ) {
				model = exceptionService.addException( model, e.getMessage() );
			}
		}
		
    	return model;
	}
	
	/**
	 * @return	If the user's already tutor, the method returns a ModelAndView 
	 * 			with an AddTimeslotForm to add availability. Otherwise a ModelAndView
	 * 			to become tutor is returned.
	 */
	@RequestMapping( value = {"/user/add-timeslots"}, method = RequestMethod.GET )
    public ModelAndView displayAddTimeslotForm() {
		if( !authenticatedUserService.getIsTutor() ) {
			return new ModelAndView( "redirect:/user/become-tutor" );
		}
		ModelAndView model = new ModelAndView( "user/add-timeslots" );
		model.addObject( "addTimeslotsForm", new AddTimeslotsForm() );
		model.addObject( "possibleTimeslots", timeSlotService.getPossibleTimeslots());
		
    	return model;
    }
	
	/**
	 * @return	A ModelAndView of the own profile if the lecture has been added correctly.
	 * 			Otherwise a ModelAndView to add the availability again. If the current logged in user
	 * 			isn't yet tutor, a ModelAndView to become tutor is returned.
	 */
	@RequestMapping( value = {"/user/add-timeslots"}, method = RequestMethod.POST )
	public ModelAndView addTimeslots(	@Valid AddTimeslotsForm addTimeslotsForm, 
									BindingResult result, 
									RedirectAttributes redirectAttributes) {
		if( !authenticatedUserService.getIsTutor() ) {
			return new ModelAndView( "redirect:/user/become-tutor" );
		}
		
		ModelAndView model = new ModelAndView( "user/add-timeslots" );
		if ( !result.hasErrors() ) {
			try {
				timeSlotService.addTimeSlots( addTimeslotsForm );
				return new ModelAndView( "redirect:/user/profile" );
			} catch ( CustomException e ) {
				model = exceptionService.addException( model, e.getMessage() );
			} catch ( ParseException e ) {
				model = exceptionService.addException( e.getMessage() );
			}
		}
		
		model.addObject( "possibleTimeslots", timeSlotService.getPossibleTimeslots());
    	return model;
	}
}