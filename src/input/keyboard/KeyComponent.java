package input.keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import input.EventFielder;
import input.manager.ActionEvent;

/**
 * This class implements the KeyListener interface to define behaviors for interpreting
 * user input via the keyboard. When a key is pressed, a new active value is stored and
 * the EventReceiver corresponding to this KeyComponent has its KeyEvent method called with the
 * new value passed accordingly.
 * 
 * Also detects the Shift key being pressed to specify lowercase or uppercase letters.
 * 
 * @author Ada Clevinger
 *
 */

public class KeyComponent implements KeyListener{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** char value representing which keyboard key's value is being stored as the active value (most recently assigned)*/
	public char activeSelect;
	/** EventReceiver object providing reference to the EventReceiver to which this KeyComponent is attached*/
	public EventFielder eventHandler;
	
//---  Constructors   -------------------------------------------------------------------------

	/**
	 * Constructor for objects of the KeyComponent type that assigns the containing EventReceiver for this
	 * KeyComponent and adds this object to the EventReceiver.
	 * 
	 * @param eventReceiver - EventReceiver object that this KeyComponent object is attached to; where input is directed via KeyEvent.
	 */
	
	public KeyComponent(EventFielder eventReceiver){
		resetSelected();
		eventHandler = eventReceiver;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------

	/**
	 * Getter method that requests the most recently submitted value to the KeyComponent by the user
	 * via the keyboard.
	 * 
	 * @return - Returns a char value describing the key that the user has most recently pressed as input.
	 */
	
	public char getSelected(){
		return activeSelect;
	}
	
//---  Operations   ---------------------------------------------------------------------------

	/**
	 * This method resets the selected char value to a default (char)0.
	 * 
	 */
	
	public void resetSelected(){
		activeSelect = (char)0;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------

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
	
//---  Events   -------------------------------------------------------------------------------
	
	@Override
	public void keyPressed(KeyEvent e) {
		eventHandler.receiveActionEvent(new ActionEvent(ActionEvent.EVENT_KEY, e.getKeyChar()));
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//This space intentionally left blank, but doesn't need to be
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//This space intentionally left blank, but doesn't need to be		
	}

}
