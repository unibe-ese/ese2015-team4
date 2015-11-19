package ch.ututor.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ututor.exceptions.custom.InvalidDateException;
import ch.ututor.model.TimeSlot;
import ch.ututor.model.User;
import ch.ututor.model.dao.TimeSlotDao;
import ch.ututor.pojos.AddTimeslotsForm;
import ch.ututor.service.interfaces.AuthenticatedUserLoaderService;
import ch.ututor.service.interfaces.TimeSlotService;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {
	
	@Autowired 	private AuthenticatedUserLoaderService authenticatedUserLoaderService;
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
}
