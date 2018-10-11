package org.tellstick.device;

import org.tellstick.JNA;


public class DimmableDeviceImpl extends SwitchableDeviceImpl implements org.tellstick.device.iface.DimmableDevice{


	public DimmableDeviceImpl(int deviceId) throws SupportedMethodsException {
		super(deviceId);
	}

	
	/**
	 * Dims the lights.
	 * @throws TellstickException 
	 * @throws IllegalArguementException
	 */
	@Override
	public void dim(int level) throws TellstickException {
		if (level < 0 || level > 255)throw new IllegalArgumentException("Dim levels must be between 0 and 255.");
		int status = JNA.CLibrary.INSTANCE.tdDim(getId(), level);
		if (status != TELLSTICK_SUCCESS)throw new TellstickException(this, status);
	}
	
	/**
	 * Since Dimmers can be dimmed, we override the SwitchableDeviceImpl::isOn.
	 * This checks if the device is turned on.
	 * @return true if device is on. false otherwise.
	 */
	@Override
	public boolean isOn(){
		if (super.isOn() || (JNA.CLibrary.TELLSTICK_DIM & this.getStatus()) > 0)return true;
		else return false;
	}
	
	@Override
	public String getType(){
		return "Dimmer Device";
	}

}
