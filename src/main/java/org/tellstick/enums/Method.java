package org.tellstick.enums;

import org.tellstick.JNA.CLibrary;

public enum Method {
	TURNON(CLibrary.TELLSTICK_TURNON), TURNOFF(CLibrary.TELLSTICK_TURNOFF), DIM(CLibrary.TELLSTICK_DIM), LEARN(
			CLibrary.TELLSTICK_LEARN), DEVICE_ADDED(CLibrary.TELLSTICK_DEVICE_ADDED), DEVICE_CHANGED(
			CLibrary.TELLSTICK_DEVICE_CHANGED), DEVICE_REMOVED(CLibrary.TELLSTICK_DEVICE_REMOVED);

	private int nativeInt;

	Method(int nativeMethod) {
		this.nativeInt = nativeMethod;
	}

	public static Method getMethodById(int nativeId) {
		for (Method m : Method.values()) {
			if (m.nativeInt == nativeId) {
				return m;
			}
		}
		throw new RuntimeException("Method not found for " + nativeId);
	}

}