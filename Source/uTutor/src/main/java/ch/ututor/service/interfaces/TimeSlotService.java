package ch.ututor.service.interfaces;

import java.text.ParseException;
import java.util.List;

import ch.ututor.model.TimeSlot;
import ch.ututor.model.User;
import ch.ututor.pojos.AddTimeslotsForm;

public interface TimeSlotService {
	
	public List<TimeSlot> addTimeSlots(AddTimeslotsForm addTimeSlotsForm) throws ParseException;
	
	public List<TimeSlot> getTimeSlotsByUser(User user);
	
	public List<String> getPossibleTimeslots();
	
	public TimeSlot requestForTimeSlot( long timeSlotId );
	
	public void deleteTimeSlot( long timeSlotId );
	
	public TimeSlot acceptTimeSlotRequest( long timeSlotId );
	
	public TimeSlot rejectTimeSlotRequest( long timeSlotId );
	
	public TimeSlot rateTimeSlot( long timeSlotId, int rating );
	
	public Integer getTimeSlotAvgRatingByTutor( User tutor );
	
}
