package org.tellstick.device;

public class DeviceNotSupportedException extends Exception{
	
	private static final long serialVersionUID = 8017872316471887150L;

	public DeviceNotSupportedException(String message){
		super(message);
	}
}
