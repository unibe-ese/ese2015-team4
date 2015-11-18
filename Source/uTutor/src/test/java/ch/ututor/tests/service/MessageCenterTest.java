package ch.ututor.tests.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.ututor.exceptions.custom.MessageNotFoundException;
import ch.ututor.model.Message;
import ch.ututor.model.User;
import ch.ututor.model.dao.MessageDao;
import ch.ututor.pojos.NewMessageForm;
import ch.ututor.service.interfaces.AuthenticatedUserLoaderService;
import ch.ututor.service.interfaces.MessageCenterService;
import ch.ututor.service.interfaces.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( locations = { "file:src/main/webapp/WEB-INF/test/messageCenterService.xml" } )
public class MessageCenterTest {
	@Autowired MessageDao messageDao;
	@Autowired UserService userService;
	@Autowired AuthenticatedUserLoaderService authenticatedUserLoaderService;
	@Autowired MessageCenterService messageCenterService;
	
	private static User lenny;
	private static User carl;
	private long messageCount = 0;
	
	@BeforeClass
	public static void setUpCarlAndLenny(){
		carl = new User();
		carl.setId( 1L );
		carl.setFirstName( "Carl" );
		carl.setLastName( "Sender" );
		carl.setUsername( "carl@sender.com" );
		
		lenny = new User();
		lenny.setId( 2L );
		lenny.setFirstName( "Lenny" );
		lenny.setLastName( "Receiver" );
		lenny.setUsername( "lenny@receiver.com" );
	}
	
	private Message setUpMessage( User sender, User receiver ){
		messageCount++;
		Message message = new Message();
		message.setId( messageCount );
		message.setSender( sender );
		message.setReceiver( receiver );
		message.setSubject( "Hello World!" );
		message.setMessage( "This is the message." );
		return message;
	}
	
	private void setUpMockAuthenticatedUser( User user ){
		when( authenticatedUserLoaderService.getAuthenticatedUser() ).thenReturn( user );
	}
	
	private void setUpMockUserServiceLoad( User user ){
		when( userService.load( user.getId() ) ).thenReturn( user );
	}
	
	private void setUpMockMessageDaoSave(){
		when( messageDao.save( any( Message.class ) ) ).then( returnsFirstArg() );
	}
	private void setUpMockMessageDaoFindById( Message message ){
		when( messageDao.findById( message.getId() ) ).thenReturn(message);
	}
	
	private void setUpMocksForLennysViews(){
		Message inboxMessage = setUpMessage( carl, lenny );
		Message outboxMessage = setUpMessage( lenny, carl );
		Message trashMessageA = setUpMessage( carl, lenny);
		trashMessageA.setReceiverDeleted( true );
		Message trashMessageB = setUpMessage( lenny, carl );
		trashMessageB.setSenderDeleted( true );
		
		when( messageDao.findByReceiverAndReceiverDeletedOrderByDateAndTimeDesc( lenny, false ) ).thenReturn( Arrays.asList( inboxMessage ) );
		when( messageDao.findBySenderAndSenderDeletedOrderByDateAndTimeDesc( lenny, false ) ).thenReturn( Arrays.asList( outboxMessage ) );
		when( messageDao.findBySenderAndSenderDeletedOrReceiverAndReceiverDeletedOrderByDateAndTimeDesc( lenny, true, lenny, true ) ).thenReturn( Arrays.asList( trashMessageA, trashMessageB ) );		
	}
	
	@Test
	public void getMessagesByViewForLennyTest(){
		setUpMockAuthenticatedUser( lenny );
		setUpMocksForLennysViews();
		
		List<Message> inbox = messageCenterService.getMessagesByView( "inbox" );
		assertEquals( 1, inbox.size() );
		assertEquals( lenny, inbox.get(0).getReceiver() );
		
		List<Message> outbox = messageCenterService.getMessagesByView( "outbox" );
		assertEquals( 1, outbox.size() );
		assertEquals( lenny, outbox.get(0).getSender() );
		
		List<Message> trash = messageCenterService.getMessagesByView( "trash" );
		assertEquals( 2, trash.size() );
		assertEquals( lenny, trash.get(0).getReceiver() );
		assertEquals( lenny, trash.get(1).getSender() );
	}
	
	@Test
	public void normalizeLongTest(){
		long lng;
		lng = messageCenterService.normalizeLong( "123" );
		assertEquals( 123L, lng );
		
		lng = messageCenterService.normalizeLong( "A" );
		assertEquals( 0L, lng );
		
		lng = messageCenterService.normalizeLong( null );
		assertEquals( 0L, lng );
	}
	
	@Test
	public void normalizeStringTest(){
		String str;
		str = messageCenterService.normalizeString( " abc " );
		assertEquals( "abc" , str );
		
		str = messageCenterService.normalizeString( null );
		assertEquals( "", str );
	}
	
	@Test
	public void normalizeViewTest(){
		String str;
		str = messageCenterService.normalizeView( "inbox" );
		assertEquals( "inbox", str );
		
		str = messageCenterService.normalizeView( "outbox" );
		assertEquals( "outbox", str );
		
		str = messageCenterService.normalizeView( "trash" );
		assertEquals( "trash", str );
		
		str = messageCenterService.normalizeView( " trash " );
		assertEquals( "trash", str );
		
		str = messageCenterService.normalizeView( "somethingelse" );
		assertEquals( "inbox", str );
		
		str = messageCenterService.normalizeView( null );
		assertEquals( "inbox", str );
	}
	
	@Test
	public void deleteMessageSenderTest(){
		setUpMockAuthenticatedUser( lenny );
		setUpMockMessageDaoSave();

		Message message = setUpMessage( lenny, carl );
		setUpMockMessageDaoFindById( message );
		
		message = messageCenterService.deleteMessage( message.getId() );
		assertTrue( message.getSenderDeleted() );
		assertFalse( message.getReceiverDeleted() );
	}
	
	@Test
	public void deleteMessageReceiverTest(){
		setUpMockAuthenticatedUser( lenny );
		setUpMockMessageDaoSave();

		Message message = setUpMessage( carl, lenny );
		setUpMockMessageDaoFindById( message );
		
		message = messageCenterService.deleteMessage( message.getId() );
		assertFalse( message.getSenderDeleted() );
		assertTrue( message.getReceiverDeleted() );
	}
	
	@Test
	public void prefillNewMessageFormTest(){
		setUpMockUserServiceLoad( carl );
		NewMessageForm newMessageForm = messageCenterService.prefillNewMessageForm( carl.getId() );
		
		assertEquals( Long.valueOf( carl.getId() ), Long.valueOf( newMessageForm.getReceiverId() ) );
		assertEquals( carl.getFirstName() + " " + carl.getLastName(), newMessageForm.getReceiverDisplayName() );
	}
	
	@Test
	public void prefillReplyMessageForm(){
		setUpMockUserServiceLoad( carl );
		Message message = setUpMessage( carl, lenny );
		setUpMockMessageDaoFindById( message );
		NewMessageForm newMessageForm = messageCenterService.prefillReplyMessageForm( message.getId() );
		
		assertEquals( Long.valueOf( carl.getId() ), Long.valueOf( newMessageForm.getReceiverId() ) );
		assertEquals( carl.getFirstName() + " " + carl.getLastName(), newMessageForm.getReceiverDisplayName() );
		assertEquals( "Re: " + message.getSubject(), newMessageForm.getSubject() );
	}
	
	@Test(expected = MessageNotFoundException.class)
	public void prefillReplyMessageNotFoundTest(){
		messageCenterService.prefillReplyMessageForm( 0 );
	}
	
	@Test
	public void sendMessageTest(){
		setUpMockAuthenticatedUser( lenny );
		setUpMockUserServiceLoad( carl );
		setUpMockMessageDaoSave();
		
		NewMessageForm newMessageForm = new NewMessageForm();
		newMessageForm.setReceiverId( carl.getId() );
		newMessageForm.setReceiverDisplayName( carl.getFirstName()+" "+carl.getLastName() );
		newMessageForm.setSubject( "Hello Carl!" );
		newMessageForm.setMessage( "How are you?" );
		
		Message message = messageCenterService.sendMessage( newMessageForm );
		assertEquals( lenny, message.getSender() );
		assertEquals( carl, message.getReceiver() );
		assertEquals( newMessageForm.getSubject(), message.getSubject() );
		assertEquals( newMessageForm.getMessage(), message.getMessage() );
	}
	
	@Test
	public void setReadReceiverTest(){
		setUpMockAuthenticatedUser( carl );
		setUpMockMessageDaoSave();
		Message message = setUpMessage( lenny, carl );
		setUpMockMessageDaoFindById( message );
		message = messageCenterService.setRead( message.getId() );
		assertTrue( message.getIsRead() );
	}
	
	@Test
	public void setReadSenderTest(){
		setUpMockAuthenticatedUser( lenny );
		setUpMockMessageDaoSave();
		Message message = setUpMessage( lenny, carl );
		setUpMockMessageDaoFindById( message );
		message = messageCenterService.setRead( message.getId() );
		assertFalse( message.getIsRead() );
	}
}