package pkj.no.tellstick.tests;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.sun.jna.Pointer;

import pkj.no.tellstick.JNA;
import pkj.no.tellstick.device.DeviceNotSupportedException;
import pkj.no.tellstick.device.SupportedMethodsException;
import pkj.no.tellstick.device.TellstickDevice;

/**
 * TEST.
 * 
 * Callbacks in Tellstick. This is currently under dev.
 * 
 * @author peec
 * 
 */
public class Callbacks {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws SupportedMethodsException {
		Properties configFile = new Properties();
		try {
			configFile.load(new FileReader("config.cnf"));
			System.setProperty("jna.library.path",
					configFile.getProperty("telldusFolder"));
		} catch (IOException e) {
			System.err
					.println("Could not open config.cnf, config.cnf must be relative to the binary file.");
		}

		// Set supported methods for this app.
		TellstickDevice.setSupportedMethods(JNA.CLibrary.TELLSTICK_BELL
				| JNA.CLibrary.TELLSTICK_TURNOFF
				| JNA.CLibrary.TELLSTICK_TURNON | JNA.CLibrary.TELLSTICK_DIM
				| JNA.CLibrary.TELLSTICK_LEARN | JNA.CLibrary.TELLSTICK_EXECUTE
				| JNA.CLibrary.TELLSTICK_STOP);

		JNA.CLibrary.INSTANCE.tdInit();

		// / First register DeviceEvent callback!
		JNA.CLibrary.TDDeviceEvent fn = new JNA.CLibrary.TDDeviceEvent() {
			@Override
			public void invoke(int deviceId, int method, Pointer data,int callbackId, Pointer context) throws SupportedMethodsException {

				try{
					TellstickDevice ts = TellstickDevice.getDevice(deviceId);
					System.out.println("Got message from " + ts.getId()
						+ "  .. " + ts.getName() + " .. " + ts.getModel()
						+ " .. " + ts.getProtocol() + ".." + " .. data: " + data);
					System.out.println("Waiting for next.");
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
					TellstickDevice ts;
					ts = TellstickDevice.getDevice(deviceId);
					System.out.println("Got Device Change event from "
						+ ts.getId() + "  .. " + ts.getName() + " .. "
						+ ts.getModel() + " .. " + ts.getProtocol() + "..");
					System.out.println("Waiting for next.");
				// The device is not supported.
				}catch(DeviceNotSupportedException e){
					e.printStackTrace();
				}
			}
		};

		int handle2 = JNA.CLibrary.INSTANCE.tdRegisterDeviceChangeEvent(
				fn2, null);

		System.out.println("Listeting to device events .... " + handle
				+ " - " + handle2);

		// Do not exit!
		while (true) {

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

}
