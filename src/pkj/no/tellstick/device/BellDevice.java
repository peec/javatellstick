package pkj.no.tellstick.device;

import pkj.no.tellstick.JNA;


public class BellDevice extends TellstickDevice implements pkj.no.tellstick.device.iface.BellDevice{

	public BellDevice(int deviceId) throws SupportedMethodsException {
		super(deviceId);
	}

	@Override
	public void bell() throws TellstickException {
		int status = JNA.CLibrary.INSTANCE.tdBell(getId());
		if (status != TELLSTICK_SUCCESS)throw new TellstickException(this, status);
	}
	
	public String getType(){
		return "Bell Device";
	}

}
