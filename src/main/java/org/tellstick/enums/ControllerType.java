package org.tellstick.enums;

public enum ControllerType {
	TELLSTICK(1),
	TELLSTICK_DUO(2),
	TELLSTICK_NET(3);

	private final int id;
	private ControllerType(int id) {
		this.id = id;
	}
	public static ControllerType findDeviceType(int id) {
		ControllerType type = null;
		for (ControllerType p : ControllerType.values()) {
		   if (p.id == id) {
			   type = p;
		   }
		}
		if (type == null) {
			throw new RuntimeException("Could not find ControllerType:"+id);
		}
		return type;
	}
	public String toString() {
		return this.name()+"("+this.id+")";
	}
}
