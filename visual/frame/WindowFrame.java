package visual.frame;

import java.util.HashMap;
import visual.panel.Panel;

/**
 * This class implements the Frame abstract class to store a collection of named Panel objects
 * that can be hidden and shown by referencing their names.
 * 
 * @author Ada Clevinger
 *
 */

public class WindowFrame extends Frame{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** HashMap<<r>String, Panel> object containing the Panel objects contained by this WindowFrame and their names*/
	private HashMap<String, HashMap<String, Panel>> windows;
	
	private String activeWindow;
	
	private boolean start;
	
	private boolean mutex;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * Constructor for objects of the WindowFrame type; assigns provided size for the WindowFrame, calls
	 * the initiate() function from abstract class Frame, and initializes the HashMap<<r>String, Panel>
	 * for the Panel objects held by this WindowFrame.
	 * 
	 * @param width - int value representing the width of this WindowFrame
	 * @param height - int value representing the height of this WindowFrame
	 */
	
	public WindowFrame(int width, int height) {
		super(width, height);
		windows = new HashMap<String, HashMap<String, Panel>>();
		activeWindow = null;
		start = true;
		mutex = false;
	}

//---  Adder Methods   ------------------------------------------------------------------------
	
	public void reserveWindow(String windowName) {
		openLock();
		windows.put(windowName, new HashMap<String, Panel>());
		closeLock();
		if(activeWindow == null) {
			setActiveWindow(windowName);
		}
	}
	
	/**
	 * This method adds a Panel object to this WindowFrame object, creating an entry
	 * in the HashMap<<r>String, Panel> for manipulation.
	 * 
	 * By default added Panels are visible; location of the Panel is handled by the
	 * Panel (call setLocation(int, int)).
	 * 
	 */
	
	public void reservePanel(String windowName, String panelName, Panel panel) {
		if(windows.get(windowName) == null) {
			openLock();
			windows.put(windowName, new HashMap<String, Panel>());
			closeLock();
		}
		openLock();
		windows.get(windowName).put(panelName, panel);
		closeLock();
		showPanel(panelName);
	}
	
	public void reservePanel(String panelName, Panel panel) {
		if(activeWindow == null) {
			return;
		}
		if(windows.get(activeWindow) == null) {
			openLock();
			windows.put(activeWindow, new HashMap<String, Panel>());
			closeLock();
		}
		openLock();
		windows.get(activeWindow).put(panelName, panel);
		closeLock();
		showPanel(panelName);
	}
	
//---  Remover Methods   ----------------------------------------------------------------------
	
	/**
	 * This method removes a Panel object specified by a name represented as a String object from
	 * this WindowFrame, removing it both from immediate view and the stored HashMap<<r>String, Panel>
	 * that contains all Panels associated to this WindowFrame object.
	 * 
	 * @param name - String object representing the name of the Panel to remove from this WindowFrame.
	 */
	
	public void removeWindowPanel(String windowName, String panelName) {
		try {
			removePanelFromScreen(windows.get(windowName).get(panelName));
			windows.get(windowName).get(panelName).setParentFrame(null);
			windows.get(windowName).remove(panelName);
		}
		catch(Exception e) {
			System.out.println("Error: Attempt to remove non-existant Panel object");
		}
	}
	
	public void removePanel(String panelName) {
		try {
			removePanelFromScreen(windows.get(activeWindow).get(panelName));
			windows.get(activeWindow).get(panelName).setParentFrame(null);
			windows.get(activeWindow).remove(panelName);
		}
		catch(Exception e) {
			System.out.println("Error: Attempt to remove non-existant Panel object");
		}
	}
	
	public void removeWindow(String windowName) {
		try {
			windows.remove(windowName);
		}
		catch(Exception e) {
			System.out.println("Error: Attempt to remove non-existant Window collection of Panel objects");
		}
	}
	
//---  Setter Methods   -----------------------------------------------------------------------

	public void setActiveWindow(String windowName) {
		activeWindow = windowName;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public String getActiveWindow() {
		return activeWindow;
	}
	
	public Panel getPanel(String windowName, String panelName) {
		return windows.get(windowName).get(panelName);
	}
	
	public Panel getPanel(String panelName) {
		return windows.get(activeWindow).get(panelName);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void repaint() {
		if(!start || windows.get(activeWindow) == null) {
			return;
		}
		openLock();
		for(Panel p : windows.get(activeWindow).values()) {
			p.getPanel().repaint();
		}
		closeLock();
	}
	
	/**
	 * This method removes a designated Panel object from the Frame, but keeps
	 * it in the stored HashMap<<r>String, Panel> for re-inclusion at a later time.
	 * 
	 * @param name - String object representing the name associated to a Panel object stored by this WindowFrame.
	 */
	
	public void hidePanel(String panelName) {
		try {
			removePanelFromScreen(windows.get(activeWindow).get(panelName));
		}
		catch(Exception e) {
			System.out.println("Error: Attempt to hide non-existant Panel object");
		}
	}
	
	public void showWindow() {
		if(!start || windows.get(activeWindow) == null) {
			return;
		}
		openLock();
		for(String p : windows.get(activeWindow).keySet()) {
			showPanel(p);
		}
		closeLock();
	}
	
	/**
	 * This method adds a designated Panel object to the Frame, accessing the stored
	 * HashMap<<r>String, Panel> via the provided name to find the Panel to add.
	 * 
	 * @param name - String object representing the name associated to a Panel object stored by this WindowFrame.
	 */
	
	public void showPanel(String panelName) {
		try {
			addPanelToScreen(windows.get(activeWindow).get(panelName));
		}
		catch(Exception e) {
			System.out.println("Error: Attempt to show non-existant Panel object");
		}
	}
	
	public void hidePanels() {
		if(windows.get(activeWindow) == null) {
			return;
		}
		openLock();
		for(String p : windows.get(activeWindow).keySet()) {
			this.hidePanel(p);
		}
		closeLock();
	}
	
	public void dispenseAttention() {
		openLock();
		for(Panel p : windows.get(activeWindow).values()) {
			p.setAttention(false);
		}
		closeLock();
	}
	
//---  Mechanics   ----------------------------------------------------------------------------
	
	private void openLock() {
		while(mutex) {
		}
		mutex = true;
	}
	
	private void closeLock() {
		mutex = false;
	}
	
}
