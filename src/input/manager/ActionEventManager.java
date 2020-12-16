package input.manager;

import java.util.LinkedList;

import input.EventReceiver;
import input.manager.actionevent.ActionEvent;

public class ActionEventManager extends Thread{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static long THREAD_TIME_OUT_DEFAULT = 50;
	private final static int THREAD_TIME_OUT_MULTIPLIER = 2;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private LinkedList<TimedThread> threads;
	private volatile LinkedList<ActionEvent> events;
	private EventReceiver reference;
	private volatile long timeOut;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public ActionEventManager(EventReceiver in) {
		if(threads == null)
			threads = new LinkedList<TimedThread>();
		events = new LinkedList<ActionEvent>();
		timeOut = THREAD_TIME_OUT_DEFAULT;
		reference = in;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void run() {
		while(events.size() > 0) {
			ActionEvent e = events.poll();
			TimedThread t = getOpenThread();
			t.run(e);
		}
	}
	
	public void add(ActionEvent in) {
		//TODO: Filter events you want to ignore
		events.add(in);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public TimedThread getOpenThread() {
		boolean timed = false;
		do {
			timed = false;
			for(TimedThread t : threads) {
				if(t.isAvailable()){
					return t;
				}
				timed = timed || !t.timedOut(timeOut);
			}
		} while(timed);
		TimedThread newT = new TimedThread(reference);
		threads.add(newT);
		timeOut *= THREAD_TIME_OUT_MULTIPLIER;
		return newT;
	}

}
