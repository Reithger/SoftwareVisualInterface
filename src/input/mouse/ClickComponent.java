package input.mouse;

import java.awt.event.*;
import java.util.*;

import input.EventFielder;
import input.manager.ActionEvent;

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
	
	/** int value representing the coded value for which event-region is currently selected*/
	private int activeSelect;
	/** ArrayList<Integer[]> object containing the coordinates and codes for each event-region*/
	private List<Detectable> detectionRegions;
	/** EventReceiver object representing the EventReceiver to which this ClickComponent is attached (the EventReceiver that is being clicked)*/
	private EventFielder eventHandler;
	
	private boolean mutex;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * Constructor for objects of the ClickComponent object; default selected value is -1, and
	 * this object is added as a MouseListener to the provided EventReceiver.
	 * 
	 * @param eventReceiver - EventReceiver object for which this ClickComponent exists; directs input to this EventReceiver.
	 */
	
	public ClickComponent(EventFielder eventReceiver){
		resetSelected();
		detectionRegions = new ArrayList<Detectable>();
		eventHandler = eventReceiver;
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * Getter method that returns the value currently stored by this ClickComponent (that value
	 * being a reflection of the user's interaction with the EventReceiver this is adjoined to.)
	 * 
	 * Value is reset after each call of this method.
	 * 
	 * @return - Returns an int value representing the code value stored by this ClickComponent
	 */
	
	public int getSelected(){
		int out = activeSelect;
		resetSelected();
		return out;
	}
	
//---  Operations   ---------------------------------------------------------------------------

	/**
	 * This method resets the stored value of this ClickComponent to -1 (the default value for
	 * nothing significant having been selected; there may have been a click action, though.)
	 */
	
	public void resetSelected(){
		activeSelect = -1;
	}

	/**
	 * This method resets the list of Detectable objects stored by this ClickComponent; no
	 * regions of the screen will generate code values until new Detectables are added.
	 */
	
	public void resetDetectionRegions() {
		detectionRegions = new ArrayList<Detectable>();
	}
	
	private void sendAction(char c, int x, int y, int code) {
		eventHandler.receiveActionEvent(new ActionEvent(c, x, y, code));
	}
	
	private void sendAction(char c, int in) {
		eventHandler.receiveActionEvent(new ActionEvent(c, in));
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
	
	public boolean removeDetectionRegion(int code) {
		openLock();
		for(int i = 0; i < detectionRegions.size(); i++) {
			Detectable d = detectionRegions.get(i);
			if(d.getCode() == code) {
				detectionRegions.remove(i);
				closeLock();
				return true;
			}
		}
		closeLock();
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
		openLock();
		for(int i = 0; i < detectionRegions.size(); i++) {
			Detectable d = detectionRegions.get(i);
			if(d.wasClicked(x, y)) {
				detectionRegions.remove(i);
				out = true;
			}
		}
		closeLock();
		return out;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------

	/**
	 * This method takes the provided list of Detectable objects and assigns it to this ClickComponent.
	 * 
	 * @param updated - ArrayList<<r>Detectable> object containing Detectable objects
	 */
	
	public void setDetectionRegions(ArrayList<Detectable> updated){
		detectionRegions = updated;
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
	
	public void addClickRegion(Detectable region){
		openLock();
		detectionRegions.add(region);
		closeLock();
	}
	
	private void openLock() {
		while(mutex) {}
		mutex = true;
	}
	
	private void closeLock() {
		mutex = false;
	}
	
//---  Events   -------------------------------------------------------------------------------
	
	@Override
	public void mouseClicked(MouseEvent e){
		eventHandler.requestFocusInWindow();
		Integer x = e.getX();
		Integer y = e.getY();
		boolean happened = false;
		for(Detectable d : findClicked(x, y)) {
			activeSelect = d.getCode();	
			
			sendAction(ActionEvent.EVENT_CLICK, x, y, getSelected());
			
			happened = true;
		}
		if(!happened){
			sendAction(ActionEvent.EVENT_CLICK, x, y, getSelected());
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
			activeSelect = d.getCode();

			sendAction(ActionEvent.EVENT_RELEASE, x, y, getSelected());
		}
		if(!happened) {

			sendAction(ActionEvent.EVENT_RELEASE, x, y, getSelected());
			
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
			activeSelect = d.getCode();

			sendAction(ActionEvent.EVENT_PRESS, x, y, getSelected());
		}
		if(!happened){
			sendAction(ActionEvent.EVENT_PRESS, x, y, getSelected());
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
			activeSelect = d.getCode();

			sendAction(ActionEvent.EVENT_DRAG, x, y, getSelected());
		}
		if(!happened){
			sendAction(ActionEvent.EVENT_DRAG, x, y, getSelected());
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		sendAction(ActionEvent.EVENT_MOUSE_MOVE, e.getX(), e.getY(), -1);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		sendAction(ActionEvent.EVENT_WHEEL, (int)(e.getPreciseWheelRotation()));
	}

	//-- Support  ---------------------------------------------
	
	private ArrayList<Detectable> findClicked(int x, int y) {
		ArrayList<Detectable> response = new ArrayList<Detectable>();
		openLock();
		for(Detectable d : new ArrayList<Detectable>(detectionRegions)) {
			if(d.wasClicked(x, y)) {
				response.add(d);
			}
		}
		Collections.sort(response);
		closeLock();
		return response;
	}

}
