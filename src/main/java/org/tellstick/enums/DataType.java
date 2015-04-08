package org.tellstick.enums;

import java.util.ArrayList;
import java.util.List;

import org.tellstick.JNA.CLibrary;

public enum DataType {
	HUMIDITY(CLibrary.TELLSTICK_HUMIDITY), TEMPERATURE(CLibrary.TELLSTICK_TEMPERATURE),
	WINDAVERAGE(CLibrary.TELLSTICK_WINDAVERAGE), WINDDIRECTION(CLibrary.TELLSTICK_WINDDIRECTION),
	WINDGUST(CLibrary.TELLSTICK_WINDGUST), RAINRATE(CLibrary.TELLSTICK_RAINRATE), RAINTOTAL(CLibrary.TELLSTICK_RAINTOTAL);

	private int nativeInt;

	DataType(int nativeMethod) {
		this.nativeInt = nativeMethod;
	}

	public static DataType[] getDataTypesById(int nativeId) {
		List<DataType> results = new ArrayList<DataType>();
		for (DataType m : DataType.values()) {
			if ((m.nativeInt & nativeId) != 0) {
				results.add(m);
			}
		}
		return results.toArray(new DataType[0]);
	}
	public int getTellstickId() {
		return this.nativeInt;
	}
}