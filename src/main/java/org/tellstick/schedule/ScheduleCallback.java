package org.tellstick.schedule;

import org.tellstick.device.TellstickDevice;

abstract public class ScheduleCallback {
	
	
	abstract public void onTrigger(TellstickDevice device);
	
}
