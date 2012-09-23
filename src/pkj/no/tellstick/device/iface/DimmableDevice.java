package pkj.no.tellstick.device.iface;

import pkj.no.tellstick.device.TellstickException;

/**
 * A device that can be dimmed.
 * @author peec
 *
 */
public interface DimmableDevice extends Device{

	/**
	 * Dims lights to a certain level.
	 */
	public void dim(int level) throws TellstickException;
	
	
}
