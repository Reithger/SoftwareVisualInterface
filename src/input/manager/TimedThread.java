package input.manager;

import input.EventReceiver;
import input.manager.actionevent.ActionEvent;

public class TimedThread extends Thread {
	
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
				e.printStackTrace();
				System.out.println("Fail forward exception: ActionEventManager's TimedThread object produced an exception while executing input.");
			}
			updateEvent(null);
		}
	}
	

	
}
