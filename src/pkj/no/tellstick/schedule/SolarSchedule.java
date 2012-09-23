package pkj.no.tellstick.schedule;

import java.util.Calendar;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

import pkj.no.tellstick.device.TellstickDevice;

public class SolarSchedule extends Schedule{

	private boolean sunrise;
	
	public SolarSchedule(int dayFlags, TellstickDevice device, ScheduleCallback callback, boolean sunrise){
		super(dayFlags, device, callback);
		
		this.sunrise = sunrise;
	}
	
	/**
	 * Runs the schedule.
	 */
	@Override
	public void run(){
		Calendar cal = Calendar.getInstance();

		int hr = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		
		// Roll one hour up, and set when to release lock of run.
		cal.roll(Calendar.HOUR_OF_DAY, true);
		int releaseHour = cal.get(Calendar.HOUR_OF_DAY);
		
		boolean runCallback=false;
		
		
		// Location of sunrise/set, as latitude/longitude.  
		Location location = new Location("39.9937", "-75.7850");  
		  
		// Create calculator object with the location and time zone identifier.  
		SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, "America/New_York");  
		
		if (correctDay(cal)){
			
		}
		
		if (runCallback)getCallback().onTrigger(getDevice());
	}
		
	
}
