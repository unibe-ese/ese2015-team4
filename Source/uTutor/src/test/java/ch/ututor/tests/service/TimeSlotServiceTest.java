package ch.ututor.tests.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
import ch.ututor.utils.TimeHelper;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalAnswers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/test/resources/timeSlotService.xml"})
public class TimeSlotServiceTest {

	@Autowired TimeSlotService timeSlotService;
	@Autowired TimeSlotDao timeSlotDao;
	@Autowired AuthenticatedUserLoaderService authenticatedUserLoaderService;
	@Autowired MessageCenterService messageCenterService;
	private AddTimeslotsForm addTimeSlotsForm;
	private List<TimeSlot> timeSlotList;
	private List<String> posTimeSlot;
	private User user;
	private User tutor;
	private User student;
	
	@Before
	public void setupTimeSlotData(){
		setupUser();
		
		user = new User();
		
		List<String> timeSlots = new ArrayList<String>();
		timeSlots.add("08:00 - 08:59");
		
		addTimeSlotsForm = new AddTimeslotsForm();
		addTimeSlotsForm.setDate("2015-12-24");
		addTimeSlotsForm.setTimeslots(timeSlots);
		
		when(timeSlotDao.save(any(TimeSlot.class))).then(returnsFirstArg());
		when( authenticatedUserLoaderService.getAuthenticatedUser() ).thenReturn( user );
		when(timeSlotDao.findByBeginDateTimeAndTutor(any(Date.class), any(User.class))).thenReturn(null);
	}

	private void setupUser() {
		tutor = new User();
		long id = 1;
		tutor.setId(id);
		
		student = new User();
		long otherId = 2;
		student.setId(otherId);
	}

	@Test
	public void testValidTimeSlotList(){
		posTimeSlot = timeSlotService.getPossibleTimeslots();
		
		assertTrue(!posTimeSlot.isEmpty());
		assertEquals(posTimeSlot.size(), 17);
		assertEquals(posTimeSlot.get(0), "06:00 - 06:59");
	}
	
	//TODO: Ab 24.12.15 ist der Test ungültig
	@Test
	public void testValidAddTimeSlots() throws ParseException{
		timeSlotList = timeSlotService.addTimeSlots(addTimeSlotsForm);
		
		TimeSlot timeSlot = timeSlotList.get(0);
		
		assertTrue(!timeSlotList.isEmpty());
		assertEquals(timeSlotList.size(), 1);
		assertEquals(timeSlot.getStatus(), TimeSlot.Status.AVAILABLE);
		assertEquals(timeSlot.getBeginDateTime().toString(), "Thu Dec 24 08:00:00 CET 2015");
		assertEquals(timeSlot.getStudent(), null);
		assertEquals(timeSlot.getTutor(), user);
	}
	
	@Test(expected = InvalidDateException.class)
	public void testInvalidDateFormatNoPointsBetweenNumbers() throws ParseException{
		addTimeSlotsForm = new AddTimeslotsForm();
		addTimeSlotsForm.setDate("2015.12.24");
		
		timeSlotList = timeSlotService.addTimeSlots(addTimeSlotsForm);
	}
	
	@Test(expected = InvalidDateException.class)
	public void testInvalidDateBeforeToday() throws ParseException{
		addTimeSlotsForm = new AddTimeslotsForm();
		addTimeSlotsForm.setDate("2014-12-24");
		
		timeSlotList = timeSlotService.addTimeSlots(addTimeSlotsForm);
	}
	/*
	 * DateParser automatically turns 13th month of the year in January of the next year
	 */
	@Test
	public void testValidWirdDate() throws ParseException{
		addTimeSlotsForm = new AddTimeslotsForm();
		addTimeSlotsForm.setDate("2015-13-24");
		addTimeSlotsForm.setTimeslots(new ArrayList<String>());
		
		timeSlotList = timeSlotService.addTimeSlots(addTimeSlotsForm);
	}
	
	@Test(expected = NullPointerException.class)
	public void testNoTimeSlots() throws ParseException{
		addTimeSlotsForm = new AddTimeslotsForm();
		addTimeSlotsForm.setDate("2015-12-24");
		
		timeSlotList = timeSlotService.addTimeSlots(addTimeSlotsForm);
	}
	
	@Test
	public void testTimeSlotAlreadyExists() throws ParseException{
		when(timeSlotDao.findByBeginDateTimeAndTutor(any(Date.class), any(User.class))).thenReturn(new TimeSlot());
		
		addTimeSlotsForm = new AddTimeslotsForm();
		addTimeSlotsForm.setDate("2015-12-24");
		addTimeSlotsForm.setTimeslots(new ArrayList<String>());
		
		timeSlotList = timeSlotService.addTimeSlots(addTimeSlotsForm);
	}
	
	private void setupTimeSlotMock(int days){
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setStatus(TimeSlot.Status.AVAILABLE);
		timeSlot.setTutor(tutor);
		timeSlot.setBeginDateTime(TimeHelper.addDays(new Date(), days));
		
		when( authenticatedUserLoaderService.getAuthenticatedUser() ).thenReturn(student);
		when(timeSlotDao.findById(any(Long.class))).thenReturn(timeSlot);
		when(messageCenterService.sendMessage(any(Message.class))).then(returnsFirstArg());
	}
	
	@Test
	public void testValidRequestForTimeslot(){
		setupTimeSlotMock(1);
		TimeSlot timeSlot = timeSlotService.requestForTimeSlot(1L);
		assertEquals(TimeSlot.Status.REQUESTED, timeSlot.getStatus());
		assertEquals(student, timeSlot.getStudent());
	}
	
	@Test(expected = TimeSlotException.class)
	public void testInvalidRequestTimeslotIsInThePast(){
		setupTimeSlotMock(-1);
		timeSlotService.requestForTimeSlot(1L);
	}
	
	@Test(expected = TimeSlotException.class)
	public void testNoTimeSlotOnDatabase(){
		
		when(timeSlotDao.findById(any(Long.class))).thenReturn(null);
		
		timeSlotService.requestForTimeSlot(1);
	}
	/*
	 * Same Exception would be thrown by enum state Accepted
	 */
	@Test(expected = TimeSlotException.class)
	public void testWrongEnumState(){
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setStatus(TimeSlot.Status.REQUESTED);
		timeSlot.setTutor(tutor);
		
		long anyIdNumber = 1;
		
		when( authenticatedUserLoaderService.getAuthenticatedUser() ).thenReturn(student);
		when(timeSlotDao.findById(any(Long.class))).thenReturn(timeSlot);
		
		timeSlot = timeSlotService.requestForTimeSlot(anyIdNumber);
	}
	
	@Test(expected = TimeSlotException.class)
	public void testTutorAndStudentAreTheSame(){
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setStatus(TimeSlot.Status.AVAILABLE);
		timeSlot.setTutor(tutor);
		
		long anyIdNumber = 1;
		
		when( authenticatedUserLoaderService.getAuthenticatedUser() ).thenReturn(tutor);
		when(timeSlotDao.findById(any(Long.class))).thenReturn(timeSlot);
		
		timeSlot = timeSlotService.requestForTimeSlot(anyIdNumber);
	}
	
	@Test(expected = TimeSlotException.class)
	public void testInvalidTimeSlotDelete(){
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setStatus(TimeSlot.Status.ACCEPTED);
		timeSlot.setTutor(tutor);
		
		when( authenticatedUserLoaderService.getAuthenticatedUser() ).thenReturn(tutor);
		when(timeSlotDao.findById(any(Long.class))).thenReturn(timeSlot);
		
		timeSlotService.deleteTimeSlot(1);
	}
	
	@Test
	public void testValidTimeSlotAcception(){
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setStatus(TimeSlot.Status.REQUESTED);
		timeSlot.setTutor(tutor);
		timeSlot.setBeginDateTime(TimeHelper.addDays(new Date(), 1));
		
		when( authenticatedUserLoaderService.getAuthenticatedUser() ).thenReturn(tutor);
		when(timeSlotDao.findById(any(Long.class))).thenReturn(timeSlot);
		
		timeSlot = timeSlotService.acceptTimeSlotRequest(1);
		
		assertEquals(TimeSlot.Status.ACCEPTED, timeSlot.getStatus());
	}
	
	@Test(expected = TimeSlotException.class)
	public void testInvalidTimeSlotAcceptionStatusIsNotRequested(){
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setStatus(TimeSlot.Status.AVAILABLE);
		timeSlot.setTutor(tutor);
		
		when( authenticatedUserLoaderService.getAuthenticatedUser() ).thenReturn(tutor);
		when(timeSlotDao.findById(any(Long.class))).thenReturn(timeSlot);
		
		timeSlot = timeSlotService.acceptTimeSlotRequest(1);
	}
	
	@Test(expected = TimeSlotException.class)
	public void testInvalidTimeSlotAcceptionDateIsInThePast(){
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setStatus(TimeSlot.Status.REQUESTED);
		timeSlot.setTutor(tutor);
		timeSlot.setBeginDateTime(TimeHelper.addDays(new Date(), -1));
		
		when( authenticatedUserLoaderService.getAuthenticatedUser() ).thenReturn(tutor);
		when(timeSlotDao.findById(any(Long.class))).thenReturn(timeSlot);
		
		timeSlot = timeSlotService.acceptTimeSlotRequest(1);
	}
	
	@Test
	public void testValidTimeSlotRejection(){
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setStatus(TimeSlot.Status.REQUESTED);
		timeSlot.setTutor(tutor);
		
		when( authenticatedUserLoaderService.getAuthenticatedUser() ).thenReturn(tutor);
		when(timeSlotDao.findById(any(Long.class))).thenReturn(timeSlot);
		
		timeSlot = timeSlotService.rejectTimeSlotRequest(1);
		
		assertEquals(TimeSlot.Status.AVAILABLE, timeSlot.getStatus());
	}
	
	@Test(expected = TimeSlotException.class)
	public void testInvalidTimeSlotRejection(){
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setStatus(TimeSlot.Status.ACCEPTED);
		timeSlot.setTutor(tutor);
		timeSlot.setBeginDateTime(TimeHelper.addDays(new Date(), 1));
		
		when( authenticatedUserLoaderService.getAuthenticatedUser() ).thenReturn(tutor);
		when(timeSlotDao.findById(any(Long.class))).thenReturn(timeSlot);
		
		timeSlot = timeSlotService.rejectTimeSlotRequest(1);
	}
	
	@Test
	public void testValidRating(){
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setStatus(TimeSlot.Status.ACCEPTED);
		timeSlot.setTutor(tutor);
		timeSlot.setStudent(student);
		timeSlot.setBeginDateTime(TimeHelper.addDays(new Date(), -1));
		
		when(timeSlotDao.findById(any(Long.class))).thenReturn(timeSlot);
		when( authenticatedUserLoaderService.getAuthenticatedUser() ).thenReturn(student);
		
		int rating = 5;
		
		timeSlot = timeSlotService.rateTimeSlot(1, rating);
		
		assertEquals(rating , (int)timeSlot.getRating());
	}
	
	@Test(expected = TimeSlotException.class)
	public void testInvalidRatingTimeSlotIsInTheFutur(){
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setStatus(TimeSlot.Status.ACCEPTED);
		timeSlot.setTutor(tutor);
		timeSlot.setStudent(student);
		timeSlot.setBeginDateTime(TimeHelper.addDays(new Date(), 1));
		
		when(timeSlotDao.findById(any(Long.class))).thenReturn(timeSlot);
		when( authenticatedUserLoaderService.getAuthenticatedUser() ).thenReturn(student);
		
		timeSlot = timeSlotService.rateTimeSlot(1, 5);
	}
	
	@Test(expected = TimeSlotException.class)
	public void testInvalidRatingUnknownGrade(){
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setStatus(TimeSlot.Status.ACCEPTED);
		timeSlot.setTutor(tutor);
		timeSlot.setStudent(student);
		timeSlot.setBeginDateTime(TimeHelper.addDays(new Date(), -1));
		
		when(timeSlotDao.findById(any(Long.class))).thenReturn(timeSlot);
		when( authenticatedUserLoaderService.getAuthenticatedUser() ).thenReturn(student);
		
		timeSlot = timeSlotService.rateTimeSlot(1, 0);
	}
}
