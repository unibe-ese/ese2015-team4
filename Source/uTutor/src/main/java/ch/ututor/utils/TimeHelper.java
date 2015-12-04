package ch.ututor.utils;

import java.util.Calendar;
import java.util.Date;

public class TimeHelper {
	
	public static boolean isFuture( Date date ){
		return new Date().before( date );
	}
	
	public static boolean isPast( Date date ){
		return new Date().after( date );
	}
	
	public static Date addDays( Date date, int days ){
		Calendar cal = Calendar.getInstance();
		cal.setTime( date );
		cal.add( Calendar.DATE, days );
		
		return cal.getTime();
	}
}
