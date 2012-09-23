package pkj.no.tellstick.tests;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import org.apache.commons.cli.*;

import pkj.no.tellstick.JNA;
import pkj.no.tellstick.device.*;
import pkj.no.tellstick.schedule.DeviceSchedular;
import pkj.no.tellstick.schedule.Schedule;
import pkj.no.tellstick.schedule.ScheduleCallback;

/**
 * Main application for the tellstick server. What this does is that it tries to
 * save power for your house with tellstick devices.
 * 
 * It gets the devices and turns them off during specialized input field.
 * 
 * @author peec
 * 
 */
public class PowerSaverServer {

	/**
	 * @param args
	 * @throws SupportedMethodsException 
	 */
	public static void main(String[] args) throws SupportedMethodsException {

		Properties configFile = new Properties();
		try {
			configFile.load(new FileReader("config.cnf"));
			System.setProperty("jna.library.path", configFile.getProperty("telldusFolder"));
		} catch (IOException e) {
			System.err.println("Could not open config.cnf, config.cnf must be relative to the binary file.");
		}

		// Set supported methods for this app.
		TellstickDevice.setSupportedMethods(
				JNA.CLibrary.TELLSTICK_BELL | 
				JNA.CLibrary.TELLSTICK_TURNOFF | 
				JNA.CLibrary.TELLSTICK_TURNON | 
				JNA.CLibrary.TELLSTICK_DIM | 
				JNA.CLibrary.TELLSTICK_LEARN |
				JNA.CLibrary.TELLSTICK_EXECUTE |
				JNA.CLibrary.TELLSTICK_STOP
		);

		


		
		
		// create Options object
		Options options = new Options();

		// Allow list devices.
		options.addOption("s", "show-devices", false,
				"Shows all the devices on the tellstick.");

		// Allow to run the server.
		options.addOption(
				"r",
				"run",
				false,
				"Runs the server. Use -o flag to set what devices that should be turned off / on at night.");

		// Allow to run the server.
		options.addOption("o", "off-on-devices", true,
				"Comma separated list of device ids. eg. 1,2,3 .");

		CommandLineParser parser = new PosixParser();
		try {
			CommandLine cmd = parser.parse(options, args);

			/* Show devices */
			if (cmd.hasOption("s")) {

				ArrayList<TellstickDevice> devices = TellstickDevice.getDevices();
				System.out
						.println("--- Devices found on the telldus center: ---");
				for(TellstickDevice t : devices){
					System.out.println(t.getType() + "\t" + t.getId() + "\t" + t.getName() + "\t");
				}
				
				/*
				 * Runs the save power server. What does it do? Turns off all
				 * lights compatible @ 01:00 (24hour time) // You have gone to
				 * sleep. Turns on lights @ 7:00, // You prepare for work Turns
				 * off lights @ 11:00 // You have left work for some time ago.
				 * Turns on lights @ 15:00 // You come home to enlighten house!
				 */
			} else if (cmd.hasOption("r")) {
				System.out.println("Running power saver server....");
				String offOnDevices = cmd.getOptionValue("o");

				String[] deviceIds = offOnDevices.split(",");

				// -o flag not set ?
				if (deviceIds.length == 0 || deviceIds[0].equals("")) {
					System.err.println("You must set the -o flag with device ids from the -s flag.");
					System.exit(1);
				}

				ArrayList<TellstickDevice> devices2 = TellstickDevice.getDevices(deviceIds);
				
				
				@SuppressWarnings("unchecked")
				ArrayList<TellstickDevice> devices = TellstickDevice.filterDevices(devices2, Device.class);
				
				
				// No devices left ? ugh ...
				if (devices.size() == 0) {
					System.err
							.println("No compatible devices: (On/Off devices) was found.");
					System.exit(1);
				}

				DeviceSchedular devSchedular = new DeviceSchedular();
				for(TellstickDevice dev: devices){
					Schedule schedule = new Schedule(Schedule.MONDAY | Schedule.TUESDAY | Schedule.WEDNESDAY | Schedule.THURSDAY | Schedule.THURSDAY | Schedule.FRIDAY,
							1, 0, dev, new ScheduleCallback(){
								@Override
								public void onTrigger(TellstickDevice device) {
									try {
										((Device) device).off();
									} catch (TellstickException e) {
										e.printStackTrace();
									}
								}
					});
					devSchedular.addSchedule(schedule);
					
					schedule = new Schedule(Schedule.MONDAY | Schedule.TUESDAY | Schedule.WEDNESDAY | Schedule.THURSDAY | Schedule.THURSDAY | Schedule.FRIDAY,
							7, 0, dev, new ScheduleCallback(){
								@Override
								public void onTrigger(TellstickDevice device) {
									try {
										((Device) device).on();
									} catch (TellstickException e) {
										e.printStackTrace();
									}
								}
					});
					devSchedular.addSchedule(schedule);
					
				}
				
				// Run in separate thread.
				devSchedular.run();
								

				// Server loop
				while (true) {

					// wait 10 sec. before new loop.
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {

					}
				}
			} else {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("ant", options);

			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
