package org.tellstick.device.iface;

import org.tellstick.device.TellstickException;

/**
 * Devices that can perform the action bell.
 * Does not implement super interface SwitchableDeviceImpl since this should only perform the bell action.
 * @author peec
 *
 */
public interface BellDevice {

	
	/**
	 * Bells the bell, meaning a sound will be played from the belling device.
	 * @throws TellstickException 
	 */
	public void bell() throws TellstickException;
	
	/**
	 * Returns the name of the device.
	 * @return
	 */
	public String getType();
	
}
