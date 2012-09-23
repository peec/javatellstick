# JAVATELLSTICK


### What is TellStick?

Tellstick is a device that can control several brands of home automation devices. TellStick can be bought from many places, go to telldus.se to get more information or buy the product.

What is this project?

Advanced OOP Library to communicate with Tellstick, written and made for JAVA.

See Getting Started and start developing your own application for home automation.


### Info

This project is in development but functional as we speak.

This is a JAVA OOP API wrapper for tellstick device control. You can turn off / on / dim / get names and etc. of devices.

This should work with the upcoming Tellstick DUO, and this project will be developed.
Development

### What do you need to develop this API?

- Eclipse ( for easy import of the project )
- Telldus Center for adding units etc.
- A tellstick ( http://telldus.se ) 

### Tellstick API Support

- Callbacks (introduced in Tellstick DUO)
- All API methods in telldus core.
- Error string exceptions.
- more... 

### The "Apps folder in trunk"

To run the jar files in the apps folder (trunk/apps) you will need to take a look at "config.cnf" in the apps folder. Point  "telldusFolder" to the telldus installation! That's it!

Currently Javatellstick ships with:

- A GUI (client.jar) that can communicate with tellstick devices.
- Some more apps that are under development. 


Comes with a test example how how to use the API! See: package pkj.no.tellstick.server;


### Some example code

#### Getting All devices

ArrayList<TellstickDevice> devices = TellstickDevice.getDevices();

#### Turn off all devices

	for (TellstickDevice d : devices) {
		d.off();
	}

	 
	 
	 

# Getting Started


## Remember to import

	import pkj.no.tellstick.JNA;
	import pkj.no.tellstick.device.*;

	
## Setting supported methods

- Before you can use the methods in the API be sure to set the methods that your new application supports.
- You will also need to set system property to where TelldusCore library is. This library location is allways where telldus center was installed.

	System.setProperty("jna.library.path", "C:\Program Files (x86)\telldus");

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

			
	

## Getting all the devices


	try{
		ArrayList<TellstickDevice> devices = TellstickDevice.getDevices();

		// Loop devices.
		for(TellstickDevice device : devices){
			// Now a device can be many types, you can check it with instance of.
			if (device instanceof Device){
				// Cast to device so we can get the method.
				try{
					((Device) device).on();
				}catch(TellstickException e){
					System.out.println(e.getMessage()); // Prints error right from the tellstick if we get error. Forexample if tellstick is not found this will print " Tellstick not found ". 
				}
			}
			// Check if its a bell device.
			if (device instanceof BellDevice){
				try{
					((BellDevice) device).bell();
				}catch(TellstickException e){
					System.out.println(e.getMessage()); // Prints error right from the tellstick if we get error. Forexample if tellstick is not found this will print " Tellstick not found ". 
				}
			}
			
		}
	} catch(SupportedMethodsException e){
	   e.printStackTrace();
	}


## Available Devices


All devices is in the package pkj.no.tellstick.device.*;

### BellDevice

Bell devices only have one action method. Bell, the method makes the device bell.
 
##### Methods

- bell()

### Device

Lights and devices that can turn on / off.

##### Methods

- on()
- off()

##### DimmableDevice

DimmableDevice extends Device, and adds dim method.
A note, when checking for device type, first check for dimmable device then Device because DimmableDevice contains the on/off methods aswell.

##### Methods

- dim(int level) level from 0 - 255, 255 = strongest light.
- on()
- off()


### GroupDevice

Collection of many devices!
getActions() will return all the types of devices.

##### Methods

- getActions()

### SceneDevice

A scene device is a virtual device that contains a scene, forexample a series of lights that turn on / of / dims etc.

##### Methods

- execute()


### UpDownDevice

Devices such as projector screens.

##### Methods

- up()
- down()
- stop()


All of these devices extends `TellstickDevice` 


## Getting specific device types

We have introduced a method named TellstickDevice.getDevices(Class ... ). Lets look closer on this method.


In this example, we want to get devices: BellDevice and GroupDevice

	ArrayList<TellstickDevice> devices = TellstickDevice.getDevices(BellDevice.class, GroupDevice.class);

	// Now devices variable will only contain BellDevice and GroupDevice.


In this example, i want specifc ID's in my device list.

	ArrayList<TellstickDevice> devices = TellstickDevice.getDevices(4,3,5,7,2);

	// Devices with those specific ids will be added to devices variable.

	

## Get a single device by id


This gets the device id 2.

	TellstickDevice device TellstickDevice.getDevice(2);
	

	
	
	
# Tellstick listener
	

Threaded listener to automatically update a list of devices based on callbacks to the telldus core.


## Introduction

The TellstickDeviceListener will keep your ArrayList of TellstickDevice items updated, if a device was turn on, the listener will detect it using the callbacks and update the appropriate item that was affected. 

For example, say a remote control turned on a light, or a new device was ADDED to telldus center - the listener will automatically delete / edit and update exisiting items in the list.


## Implementation


	// Get all devices.
	ArrayList<TellstickDevice> devices = TellstickDevice.getDevices();


	// Create a listener, to keep in track of live statuses.
	final TellstickDeviceListener listener = new TellstickDeviceListener(devices, new DeviceChangeListener(){
		public void onRequest(ArrayList<TellstickDevice> newDevices) {
			devices = newDevices;
			// We got callback event, maybe we would want to trigger some display function so we update the display of the devices.
		}			
	});
 

 
 
## Stopping the listener

The listener will run in a separate thread, when your application is being closed you must stop the listener.

Stopping the listener can be done with:

	listener.remove(); // Stops the listener thread.
	System.exit(0); // Exits application.
