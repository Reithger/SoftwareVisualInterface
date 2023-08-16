package com.github.softwarevisualinterface.input.keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import com.github.softwarevisualinterface.input.EventFielder;
import com.github.softwarevisualinterface.input.manager.actionevent.ActionEventGenerator;

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
	
	/** EventReceiver object providing reference to the EventReceiver to which this KeyComponent is attached*/
	private EventFielder eventHandler;
	
	private HashMap<Integer, Integer> VK_mapping;
	
//---  Constructors   -------------------------------------------------------------------------

	/**
	 * Constructor for objects of the KeyComponent type that assigns the containing EventReceiver for this
	 * KeyComponent and adds this object to the EventReceiver.
	 * 
	 * @param eventReceiver - EventReceiver object that this KeyComponent object is attached to; where input is directed via KeyEvent.
	 */
	
	public KeyComponent(EventFielder eventReceiver){
		eventHandler = eventReceiver;
		VK_mapping = new HashMap<Integer, Integer>();
		for(int i = 0; i < 4; i++) {
			VK_mapping.put(37 + i, i + 1);
		}
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
		if(e.getKeyCode() >= 37 && e.getKeyCode() <= 40) {
			eventHandler.receiveActionEvent(ActionEventGenerator.generateKeyActionEvent(ActionEventGenerator.KEY_PRESS, (char)(VK_mapping.get(e.getKeyCode())+0)));	
		}
		eventHandler.receiveActionEvent(ActionEventGenerator.generateKeyActionEvent(ActionEventGenerator.KEY_DOWN, e.getKeyChar()));
	}

	@Override
	public void keyReleased(KeyEvent e) {
		eventHandler.receiveActionEvent(ActionEventGenerator.generateKeyActionEvent(ActionEventGenerator.KEY_UP, e.getKeyChar()));
	}

	@Override
	public void keyTyped(KeyEvent e) {
		eventHandler.receiveActionEvent(ActionEventGenerator.generateKeyActionEvent(ActionEventGenerator.KEY_PRESS, e.getKeyChar()));	
	}

}
