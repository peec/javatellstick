package org.tellstick.enums;

import org.tellstick.JNA.CLibrary;

public enum DeviceType {
	GROUP(CLibrary.TELLSTICK_TYPE_GROUP), DEVICE(CLibrary.TELLSTICK_TYPE_DEVICE), SCENE(CLibrary.TELLSTICK_TYPE_SCENE), SENSOR(99);

	private int nativeInt;

	DeviceType(int nativeType) {
		this.nativeInt = nativeType;
	}

	public static DeviceType getDeviceTypeById(int nativeId) {
		for (DeviceType m : DeviceType.values()) {
			if (m.nativeInt == nativeId) {
				return m;
			}
		}
		throw new RuntimeException("DeviceType not found for " + nativeId);
	}

}