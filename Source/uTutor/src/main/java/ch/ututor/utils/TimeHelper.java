package ch.ututor.utils;

import java.util.Date;

public class TimeHelper {
	public static boolean isFuture(Date date){
		return new Date().before(date);
	}
	
	public static boolean isPast(Date date){
		return new Date().after(date);
	}
}
