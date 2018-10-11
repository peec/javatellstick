package org.tellstick.device;

import java.util.ArrayList;

import org.tellstick.JNA;


/**
 * A group can contain many methods and many devices!
 * A SwitchableDeviceImpl group can contain many actions such as Bell, Dim and etc. 
 * 
 * 
 * You must iterate getActions() in this class.
 * Check with instanceof operator.
 * 
 * 
 * Example:
 * --------------
 * GroupDevice gp = new GroupDevice(deviceId);
 * for(TellstickDevice dev : gp.getActions()){
 * 		if (dev instanceof BellDevice){
 * 			// bell method accepted.
 * 		}
 * 		if (dev instanceof DimmableDeviceImpl){
 *          // dim method accepted.
 * 		}
 *      if(dev instanceof SwitchableDeviceImpl){
 * 			// SwitchableDeviceImpl instance. (On / Off accepted )
 * 		}
 * }
 * @author peec
 *
 */
public class GroupDevice extends TellstickDevice{

	/**
	 * Contains collection of instances to BellDevice / DimmableDeviceImpl / SwitchableDeviceImpl.
	 * Can be only BellDevice and etc.
	 */
	ArrayList<TellstickDevice>  deviceActions = new ArrayList<TellstickDevice>();
	
	
	public GroupDevice(int deviceId) throws SupportedMethodsException {
		super(deviceId);
		
		int methods = JNA.CLibrary.INSTANCE.tdMethods(deviceId, getSupportedMethods());
		
		
		// Now single action based.
		if ((methods & JNA.CLibrary.TELLSTICK_BELL) > 0) {
			deviceActions.add(new BellDevice(deviceId));
		}
		
		if((methods & JNA.CLibrary.TELLSTICK_DIM) > 0){
			deviceActions.add(new DimmableDeviceImpl(deviceId));
		}else if((methods & JNA.CLibrary.TELLSTICK_TURNON) > 0 && (methods & JNA.CLibrary.TELLSTICK_TURNOFF) > 0){
			deviceActions.add(new SwitchableDeviceImpl(deviceId));
		}
		
		if((methods & JNA.CLibrary.TELLSTICK_UP) > 0 && (methods & JNA.CLibrary.TELLSTICK_DOWN) > 0 && (methods & JNA.CLibrary.TELLSTICK_STOP) > 0){
			deviceActions.add(new UpDownDevice(deviceId));
		}
		
		if((methods & JNA.CLibrary.TELLSTICK_EXECUTE) > 0){
			deviceActions.add(new SceneDevice(deviceId));
		}
		
	}
	
	/**
	 * Gets all the device actions possible.
	 * 
	 * @return
	 */
	public ArrayList<TellstickDevice> getActions(){
		return deviceActions;
	}
	
	
	/**
	 * Can be used such as:
	 * this.hasDevice(SwitchableDeviceImpl.class, DimmableDeviceImpl.class, BellDevice.class, ... )
	 * Return true if it has the device action.
	 * 
	 * 
	 * @param types
	 * @return
	 */
	public boolean hasDevice(@SuppressWarnings("unchecked") Class<? extends TellstickDevice> ... types){
		
		ArrayList<TellstickDevice> list = getActions();
		for(TellstickDevice i : list){
			for(Class<? extends TellstickDevice> dev : types){
				if (dev.isInstance(i)){
		            return true;
	    		}
			}
		}
		
		return false;
	}
	
	
	public String getType(){
		return "Group";
	}
	
	
}
