package org.tellstick.device.iface;

import org.tellstick.enums.DeviceType;

/**
 * Marker interface for telldus devices
 * @author JarleB
 *
 */
public interface Device {
	public int getId();
	public String getProtocol();
	public String getModel();
	public DeviceType getDeviceType();
}
