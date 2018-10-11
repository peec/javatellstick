package org.tellstick.device;

import java.util.Collections;
import java.util.List;

import org.tellstick.JNA;
import org.tellstick.device.iface.DeviceChangeListener;
import org.tellstick.enums.Method;

import com.sun.jna.Pointer;

/**
 * This listener will update / add objects automatically to a list of tellstick devices.
 * This will use the C# Library to find out if devices has changed anything.
 * 
 * It will make your list of devices updated all the time.
 * 
 * Remember to do remove() when exit of the application.
 * 
 * 
 * Usage can be:
 * 
		// Create a listener, to keep in track of live statuses.
		final TellstickDeviceListener listener = new TellstickDeviceListener(devices, new DeviceChangeListener(){
			public void onRequest(ArrayList<TellstickDevice> newDevices) {
				devices = newDevices;
				// Create async request.
				Application.display.syncExec(new Runnable() {
					public void run() {
						table.removeAll();
						printDevices();
					}
				});

			}			
		});

 * 
 * 
 * @author peec
 *
 */
public class TellstickDeviceListener extends Thread implements Runnable{

	List<TellstickDevice> list;
	DeviceChangeListener changeListener;
	
	private boolean run = true;
	
	
	public TellstickDeviceListener(List<TellstickDevice> list, DeviceChangeListener changeListener){
		
		this.list = list;
		this.changeListener = changeListener;
		this.start();
	}
	
	
	
	public void remove(){
		run = false;
	}
	
	
	@Override
	public void run() {
		JNA.CLibrary.INSTANCE.tdInit();

		// / First register DeviceEvent callback!
		JNA.CLibrary.TDDeviceEvent fn = new JNA.CLibrary.TDDeviceEvent() {
			@Override
			public void invoke(int deviceId, int method, Pointer data,int callbackId, Pointer context) throws SupportedMethodsException {


				try{
					TellstickDevice ts = TellstickDevice.getDevice(deviceId);
					String dataStr = data.getString(0);
					int idx = Collections.binarySearch(list, ts);
					if (idx > -1){
						list.set(idx, ts);
					}
					TellstickDeviceEvent event = new TellstickDeviceEvent(ts, Method.getMethodById(method), dataStr);
					changeListener.onRequest(event);
				// The device is not supported.
				}catch(DeviceNotSupportedException e){
					e.printStackTrace();
				}

			}
		};

		int handle = JNA.CLibrary.INSTANCE.tdRegisterDeviceEvent(fn, null);

		// Now DeviceChangeEvent
		JNA.CLibrary.TDDeviceChangeEvent fn2 = new JNA.CLibrary.TDDeviceChangeEvent() {
			@Override
			public void invoke(int deviceId, int method, int changeType,int callbackId, Pointer context) throws SupportedMethodsException {
				try{
					TellstickDevice ts = TellstickDevice.getDevice(deviceId);
					if (method == JNA.CLibrary.TELLSTICK_DEVICE_CHANGED || method == JNA.CLibrary.TELLSTICK_DEVICE_STATE_CHANGED){
						
						int idx = Collections.binarySearch(list, ts);
						if (idx > -1){
							list.set(idx, ts);
						}
					}
					
					if (method == JNA.CLibrary.TELLSTICK_DEVICE_ADDED){
						list.add(ts);
					}
					
					
					TellstickDeviceEvent event = new TellstickDeviceEvent(ts, Method.getMethodById(method), null);
					changeListener.onRequest(event);
				// The device is not supported.
				}catch(DeviceNotSupportedException e){
					
					// We handle remove unit here.
					if (method == JNA.CLibrary.TELLSTICK_DEVICE_REMOVED){
						for(int i = 0; i < list.size(); i++){
							if (list.get(i).getId() == deviceId){
								list.remove(i);
								TellstickDeviceEvent event = new TellstickDeviceEvent(null, Method.getMethodById(method), null);
								changeListener.onRequest(event);
								return;
							}
						}
						
					}
					
				}
			}
		};

		int handle2 = JNA.CLibrary.INSTANCE.tdRegisterDeviceChangeEvent(
				fn2, null);

		// Do not exit!
		while (run) {
			
			try {
				Thread.sleep(100); // 100 ms
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		
	}

	
	
}
