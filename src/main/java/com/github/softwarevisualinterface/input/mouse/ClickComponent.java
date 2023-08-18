package com.github.softwarevisualinterface.input.mouse;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.github.softwarevisualinterface.input.EventFielder;
import com.github.softwarevisualinterface.input.manager.actionevent.ActionEventGenerator;

/**
 * This class implements the MouseListener interface to predefine behavior for integrating user
 * interactivity via the user's Mouse; this is a low level class that a user of the SWI library
 * for visual design should not see.
 * 
 * Response to user input is performed by predefining regions of the screen to generate defined
 * code values that the containing EventReceiver object is given when its clickEvent method is called.
 * 
 * Extending to account for x, y integer coordinate values being passed to the EventReceiver is planned
 * for future flexibility; likely create an interface for which discrete and 'continuous' reactivity
 * is integrated.
 * 
 * @author Ada Clevinger
 *
 */

public class ClickComponent implements MouseListener, MouseMotionListener, MouseWheelListener{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** ArrayList<Integer[]> object containing the coordinates and codes for each event-region*/
	private Map<Integer, Detectable> detectionRegions;
	/** EventReceiver object representing the EventReceiver to which this ClickComponent is attached (the EventReceiver that is being clicked)*/
	private EventFielder eventHandler;

	/** The lock used for synchronization. */
	protected final Lock lock;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * Constructor for objects of the ClickComponent object; default selected value is -1, and
	 * this object is added as a MouseListener to the provided EventReceiver.
	 * 
	 * @param eventReceiver - EventReceiver object for which this ClickComponent exists; directs input to this EventReceiver.
	 */
	
	public ClickComponent(EventFielder eventReceiver){
		detectionRegions = new HashMap<Integer, Detectable>();
		eventHandler = eventReceiver;
		lock = new ReentrantLock(true);
	}
	
//---  Operations   ---------------------------------------------------------------------------

	/**
	 * This method resets the list of Detectable objects stored by this ClickComponent; no
	 * regions of the screen will generate code values until new Detectables are added.
	 */
	
	public void resetDetectionRegions() {
		lock.lock();;
		detectionRegions = new HashMap<Integer, Detectable>();
		lock.unlock();
	}
	
//---  Remover Methods   ----------------------------------------------------------------------
	
	/**
	 * This method removes the Detectable object specified by a Code value (that is,
	 * the Detectable object which would return the provided value if a click registered
	 * in its region.)
	 * 
	 * @param code - int value that represents the code for which the matching Detectable object should be removed.
	 * @return - Returns a boolean value describing whether or not a Detectable object was found and removed or not.
	 */
	
	public boolean removeDetectionRegion(int identity) {
		lock.lock();
		detectionRegions.remove(identity);
		lock.unlock();
		return false;
	}

	/**
	 * This method removes all Detectable objects for which the provided x and y coordinates would
	 * generate their code. (Detectable objects can overlap, and all would trigger a ClickEvent in
	 * the containing EventReceiver object.)
	 * 
	 * @param x - int value representing the x coordinate of the point in the EventReceiver containing this ClickComponent to remove Detectable objects from.
	 * @param y - int value representing the y coordinate of the point in the EventReceiver containing this ClickComponent to remove Detectable objects from.
	 * @return - Returns a boolean value representing whether any Detectable objects were removed (true) or not (false).
	 * 
	 */
	
	public boolean removeDetectionRegions(int x, int y) {
		boolean out = false;
		lock.lock();
		for(int i = 0; i < detectionRegions.size(); i++) {
			Detectable d = detectionRegions.get(i);
			if(d.wasClicked(x, y)) {
				detectionRegions.remove(i);
				out = true;
			}
		}
		lock.unlock();
		return out;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------

	/**
	 * This method takes the provided list of Detectable objects and assigns it to this ClickComponent.
	 * 
	 * @param updated - ArrayList<<r>Detectable> object containing Detectable objects
	 */
	
	public void setDetectionRegions(Map<Integer, Detectable> updated){
		lock.lock();
		detectionRegions = updated;
		lock.unlock();
	}

	/**
	 * This method sets the provided EventReceiver as the container for which this ClickComponent exists (will
	 * direct user interaction to that EventReceiver.)
	 * 
	 * Does not add this ClickComponent to the EventReceiver, only changes the reference locally. You are still
	 * able to add the ClickComponent to the EventReceiver elsewhere.
	 * 
	 * @param reference - EventReceiver object for which this ClickComponent is now associated.
	 */
	
	public void setEventFielder(EventFielder eF){
		eventHandler = eF;
	}
	
//---  Adder Methods   ------------------------------------------------------------------------
	
	/**
	 * This method adds the provided Detectable object to the ArrayList stored
	 * by this ClickComponent; if an existing Detectable object already uses the
	 * code associated to the Detectable object, it is not added.
	 * 
	 * @param region - Detectable object representing a new defined region of the screen to generate a specified code value.
	 * @return - Returns a boolean value representing whether or not the Detectable object was successfully added.
	 */
	
	public void addClickRegion(int identity, Detectable region){
		lock.lock();
		detectionRegions.put(identity, region);
		lock.unlock();
	}
	
//---  Events   -------------------------------------------------------------------------------
	
	@Override
	public void mouseClicked(MouseEvent e){
		eventHandler.requestFocusInWindow();
		Integer x = e.getX();
		Integer y = e.getY();
		boolean happened = false;
		for(Detectable d : findClicked(x, y)) {
			happened = true;
			eventHandler.receiveActionEvent(ActionEventGenerator.generateMouseActionEvent(ActionEventGenerator.MOUSE_CLICK, d.getCode(), x, y, e.getButton()));
		}
		if(!happened){
			eventHandler.receiveActionEvent(ActionEventGenerator.generateMouseActionEvent(ActionEventGenerator.MOUSE_CLICK, -1, x, y, e.getButton()));
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		eventHandler.requestFocusInWindow();
		Integer x = e.getX();
		Integer y = e.getY();
		boolean happened = false;
		for(Detectable d : findClicked(x, y)) {
			happened = true;
			eventHandler.receiveActionEvent(ActionEventGenerator.generateMouseActionEvent(ActionEventGenerator.MOUSE_RELEASE, d.getCode(), x, y, e.getButton()));
		}
		if(!happened) {
			eventHandler.receiveActionEvent(ActionEventGenerator.generateMouseActionEvent(ActionEventGenerator.MOUSE_RELEASE, -1, x, y, e.getButton()));
			
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e){
		//This space intentionally left blank, but doesn't need to be
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		eventHandler.requestFocusInWindow();
		Integer x = e.getX();
		Integer y = e.getY();
		boolean happened = false;
		for(Detectable d : findClicked(x, y)) {
			happened = true;
			eventHandler.receiveActionEvent(ActionEventGenerator.generateMouseActionEvent(ActionEventGenerator.MOUSE_PRESS, d.getCode(), x, y, e.getButton()));
		}
		if(!happened){
			eventHandler.receiveActionEvent(ActionEventGenerator.generateMouseActionEvent(ActionEventGenerator.MOUSE_PRESS, -1, x, y, e.getButton()));
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e){
		//This space intentionally left blank, but doesn't need to be
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		eventHandler.requestFocusInWindow();
		Integer x = e.getX();
		Integer y = e.getY();
		boolean happened = false;
		for(Detectable d : findClicked(x, y)) {
			happened = true;
			eventHandler.receiveActionEvent(ActionEventGenerator.generateMouseActionEvent(ActionEventGenerator.MOUSE_DRAG, d.getCode(), x, y, e.getButton()));
		}
		if(!happened){
			eventHandler.receiveActionEvent(ActionEventGenerator.generateMouseActionEvent(ActionEventGenerator.MOUSE_DRAG, -1, x, y, e.getButton()));
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		eventHandler.requestFocusInWindow();
		Integer x = e.getX();
		Integer y = e.getY();
		boolean happened = false;
		for(Detectable d : findClicked(x, y)) {
			happened = true;
			eventHandler.receiveActionEvent(ActionEventGenerator.generateMouseActionEvent(ActionEventGenerator.MOUSE_MOVE, d.getCode(), x, y, e.getButton()));
		}
		if(!happened)
			eventHandler.receiveActionEvent(ActionEventGenerator.generateMouseActionEvent(ActionEventGenerator.MOUSE_MOVE, -1, x, y, e.getButton()));
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		eventHandler.receiveActionEvent(ActionEventGenerator.generateMouseWheelActionEvent((int)(e.getPreciseWheelRotation())));
	}
	
	//-- Support  ---------------------------------------------
	
	private List<Detectable> findClicked(int x, int y) {
		List<Detectable> response = new ArrayList<Detectable>();
		lock.lock();
		for(Detectable d : (new HashMap<Integer, Detectable>(detectionRegions)).values()) {
			if(d.wasClicked(x, y)) {
				response.add(d);
			}
		}
		Collections.sort(response);
		lock.unlock();
		return response;
	}

}
