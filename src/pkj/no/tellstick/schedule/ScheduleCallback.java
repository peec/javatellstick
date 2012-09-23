package pkj.no.tellstick.schedule;

import pkj.no.tellstick.device.TellstickDevice;

abstract public class ScheduleCallback {
	
	
	abstract public void onTrigger(TellstickDevice device);
	
}
