package com.github.softwarevisualinterface.visual.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JPanel;

import com.github.softwarevisualinterface.input.ComponentReceiver;
import com.github.softwarevisualinterface.input.CustomEventReceiver;
import com.github.softwarevisualinterface.input.EventFielder;
import com.github.softwarevisualinterface.input.EventReceiver;
import com.github.softwarevisualinterface.input.NestedEventReceiver;
import com.github.softwarevisualinterface.input.mouse.Detectable;
import com.github.softwarevisualinterface.visual.frame.Frame;

/**
 * This abstract class defines the base behavior of Panel objects in the SoftwareVisualInterface library,
 * handling input via the Mouse and Keyboard and abstracting many low-details away.
 * 
 * Panel objects need to be placed inside of Frame objects, and specific implementations of this abstract class
 * will provide tools for specifying how to manipulate the appearance of the Panel.
 * 
 * @author Ada Clevinger
 *
 */

public abstract class Panel implements Comparable<Panel>, ComponentReceiver{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	protected JPanel panel;
	/** Frame object that houses this Panel object; may be null if unassociated, parentFrame may not always show this Panel*/
	private Frame parentFrame;
	
	private EventFielder eventHandler;
	
	private boolean attention;
	
	private int priority;

	/** The lock used for synchronization. */
	protected final Lock lock = new ReentrantLock(true);
	
	private NestedEventReceiver inputHandler;
	
//---  Constructor Support   ------------------------------------------------------------------
	
	/**
	 * This method handles common actions that all Panel objects are expected to undertake,
	 * abstracting some aspects of preparing the Panel in implementations of the abstract class.
	 * 
	 * @param x - int value representing the x position of this Panel object's origin in the containing Frame object
	 * @param y - int value representing the y position of this Panel object's origin in the containing Frame object
	 * @param width - int value representing the width of this Panel object
	 * @param height - int value representing the height of this Panel object
	 */
	
	public Panel(int x, int y, int width, int height) {
		Panel p = this;
		setEventReceiver(new CustomEventReceiver());
		panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				p.paintComponent(g);
			}
		};
		panel.setDoubleBuffered(true);
		panel.setFocusable(true);
		panel.setLocation(x, y);
		panel.setSize(width, height);
		panel.setPreferredSize(new Dimension(width, height));
		panel.setVisible(true);
		eventHandler = new EventFielder(this);
		attention = true;
		panel.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {

			}

			@Override
			public void focusLost(FocusEvent e) {
				if(attention) {
					panel.requestFocusInWindow();
				}
			}
			
		});
	}

//---  Operations   ---------------------------------------------------------------------------
	
	public abstract void paintComponent(Graphics g);

	public void repaint() {
		getPanel().repaint();
	}

	/**
	 * This method instructs the ClickComponent object of this class to reset the regions that
	 * have been assigned a particular value to return if clicked. (See documentation of
	 * ClickComponent for further information on its functionality.)
	 * 
	 * Effectively resets how this Panel object responds to user mouse-clicking.
	 * 
	 */
	
	public void resetDetectionRegions() {
		eventHandler.resetDetectionRegions();
	}

	public void requestFocusInWindow() {
		getPanel().requestFocusInWindow();
	}
	
	public void addEventReceiver(EventReceiver in) {
		if(inputHandler == null) {
			inputHandler = new NestedEventReceiver(in);
		}
		else {
			inputHandler.addNested(in);
		}
	}

	public void addEventReceiver(String label, EventReceiver in) {
		if(inputHandler == null) {
			inputHandler = new NestedEventReceiver(in, label);
		}
		else {
			inputHandler.addNested(in, label);
		}
	}
	
	public void removeEventReceiver(String label) {
		if(inputHandler != null) {
			inputHandler.removeNestedEventReceiver(label);
		}
	}
	
//---  Setter Methods   -----------------------------------------------------------------------

	/**
	 * Setter method to assign a provided Frame object as this Panel's containing Frame.
	 * 
	 * @param fram - Frame object which this Panel object will have reference to.
	 */
	
	public void setParentFrame(Frame fram) {
		parentFrame = fram;
		
	}
	
	public void setEventReceiver(EventReceiver in) {
		inputHandler = new NestedEventReceiver(in);
	}
	
	public void setEventReceiver(NestedEventReceiver in) {
		inputHandler = in;
	}
	
	public void setPriority(int in) {
		priority = in;
	}
	
	public void setAttention(boolean atten) {
		attention = atten;
	}
		
	public void setLocation(int x, int y) {
		panel.setLocation(x, y);
	}
	
	public void resize(int width, int height) {
		panel.setSize(width, height);
		panel.setPreferredSize(new Dimension(width, height));
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public NestedEventReceiver getEventReceiver() {
		return inputHandler;
	}
	
	public Component getListenerRecipient() {
		return getPanel();
	}
	
	public int getDrawPriority() {
		return priority;
	}
	
	/**
	 * Getter method that requests the X position of this Panel object's origin in the
	 * Frame that contains it.
	 * 
	 * @return - Returns an int value representing the X position of this Panel object's origin in the Frame that contains it.
	 */
	
	public int getPanelXLocation() {
		return panel.getX();
	}
	
	/**
	 * Getter method that requests the Y position of this Panel object's origin in the
	 * Frame that contains it.
	 * 
	 * @return - Returns an int value representing the Y position of this Panel object's origin in the Frame that contains it.
	 */
	
	public int getPanelYLocation() {
		return panel.getY();
	}
	
	public int getWidth() {
		return panel.getWidth();
	}
	
	public int getHeight() {
		return panel.getHeight();
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public Frame getParentFrame() {
		return parentFrame;
	}

//---  Adder Methods   ------------------------------------------------------------------------
	
	/**
	 * Adder method that takes a provided Detectable object and passes it to this Panel's
	 * ClickComponent to define a new region of the Panel that, when clicked, will generate
	 * a code value defined by the Detectable object (this is what is given to clickEvent()).
	 * 
	 * If there is a collision of code values (two regions assigned the same value), the
	 * new Detectable object will not be added and the method will return false.
	 * 
	 * @param detect - Detectable object that represents a region of the screen 
	 * @return - returns a boolean value representing the result of this operation; true if detect was added, false otherwise.
	 */
	
	public void addClickRegion(int identity, Detectable detect) {
		eventHandler.addClickRegion(identity, detect);
	}
	
//---  Remove Methods   -----------------------------------------------------------------------

	/**
	 * This method removes a Detectable object from the ClickComponent object that possesses
	 * the same code as that which is provided to this method.
	 * 
	 * @param code - int value representing the code of the Detectable object to remove from this Panel's ClickComponent.
	 * @return - Returns a boolean value representing the result of this operation; true if a Detectable was removed,
	 * false if no matching object was found.
	 */
	
	public boolean removeClickRegion(int identity) {
		return eventHandler.removeDetectionRegion(identity);
	}
	
	/**
	 * This method removes all Detectable objects from the ClickComponent object that intersect the
	 * point defined by the input to this method.
	 * 
	 * @param x - int value representing the x coordinate of the point that removes all Detectable objects intersecting it
	 * @param y - int value representing the y coordinate of the point that removes all Detectable objects intersecting it
	 * @return - Returns a boolean value representing the result of this operation; true if a Detectable was removed, false
	 * if no matching objects were found. 
	 */
	
	public boolean removeClickRegions(int x, int y) {
		return eventHandler.removeDetectionRegions(x, y);
	}
	
//---  Mechanics   ----------------------------------------------------------------------------
	
	@Override
	public int compareTo(Panel p) {
		int a = this.getDrawPriority();
		int b = p.getDrawPriority();
		if(a > b)
			return 1;
		if(a < b)
			return -1;
		return 0;
	}

}
