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

@Service
public class TimeSlotServiceImpl implements TimeSlotService {
	
	@Autowired 	private AuthenticatedUserLoaderService authenticatedUserLoaderService;
	@Autowired	private MessageCenterService messageCenterService;
	@Autowired 	private TimeSlotDao timeSlotDao;
	
	public List<String> getPossibleTimeslots(){
		List<String> possibleTimeslots = new ArrayList<String>();
		for(Integer i=6; i<23; i++){
			String hour = i.toString();
			
			if(i < 10){
				hour = "0" + i;
			}
			possibleTimeslots.add(hour + ":00 - " + hour + ":59");
		}
		return possibleTimeslots;
	}

	public List<TimeSlot> addTimeSlots(AddTimeslotsForm addTimeSlotsForm) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		String selectedDateString = addTimeSlotsForm.getDate();
		Date currentDate = new Date();
		Date selectedDate;
		
		try{	
			selectedDate = parseStringToDate( dateFormat, selectedDateString );
		} catch ( ParseException e ){
			throw new InvalidDateException( "The entered date is invalid!" );
		}
		
		if ( currentDate.after( selectedDate ) ){
			throw new InvalidDateException( "Please enter a date which is in the future!" );
		}
		
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
		User tutor = authenticatedUserLoaderService.getAuthenticatedUser();
		
		for( String s: addTimeSlotsForm.getTimeslots() ){
			Date date;
			
			date = parseStringToDate( dateFormat, selectedDateString + " " + s.substring( 0, 5 ) );			
			
			TimeSlot timeSlot;
			timeSlot = timeSlotDao.findByBeginDateTimeAndTutor( date, tutor);
			
			if ( timeSlot == null ){
				timeSlot = addTimeslot(tutor, date);
			}
			
			timeSlots.add( timeSlot );
			
		}
			
		return timeSlots;
	}
	
	public TimeSlot addTimeslot(User tutor, Date beginDateTime){
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

	public List<TimeSlot> getTimeSlotsByUser(User user) {
		return timeSlotDao.findByTutorOrStudentOrderByBeginDateTimeAsc(user, user);
	}

	@Override
	public TimeSlot requestForTimeSlot(long id) {
		TimeSlot timeSlot = timeSlotDao.findById( id );
		User student = authenticatedUserLoaderService.getAuthenticatedUser();
		if( 
				timeSlot != null
				&& timeSlot.getStatus() == TimeSlot.Status.AVAILABLE
				&& !student.equals( timeSlot.getTutor() )
		){
			
			timeSlot.setStatus( TimeSlot.Status.REQUESTED );
			timeSlot.setStudent( student );
			timeSlotDao.save( timeSlot );
			
			String messageBody = "Hello "+timeSlot.getTutor().getFirstName() + "\n\n"
							   + "I have made a request for an appointment.\n"
					           + "Please checkout your profile.\n\n"
							   + "Cheers\n"
					           + student.getFirstName() + " " + student.getLastName();
			
			Message message = new Message();
			message.setSender( student );
			message.setReceiver( timeSlot.getTutor() );
			message.setSubject( "New appointment request!" );
			message.setMessage( messageBody );
			messageCenterService.sendMessage( message );
		}else{
			throw new TimeSlotException( "The time-slot you wanted to request is no longer available!" );
		}
		return timeSlot;
	}

	@Override
	public void deleteTimeSlot(long timeSlotId) {
		TimeSlot timeSlot = timeSlotDao.findById( timeSlotId );
		if( timeSlot != null && authenticatedUserLoaderService.getAuthenticatedUser().equals(timeSlot.getTutor())){
			if( timeSlot.getStatus() !=  TimeSlot.Status.AVAILABLE){
				throw new TimeSlotException( "The time-slot you wanted to delete has no longer AVAILABLE as state." );
			}
			
			timeSlotDao.delete(timeSlot);
		}
	}

	@Override
	public TimeSlot acceptTimeSlotRequest(long timeSlotId) {
		TimeSlot timeSlot = timeSlotDao.findById( timeSlotId );
		if( timeSlot != null && authenticatedUserLoaderService.getAuthenticatedUser().equals(timeSlot.getTutor())){
			if( timeSlot.getStatus() !=  TimeSlot.Status.REQUESTED){
				throw new TimeSlotException( "The time-slot request you wanted to accept has no longer REQUESTED as state." );
			}
			
			timeSlot.setStatus(TimeSlot.Status.ACCEPTED);
			timeSlotDao.save(timeSlot);
		}
		return timeSlot;
	}

	@Override
	public TimeSlot rejectTimeSlotRequest(long timeSlotId) {
		TimeSlot timeSlot = timeSlotDao.findById( timeSlotId );
		if( timeSlot != null && authenticatedUserLoaderService.getAuthenticatedUser().equals(timeSlot.getTutor())){
			if( timeSlot.getStatus() !=  TimeSlot.Status.REQUESTED){
				throw new TimeSlotException( "The time-slot request you wanted to reject has no longer REQUESTED as state." );
			}
			
			timeSlot.setStatus(TimeSlot.Status.AVAILABLE);
			timeSlot.setStudent(null);
			timeSlotDao.save(timeSlot);
		}
		return timeSlot;
	}
}
