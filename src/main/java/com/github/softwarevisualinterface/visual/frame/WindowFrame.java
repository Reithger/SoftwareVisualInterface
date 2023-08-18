package com.github.softwarevisualinterface.visual.frame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.softwarevisualinterface.visual.panel.Panel;

/**
 * This class implements the Frame abstract class to store a collection of named Panel objects
 * that can be hidden and shown by referencing their names.
 * 
 * Functions that don't specify the Window you are referring to assumes that you are using the DEFAULT_WINDOW; if
 * this is not the case, make sure to use the functions that specify the Window you are manipulating.
 * 
 * TODO: Panel priority levels in cases of overlap? Handle that in the background or provide controls to intentionally stack stuff?
 * 
 * @author Ada Clevinger
 *
 */

public class WindowFrame extends Frame{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static String DEFAULT_WINDOW = "default";
	private static Logger logger = LogManager.getLogger();
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** HashMap<<r>String, Panel> object containing the Panel objects contained by this WindowFrame and their names*/
	private Map<String, Map<String, Panel>> windows;
	
	private Set<String> activeWindow;
	
	private boolean start;

	/** The lock used for synchronization. */
	protected final Lock lock = new ReentrantLock(true);
	
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
		windows = new HashMap<String, Map<String, Panel>>();
		activeWindow = new HashSet<String>();
		reserveWindow(DEFAULT_WINDOW);
		start = true;
	}

//---  Adder Methods   ------------------------------------------------------------------------
	
	public void reserveWindow(String windowName) {
		if(windows.get(windowName) != null) {
			logger.error("Reserved Window: \"" + windowName + "\" already exists. Window \"" + DEFAULT_WINDOW + "\" is present by default.");
			return;
		}
		lock.lock();
		windows.put(windowName, new HashMap<String, Panel>());
		lock.unlock();
		if(activeWindow.size() == 0) {
			showActiveWindow(windowName);
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
	
	public void addPanelToWindow(String windowName, String panelName, Panel panel) {
		if(windows.get(windowName) == null) {
			reserveWindow(windowName);
		}
		lock.lock();
		windows.get(windowName).put(panelName, panel);
		lock.unlock();
		if(activeWindow.contains(windowName)) {
			showPanel(windowName, panelName);
		}
	}
	
	public void addPanel(String panelName, Panel panel) {
		lock.lock();
		windows.get(DEFAULT_WINDOW).put(panelName, panel);
		lock.unlock();
		showPanel(panelName);
		if(activeWindow.contains(DEFAULT_WINDOW)) {
			showPanel(DEFAULT_WINDOW, panelName);
		}
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
			logger.error("Attempt to remove non-existant Panel object", e);
		}
	}
	
	public void removePanel(String panelName) {
		try {
			removePanelFromScreen(windows.get(DEFAULT_WINDOW).get(panelName));
			windows.get(DEFAULT_WINDOW).get(panelName).setParentFrame(null);
			windows.get(DEFAULT_WINDOW).remove(panelName);
		}
		catch(Exception e) {
			logger.error("Attempt to remove non-existant Panel object; \"" + panelName + "\" not found in default Window, please specify the Window this Panel is in.", e);
		}
	}
	
	public void removeWindow(String windowName) {
		try {
			hideActiveWindow(windowName);
			lock.lock();
			windows.remove(windowName);
			lock.unlock();
		}
		catch(Exception e) {
			logger.error("Attempt to remove non-existant Window collection of Panel objects", e);
		}
	}
	
//---  Setter Methods   -----------------------------------------------------------------------

	public void showActiveWindow(String windowName) {
		activeWindow.add(windowName);
		lock.lock();
		for(Panel p : windows.get(windowName).values()) {
			addPanelToScreen(p);
		}
		lock.unlock();
	}
	
	public void hideActiveWindow(String windowName) {
		activeWindow.remove(windowName);
		lock.lock();
		for(Panel p : windows.get(windowName).values()) {
			removePanelFromScreen(p);
		}
		lock.unlock();
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public Set<String> getActiveWindows() {
		return activeWindow;
	}
	
	public Panel getPanel(String windowName, String panelName) {
		if(windows.get(windowName) == null) {
			return null;
		}
		return windows.get(windowName).get(panelName);
	}
	
	public Panel getPanel(String panelName) {
		if(windows.get(DEFAULT_WINDOW) == null) {
			return null;
		}
		return windows.get(DEFAULT_WINDOW).get(panelName);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void repaint() {
		super.repaint();
	}

	public void showWindow() {
		if(!start || activeWindow.size() == 0) {
			return;
		}
		lock.lock();
		for(String window : activeWindow) {
			for(String p : windows.get(window).keySet()) {
				showPanel(window, p);
			}
		}
		lock.unlock();
	}
	
	public void showPanel(String windowName, String panelName) {
		try {
			addPanelToScreen(windows.get(windowName).get(panelName));
		}
		catch(Exception e) {
			logger.error("Attempt to show non-existant Panel object", e);
		}
	}
	
	public void showPanel(String panelName) {
		try {
			addPanelToScreen(windows.get(DEFAULT_WINDOW).get(panelName));
		}
		catch(Exception e) {
			logger.error("Attempt to show non-existant Panel object", e);
		}
	}

	public void hidePanel(String windowName, String panelName) {
		try {
			removePanelFromScreen(windows.get(windowName).get(panelName));
		}
		catch(Exception e) {
			logger.error("Attempt to hide non-existant Panel object", e);
		}
	}
	
	public void hidePanel(String panelName) {
		try {
			removePanelFromScreen(windows.get(DEFAULT_WINDOW).get(panelName));
		}
		catch(Exception e) {
			logger.error("Attempt to hide non-existant Panel object", e);
		}
	}

	public void hidePanels() {
		if(activeWindow.size() == 0) {
			return;
		}
		lock.lock();
		for(String window : activeWindow) {
			for(String p : windows.get(window).keySet()) {
				hidePanel(window, p);
			}
		}

		lock.unlock();
	}
	
	public void dispenseAttention() {
		lock.lock();
		for(String window : activeWindow) {
			for(Panel p : windows.get(window).values()) {
				p.setAttention(false);
			}
		}
		lock.unlock();
	}
	
	@Override
	public void reactToResize() {
		logger.info("Overwrite this method: reactToResize() in WindowFrame.java");
	}
	
}
