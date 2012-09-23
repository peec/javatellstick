package pkj.no.gui;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

import pkj.no.gui.panel.*;
import pkj.no.tellstick.JNA;
import pkj.no.tellstick.device.TellstickDevice;
/**
 * Tellstick GUI application.
 * 
 * @author peec
 *
 */
public class Application {

	
	static int[] data = new int[0];

	static public Shell shell;
	static public Display display;
	
	final static public double VERSION = 1.0;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Set supported methods for this app.
		// THIS IS REQUIRED.
		TellstickDevice.setSupportedMethods(
				JNA.CLibrary.TELLSTICK_BELL | 
				JNA.CLibrary.TELLSTICK_TURNOFF | 
				JNA.CLibrary.TELLSTICK_TURNON | 
				JNA.CLibrary.TELLSTICK_DIM | 
				JNA.CLibrary.TELLSTICK_LEARN |
				JNA.CLibrary.TELLSTICK_EXECUTE |
				JNA.CLibrary.TELLSTICK_STOP |
				JNA.CLibrary.TELLSTICK_UP |
				JNA.CLibrary.TELLSTICK_DOWN
		);
		
		display = new Display ();
		shell = new Shell (display);
		
		shell.setText("PKJ Tellstick GUI " + VERSION);
		

		Properties configFile = new Properties();
		try {
			configFile.load(new FileReader("config.cnf"));
			System.setProperty("jna.library.path", configFile.getProperty("telldusFolder"));
		} catch (IOException e) {
			Error("Could not open config.cnf, config.cnf must be relative to the binary file.");
		}
		
		try{
			JNA.CLibrary.INSTANCE.tdInit();
		
			menu();
			
			
			new Devices(shell);
			
			
			shell.pack ();
			shell.open ();
			while (!shell.isDisposed ()) {
				if (!display.readAndDispatch ()) display.sleep ();
			}
			
			
			display.dispose ();
		
		}catch(UnsatisfiedLinkError e){
			Error(e.getMessage());
		}

		
	}
	
	public static void menu(){
		Menu bar = new Menu (shell, SWT.BAR);
		shell.setMenuBar (bar);

		MenuItem item;
		
		
		
		// Devices menu.
		MenuItem devicesItem = new MenuItem (bar, SWT.CASCADE);
		devicesItem.setText ("&Devices");
		Menu submenu = new Menu (shell, SWT.DROP_DOWN);
		devicesItem.setMenu (submenu);
		
		// Information
		item = new MenuItem (submenu, SWT.PUSH);
		item.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				new Devices(shell);
			}
		});
		item.setText ("&Show &Devices");
		
		
		// About menu.
		MenuItem aboutItem = new MenuItem (bar, SWT.CASCADE);
		aboutItem.setText ("&About");
		submenu = new Menu (shell, SWT.DROP_DOWN);
		aboutItem.setMenu (submenu);
		
		// Information
		item = new MenuItem (submenu, SWT.PUSH);
		item.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				Information(
				"PKJ GUI Version "+VERSION+"" +
				"\n\nThis application lets you control your tellstick devices. It works on all platforms such as: Mac, Linux and Windows." +
				"\n\nAuthor: Petter Kjelkenes <kjelkenes@gmail.com>" +
				"\n\nThis application is writting in Java,  and is based on the BSD 2 clause License." +
				"\n\nCopyright Petter Kjelkenes<kjelkenes@gmail.com>"
				, "Information");
			}
		});
		item.setText ("&Information");
		
		
		// License
		item = new MenuItem (submenu, SWT.PUSH);
		item.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				Information(
				"Copyright 2011 Petter Kjelkenes <kjelkenes@gmail.com>. All rights reserved." +
				"\n\nRedistribution and use in source and binary forms, with or without modification, are" +
				"permitted provided that the following conditions are met:" +
				"\n\n    1. Redistributions of source code must retain the above copyright notice, this list of" +
				"conditions and the following disclaimer." +
				"\n\n    2. Redistributions in binary form must reproduce the above copyright notice, this list" +
				"of conditions and the following disclaimer in the documentation and/or other materials" +
				"provided with the distribution." +
				"\n\nTHIS SOFTWARE IS PROVIDED BY Petter Kjelkenes ``AS IS'' AND ANY EXPRESS OR IMPLIED" +
				"WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND" +
				"FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Petter Kjelkenes OR" +
				"CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR" +
				"CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR" +
				"SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON" +
				"ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING" +
				"NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF" +
				"ADVISED OF THE POSSIBILITY OF SUCH DAMAGE." +
				"\n\nThe views and conclusions contained in the software and documentation are those of the" +
				"authors and should not be interpreted as representing official policies, either expressed" +
				"or implied, of Petter Kjelkenes."
				, "License");
			}
		});
		item.setText ("&License");
		
		
		
		
	}
	
	
	public static void Error(String msg, String title){
		MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR);
		dialog.setText ("Error");
		
		dialog.setMessage(msg);
		
		dialog.open ();
	}
	public static void Error(String msg){
		Error(msg, "Error");
	}
	
	public static void Information(String msg, String title){
		MessageBox dialog = new MessageBox(shell, SWT.ICON_INFORMATION);
		dialog.setText (title);
		
		dialog.setMessage(msg);
		
		dialog.open ();
	}
	public static void Information(String msg){
		Information(msg, "Information");
	}
	

}
