package visual.panel;

import java.util.ArrayList;
import java.util.LinkedList;

public class ClickListManager {

	/** HashMap that assigns a name to defined regions of the screen that generate a specified code upon interaction*/
	private volatile LinkedList<String> clickList;	
	
	private volatile boolean mutex;
	
	public ClickListManager() {
		clickList = new LinkedList<String>();
	}
	
	public void addElement(String name) {
		openLock();
		clickList.add(name);
		closeLock();
	}
	
	public void removeElement(String name) {
		openLock();
		clickList.remove(name);
		closeLock();
	}
	
	public boolean hasName(String name) {
		return clickList.contains(name);
	}
	
	public ArrayList<String> getClickableNames(){
		ArrayList<String> out = new ArrayList<String>(clickList);
		return out;
	}
	

//---  Mechanics   ----------------------------------------------------------------------------
	
	protected void openLock() {
		while(mutex) {}
		mutex = true;
	}
	
	protected void closeLock() {
		mutex = false;
	}
		
}
