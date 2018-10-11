package org.tellstick.device.iface;

import org.tellstick.device.TellstickException;

/**
 * A generic device.
 * @author peec
 *
 */
public interface SwitchableDevice{

	
	
	/**
	 * Turns on the device.
	 * @return 
	 */
	public void on() throws TellstickException;
	
	/**
	 * Turns off the device.
	 */
	public void off() throws TellstickException;
	
	
	/**
	 * Returns the name of the device.
	 * @return
	 */
	public String getType();
	
}
