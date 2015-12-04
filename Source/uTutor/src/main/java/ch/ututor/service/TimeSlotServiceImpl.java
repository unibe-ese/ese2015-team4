package ch.ututor.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.exceptions.custom.InvalidDateException;
import ch.ututor.exceptions.custom.TimeSlotException;
import ch.ututor.model.Message;
import ch.ututor.model.TimeSlot;
import ch.ututor.model.User;
import ch.ututor.model.dao.TimeSlotDao;
import ch.ututor.pojos.AddTimeslotsForm;
import ch.ututor.service.interfaces.AuthenticatedUserLoaderService;
import ch.ututor.service.interfaces.MessageCenterService;
import ch.ututor.service.interfaces.TimeSlotService;
import ch.ututor.service.interfaces.TutorService;
import ch.ututor.utils.TimeHelper;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {
	
	@Autowired 	private AuthenticatedUserLoaderService authenticatedUserLoaderService;
	@Autowired	private MessageCenterService messageCenterService;
	@Autowired 	private TimeSlotDao timeSlotDao;
	@Autowired  private TutorService tutorService;
	
	/**
	 *	Returns a String array of time slots from 6am to 10pm each going from xx:00 to xx:59
	 */
	public List<String> getPossibleTimeslots(){
		List<String> possibleTimeslots = new ArrayList<String>();
		
		for(Integer i = 6; i < 23; i++){
			String hour = i.toString();
			
			if(i < 10){
				hour = "0" + i;
			}
			possibleTimeslots.add(hour + ":00 - " + hour + ":59");
		}
		
		return possibleTimeslots;
	}

	//TODO: Mein englisch überprüfen
	/**
	 *	saves the selected time slots in the tutor's profile
	 *
	 *	@param addTimeSlotsForm		should not be null	 
	 *
	 * 	@throws ParseException 		if the date string saved in the AddTimeslotsForm can not be parsed correctly.
	 */
	public List<TimeSlot> addTimeSlots(AddTimeslotsForm addTimeSlotsForm) throws ParseException {
		assert( addTimeSlotsForm != null );
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		String selectedDateString = addTimeSlotsForm.getDate();
		Date selectedDate;
		
		try{	
			selectedDate = parseStringToDate( dateFormat, selectedDateString );
		} catch ( ParseException e ){
			throw new InvalidDateException( "The entered date is invalid!" );
		}
		
		if ( TimeHelper.isPast( selectedDate ) ){
			throw new InvalidDateException( "Please enter a future date !" );
		}
		
		return saveTimeslots( selectedDateString, addTimeSlotsForm.getTimeslots() );
	}
	
	private List<TimeSlot> saveTimeslots( String selectedDate, List<String> timeslots ) throws ParseException {
		
		User tutor = authenticatedUserLoaderService.getAuthenticatedUser();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		List<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
		
		for( String s: timeslots ){
			Date date;
			
			date = parseStringToDate( dateFormat, selectedDate + " " + s.substring( 0, 5 ) );			
			
			TimeSlot timeSlot;
			timeSlot = timeSlotDao.findByBeginDateTimeAndTutor( date, tutor);
			
			if ( timeSlot == null ){
				timeSlot = saveTimeslot(tutor, date);
			}
			
			timeSlots.add( timeSlot );
		}
		
		return timeSlots;
	}
	
	private TimeSlot saveTimeslot(User tutor, Date beginDateTime){
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setBeginDateTime( beginDateTime );
		timeSlot.setTutor( tutor );
		
		return timeSlotDao.save( timeSlot );
	}
	
	private Date parseStringToDate( SimpleDateFormat simpleDateFormat, String dateToParse ) throws ParseException{
		Date date;
		
		date = simpleDateFormat.parse( dateToParse );
		
		return date;
	}
	
	public List<TimeSlot> getTimeSlotsByUser( User user ) {
		return timeSlotDao.findByTutorOrStudentOrderByBeginDateTimeAsc(user, user);
	}

	/**
	 *	@throws TimeSlotException if the requested time lost is in the past
	 *			or no longer available
	 */
	public TimeSlot requestForTimeSlot(long id) {
		TimeSlot timeSlot = timeSlotDao.findById( id );
		User student = authenticatedUserLoaderService.getAuthenticatedUser();
		
		if( timeSlot != null && timeSlot.getStatus() == TimeSlot.Status.AVAILABLE && !student.equals( timeSlot.getTutor() ) ){
			
			if(TimeHelper.isPast(timeSlot.getBeginDateTime())){
				throw new TimeSlotException("You can not request a past time-slot!");
			}
			
			timeSlot.setStatus( TimeSlot.Status.REQUESTED );
			timeSlot.setStudent( student );
			timeSlotDao.save( timeSlot );
			
			String messageBody = createRequestMessageText( timeSlot.getTutor(), student);
			
			Message message = createRequestMessage( timeSlot.getTutor(), student, messageBody );
			messageCenterService.sendMessage( message );
			
		} else {
			throw new TimeSlotException( "The time slot you wanted to request is no longer available!" );
		}
		
		return timeSlot;
	}

	private String createRequestMessageText( User tutor, User student ){
		return 	"Dear " + tutor.getFirstName() + "\n\n"
				+ "I have made a request for an appointment.\n"
		        + "Please check out your profile.\n\n"
				+ "Greetings\n"
		        + student.getFirstName() + " " + student.getLastName();
	}
	
	private Message createRequestMessage( User tutor, User student, String messageBody){
		Message message = new Message();
		message.setSender( student );
		message.setReceiver( tutor );
		message.setSubject( "New appointment request!" );
		message.setMessage( messageBody );
		return message;
	}
	
	/**
	 *	@throws TimeSlotException if the tutor wants to delete a TimeSlot which hasn't state available
	 */
	public void deleteTimeSlot( long timeSlotId ) {
		TimeSlot timeSlot = timeSlotDao.findById( timeSlotId );
		
		if( timeSlot != null && authenticatedUserLoaderService.getAuthenticatedUser().equals(timeSlot.getTutor())){
			if( timeSlot.getStatus() !=  TimeSlot.Status.AVAILABLE ){
				throw new TimeSlotException( "The time slot you wanted to delete is no longer in the AVAILABLE state." );
			}
			
			timeSlotDao.delete(timeSlot);
		}
	}

	/**
	 *	@throws TimeSlotException if the TimeSlot the tutor want's to accept is already passed or hasn't the
	 *			requested state
	 */
	public TimeSlot acceptTimeSlotRequest( long timeSlotId ) {
		TimeSlot timeSlot = timeSlotDao.findById( timeSlotId );
		
		if( timeSlot != null && authenticatedUserLoaderService.getAuthenticatedUser().equals(timeSlot.getTutor())){
			if( timeSlot.getStatus() !=  TimeSlot.Status.REQUESTED){
				throw new TimeSlotException( "The time slot request you wanted to accept is no longer in the REQUESTED state." );
			}
			if( TimeHelper.isPast( timeSlot.getBeginDateTime() ) ){
				throw new TimeSlotException( "You can not accept a time slot request for a date in the past!" );
			}
			
			timeSlot.setStatus(TimeSlot.Status.ACCEPTED);
			timeSlotDao.save(timeSlot);
		}
		return timeSlot;
	}

	/**
	 *	@throws TimeSlotException if the Timeslot isn't in the requested state
	 */
	public TimeSlot rejectTimeSlotRequest( long timeSlotId ) {
		TimeSlot timeSlot = timeSlotDao.findById( timeSlotId );
		if( timeSlot != null && authenticatedUserLoaderService.getAuthenticatedUser().equals(timeSlot.getTutor())){
			if( timeSlot.getStatus() !=  TimeSlot.Status.REQUESTED){
				throw new TimeSlotException( "The time slot request you wanted to reject is no longer in the REQUESTED state." );
			}
			
			timeSlot.setStatus(TimeSlot.Status.AVAILABLE);
			timeSlot.setStudent(null);
			timeSlotDao.save(timeSlot);
		}
		return timeSlot;
	}
	
	/**
	 *	@throws TimeSlotException if the rated TimeSlot is in the future or the rating isn't between
	 *			1 and 5.
	 */
	public TimeSlot rateTimeSlot(long timeSlotId, int rating) {
		TimeSlot timeSlot = timeSlotDao.findById( timeSlotId );
		
		if( timeSlot != null && authenticatedUserLoaderService.getAuthenticatedUser().equals(timeSlot.getStudent())){
			if(TimeHelper.isFuture(timeSlot.getBeginDateTime())){
				throw new TimeSlotException("You can not rate a time-slot with a date in the future!");
			}
			if( rating < 1 || rating > 5 ){
				throw new TimeSlotException("Invalid rating. The value must be between 1 and 5!");
			}
			timeSlot.setRating( rating );
			timeSlotDao.save( timeSlot );
			User tutor = timeSlot.getTutor();
			tutorService.updateTutorRating( tutor, getTimeSlotAvgRatingByTutor( tutor ) );
		}
		
		return timeSlot;
	}
	
	public Integer getTimeSlotAvgRatingByTutor(User tutor) {
		Integer rating = null;
		List<TimeSlot> timeSlots = timeSlotDao.findByTutorAndRatingNotNull(tutor);
		int total = 0;
		for( TimeSlot timeSlot : timeSlots ){
			total += timeSlot.getRating();
		}
		if( timeSlots.size() > 0 ){
			rating = total / timeSlots.size();
		}
		return rating;
	}
}
