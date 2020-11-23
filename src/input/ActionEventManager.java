package input;

import java.util.LinkedList;

import visual.panel.Panel;

public class ActionEventManager extends Thread{

	private LinkedList<TimedThread> threads;
	private LinkedList<ActionEvent> events;
	private Panel reference;
	
	public ActionEventManager(Panel in) {
		threads = new LinkedList<TimedThread>();
		events = new LinkedList<ActionEvent>();
		reference = in;
	}
	
	@Override
	public void run() {
		while(events.size() > 0) {
			ActionEvent e = events.poll();
			TimedThread t = getOpenThread();
			t.run(e);
		}
	}
	
	public TimedThread getOpenThread() {
		boolean timed = false;
		do {
			timed = false;
			for(TimedThread t : threads) {
				if(t.isAvailable()){
					return t;
				}
				timed = timed || !t.timedOut();
			}
		} while(timed);
		TimedThread newT = new TimedThread(reference);
		threads.add(newT);
		return newT;
	}
	
	public void add(ActionEvent in) {
		events.add(in);
	}
	
	class TimedThread extends Thread {
		
		private Panel reference;
		private ActionEvent event;
		private boolean open;
		private long time;
		
		public TimedThread(Panel ref) {
			reference = ref;
			start();
		}
		
		public void updateEvent(ActionEvent in) {
			event = in;
		}
		
		public void run(ActionEvent e) {
			open = false;
			time = System.currentTimeMillis();
			interrupt();
			event = e;
		}
		
		public boolean isAvailable() {
			return open;
		}
		
		public boolean timedOut() {
			return (System.currentTimeMillis() - time) > 5;
		}
		
		@Override
		public void run() {
			while(reference != null) {
				open = true;
				while(open) {
					try {
						sleep(10);
					} catch (InterruptedException e) {}
				}
				try {
					event.execute(reference);
				}
				catch(Exception e) {
					e.printStackTrace();
					System.out.println("Fail forward exception: ActionEventManager's TimedThread object produced an exception while executing input.");
				}
			}
		}
		
	}
	
}
