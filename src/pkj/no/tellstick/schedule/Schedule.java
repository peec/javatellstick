package pkj.no.tellstick.schedule;

import java.util.Calendar;

import pkj.no.tellstick.device.TellstickDevice;

public class Schedule {

	final static public int ALL_DAYS = 1;
	final static public int MONDAY = 2;
	final static public int TUESDAY = 4;
	final static public int WEDNESDAY = 8;
	final static public int THURSDAY = 16;
	final static public int FRIDAY = 32;
	final static public int SATURDAY = 64;
	final static public int SUNDAY = 128;
	
	
	
	private int dayFlags;
	private int hour;
	private int minute;
	private TellstickDevice device;
	private ScheduleCallback callback;
	
	
	private int lastRunHour=Integer.MIN_VALUE;
	
	/**
	 * Creates a new shedule 
	 * @param dayFlags Flags such as MONDAY | TUESDAY | WEDNESDAY .. or ALL_DAYS
	 * @param hour Hour, 24-hour format. 0-23
	 * @param minute 0-59
	 * @param device TellstickDevice instance, what device you want to do actions on.
	 * @param callback Callback class to run whenever we got action.
	 */
	public Schedule(int dayFlags, int hour, int minute, TellstickDevice device, ScheduleCallback callback){
		
		this.hour = hour;
		this.minute = minute;
		this.dayFlags = dayFlags;
		this.device = device;
		this.callback = callback;
	
	}
	
	public Schedule(int dayFlags, TellstickDevice device, ScheduleCallback callback){
		this.dayFlags = dayFlags;
		this.device = device;
		this.callback = callback;
		
	}
	
	public int getDayFlags(){
		return dayFlags;
	}
	
	public TellstickDevice getDevice(){
		return device;
	}
	
	public ScheduleCallback getCallback(){
		return callback;
	}
	
	/**
	 * Runs the schedule.
	 */
	public void run(){
		Calendar cal = Calendar.getInstance();

		int hr = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		
		// Roll one hour up, and set when to release lock of run.
		cal.roll(Calendar.HOUR_OF_DAY, true);
		int releaseHour = cal.get(Calendar.HOUR_OF_DAY);
		
		boolean runCallback=false;
		
		if (correctDay(cal)){
			if (hour == hr && minute==min){
				if (hour != lastRunHour){
				
					runCallback = true;
				
					lastRunHour = hr;
				}
			}else if(hr == releaseHour){
				lastRunHour = Integer.MIN_VALUE;
			}
		}
		
		if (runCallback)getCallback().onTrigger(device);
	}
	
	protected boolean correctDay(Calendar cal){
		int day = cal.get(Calendar.DAY_OF_WEEK);
		
		return (ALL_DAYS & dayFlags) > 0 ||
		(
				((day == Calendar.MONDAY) && (MONDAY & dayFlags) > 0) ||
				((day == Calendar.TUESDAY) && (TUESDAY & dayFlags) > 0) ||
				((day == Calendar.WEDNESDAY) && (WEDNESDAY & dayFlags) > 0) ||
				((day == Calendar.THURSDAY) && (THURSDAY & dayFlags) > 0) ||
				((day == Calendar.FRIDAY) && (FRIDAY & dayFlags) > 0) ||
				((day == Calendar.SATURDAY) && (SATURDAY & dayFlags) > 0) ||
				((day == Calendar.SUNDAY) && (SUNDAY & dayFlags) > 0)
		);
	}
}
