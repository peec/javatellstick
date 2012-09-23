package pkj.no.tellstick.device;

import java.util.ArrayList;

abstract public class DeviceChangeListener {

	/**
	 * This event listener must be implemented.
	 * This is the method that will get called if we got requests.
	 */
	abstract public void onRequest(ArrayList<TellstickDevice> newDevices);
	
}
