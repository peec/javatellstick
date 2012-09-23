package pkj.no.gui.panel;


import java.util.ArrayList;


import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.*;

import pkj.no.gui.Application;
import pkj.no.tellstick.JNA;
import pkj.no.tellstick.device.*;

public class Devices extends Panel {

	ArrayList<TellstickDevice> devices;

	
	Table table;
	
	public Devices(Shell shell) {
		super(shell);

		try{
		
		// initialize the table with a border
		table = new Table(shell, SWT.BORDER);

		String[] titles = {"Device Name", "On/Off", "Dimmer", "Bell", "Learn", "Up", "Down", "Stop", "Execute"};
		int[] widths = {100, 100, 150, 50, 50, 50, 50, 50, 60};
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
			column.setWidth(widths[i]);
		}

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setSize(700, 400);

		// ENABLED THE SCROLL BAR
		// sets the maximum amount of elements visible before a scroll bar
		// appears
		table.setTopIndex(20);

		
		// Get devices.
		
		devices = TellstickDevice.getDevices();

		
		
		// Create a listener, to keep in track of live statuses.
		final TellstickDeviceListener listener = new TellstickDeviceListener(devices, new DeviceChangeListener(){
			@Override
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

		// Important remove listener if closed.
	    shell.addListener(SWT.Close, new Listener() {
	      public void handleEvent(Event event) {
	    	  listener.remove();
	    	  JNA.CLibrary.INSTANCE.tdClose();
	      }
	    });		
		
	    printDevices();

		table.pack();

		}catch(SupportedMethodsException e){
			e.printStackTrace();
		}
	}
	
	public void printDevices(){


		// Print devices.
		for (int i = 0; i < devices.size(); i++) {
		
			
			TableItem item = new TableItem(table, SWT.NONE);

			final TellstickDevice device = devices.get(i);
			
			item.setText(0, device.getName());
				

			// Group of multiple devices.
			if (device instanceof GroupDevice) {
				
				item.setBackground(new Color(null, 220,220,220));
				
				for(TellstickDevice d : ((GroupDevice) device).getActions()){
					deviceTable(table, item,  d);
				}
			// Scene Device.
			}else if(device instanceof SceneDevice){
				
				// 244-238-224
				item.setBackground(new Color(null, 244,238,224));
				
				TableEditor buttonEditor = new TableEditor(table);
				buttonEditor.grabHorizontal = true;
				buttonEditor.grabVertical = true;
				final Button button = new Button(table, SWT.PUSH);
				button.setText("Execute");

				
				button.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}

					@Override
					public void widgetSelected(SelectionEvent e) {
						try {
							((SceneDevice) device).execute();
						} catch (TellstickException ex) {
							Application.Error(ex.getMessage());
						}

					}
				});
				buttonEditor.setEditor(button, item, 8);
				
				
				
			// Single device.	
			}else{
				deviceTable(table, item,  device);
			}
			
		}		
	}
	
	public void deviceTable(Table table, TableItem item, TellstickDevice device){

		final TellstickDevice  dev = device;
		if (dev instanceof Device) {
			
			
			String action = "";
			if ((JNA.CLibrary.TELLSTICK_TURNON & dev.getStatus()) > 0) {
				action = "Turn off";
			} else {
				action = "Turn on";
			}

			TableEditor buttonEditor = new TableEditor(table);
			buttonEditor.grabHorizontal = true;
			buttonEditor.grabVertical = true;
			final Button button = new Button(table, SWT.PUSH);
			button.setText(action);

			
			button.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						if ((JNA.CLibrary.TELLSTICK_TURNON & dev
								.getStatus()) > 0) {
							((Device) dev).off();
							button.setText("Turn on");
						} else {
							((Device) dev).on();
							button.setText("Turn off");
						}
					} catch (TellstickException ex) {
						Application.Error(ex.getMessage());
					}

				}
			});
			buttonEditor.setEditor(button, item, 1);
			

			if (dev instanceof DimmableDevice) {

				
				// make a TableEditor, an element that allows you to put an
				// editable component into the cell
				TableEditor editor = new TableEditor(table);

				// create a new slider
				final Slider slider = new Slider(table, SWT.HORIZONTAL);
				slider.setMinimum(0);
				slider.setMaximum(265); // Maximum 265-10
				slider.setIncrement(8);
				slider.setPageIncrement(32);
				slider.setSelection(0);

				// CRITICAL - specifies that the editor should be sized into
				// the
				// entire width of the control
				// or else will not visible
				editor.grabHorizontal = true;
				editor.grabVertical = true;

				// register a slider drag event
				slider.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						switch (event.detail) {

							default:
							int val = slider.getSelection();
							try {
								((DimmableDevice) dev).dim(val);
							} catch (TellstickException e) {
								Application.Error(e.getMessage());
							}
							break;
						}
					}
				});

				// add the slider into the table
				editor.setEditor(slider, item, 2);

			}

		}else if (dev instanceof BellDevice){
			TableEditor buttonEditor = new TableEditor(table);
			buttonEditor.grabHorizontal = true;
			buttonEditor.grabVertical = true;
			final Button button = new Button(table, SWT.PUSH);
			button.setText("Bell");

			button.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						((BellDevice) dev).bell();
					} catch (TellstickException ex) {
						Application.Error(ex.getMessage());
					}

				}
			});
			
			
			buttonEditor.setEditor(button, item, 3);
		}else if(dev instanceof UpDownDevice){

			
			TableEditor buttonEditor = new TableEditor(table);
			buttonEditor.grabHorizontal = true;
			buttonEditor.grabVertical = true;
			final Button button2 = new Button(table, SWT.ARROW | SWT.UP);
			

			button2.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						((UpDownDevice) dev).up();
					} catch (TellstickException ex) {
						Application.Error(ex.getMessage());
					}

				}
			});
			buttonEditor.setEditor(button2, item, 5);
			
			
			
			buttonEditor = new TableEditor(table);
			buttonEditor.grabHorizontal = true;
			buttonEditor.grabVertical = true;
			final Button button3 = new Button(table, SWT.ARROW | SWT.DOWN);
			button3.setText("Down");


			button3.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						((UpDownDevice) dev).down();
					} catch (TellstickException ex) {
						Application.Error(ex.getMessage());
					}

				}
			});
			buttonEditor.setEditor(button3, item, 6);
			
			
			
			buttonEditor = new TableEditor(table);
			buttonEditor.grabHorizontal = true;
			buttonEditor.grabVertical = true;
			final Button button4 = new Button(table, SWT.ABORT);
			button4.setText("Stop");


			button4.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						((UpDownDevice) dev).stop();
					} catch (TellstickException ex) {
						Application.Error(ex.getMessage());
					}

				}
			});
			buttonEditor.setEditor(button4, item, 7);
			
			
			
		}
		

		if (dev.isLearnable()){
			TableEditor buttonEditor = new TableEditor(table);
			buttonEditor.grabHorizontal = true;
			buttonEditor.grabVertical = true;
			final Button button = new Button(table, SWT.PUSH);
			button.setText("Learn");

			button.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						dev.learn();
					} catch (TellstickException ex) {
						Application.Error(ex.getMessage());
					}

				}
			});
			
			
			buttonEditor.setEditor(button, item, 4);
			
		}
		
	}
	

}
