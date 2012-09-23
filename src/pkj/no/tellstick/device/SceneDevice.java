package pkj.no.tellstick.device;

import pkj.no.tellstick.JNA;

public class SceneDevice extends TellstickDevice{

	public SceneDevice(int deviceId) throws SupportedMethodsException {
		super(deviceId);
	}
	
	/**
	 * Executes Scene.
	 * 
	 * @throws TellstickException
	 */
	public void execute() throws TellstickException{
		int status = JNA.CLibrary.INSTANCE.tdExecute(getId());
		if (status != TELLSTICK_SUCCESS)throw new TellstickException(this, status);
	}
	
	public String getType(){
		return "Scene";
	}
}
