package pkj.no.tellstick.schedule;

import java.util.ArrayList;
/**
 * A shedular application that runs in a separate thread.
 * You can schedule actions for devices, create a new Schedule in a very neat way and add it to this collection.
 * @author peec
 *
 */
public class DeviceSchedular extends Thread implements Runnable {

	boolean running = true;

	ArrayList<Schedule> schedules;
	
	public DeviceSchedular(){
		
	}
	
	
	/**
	 * Runs the thread.
	 */
	@Override
	public void run() {

		while (running) {
			if(schedules.size() > 0){
				
				for(int i = 0; i < schedules.size(); i++){
					Schedule schedule = schedules.get(i);
					
					schedule.run();
				}
				
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					
				}
			// No schedules is here, just exit.
			}else{
				this.delete();
			}
		}

	}

	/**
	 * Adds a new schedule item.
	 * @param schedule
	 */
	public void addSchedule(Schedule schedule) {
		this.schedules.add(schedule);
	}

	/**
	 * Deletes the thread in a safe way. 
	 */
	public void delete() {
		running = false;
	}

	
}
