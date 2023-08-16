package com.github.softwarevisualinterface.input.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.softwarevisualinterface.input.EventReceiver;
import com.github.softwarevisualinterface.input.manager.actionevent.ActionEvent;

public class TimedThread extends Thread {

	private static Logger logger = LogManager.getLogger();
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private EventReceiver reference;
	private ActionEvent event;
	private volatile boolean open;
	private volatile long time;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public TimedThread(EventReceiver ref) {
		reference = ref;
		start();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	private void updateEvent(ActionEvent in) {
		event = in;
	}
	
	public void run(ActionEvent e) {
		updateEvent(e);
		time = System.currentTimeMillis();
		open = false;
		interrupt();
	}
	
	public boolean isAvailable() {
		return open;
	}
	
	public boolean timedOut(long duration) {
		return getLifetime() > duration;
	}
	
	private long getLifetime() {
		return (System.currentTimeMillis() - time);
	}
	
	@Override
	public void run() {
		while(reference != null) {
			open = true;
			while(open && reference != null) {
				try {
					sleep(250);
				}
				catch(InterruptedException e) {}
			}
			try {
				event.execute(reference);
			}
			catch(Exception e) {
				logger.error("Fail forward exception: ActionEventManager's TimedThread object produced an exception while executing input.", e);
			}
			updateEvent(null);
		}
	}
	

	
}
