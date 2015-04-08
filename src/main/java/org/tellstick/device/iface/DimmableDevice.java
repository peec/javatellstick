package org.tellstick.device.iface;

import org.tellstick.device.TellstickException;

/**
 * A device that can be dimmed.
 * @author peec
 *
 */
public interface DimmableDevice extends SwitchableDevice{

	/**
	 * Dims lights to a certain level.
	 */
	public void dim(int level) throws TellstickException;
	
	
}
