package visual.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPanel;

import input.EventFielder;
import input.EventReceiver;
import input.mouse.Detectable;
import visual.frame.Frame;

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

public abstract class Panel implements Comparable<Panel>, EventReceiver{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	protected JPanelWrap panel;
	/** Frame object that houses this Panel object; may be null if unassociated, parentFrame may not always show this Panel*/
	private Frame parentFrame;
	
	private EventFielder eventHandler;
	
	private boolean attention;
	/** int value representing how displaced along the x-axis elements within this Panel need to be drawn relative to their origin point in the Frame*/
	private int offsetX;
	
	private int offsetY;
	
	private int priority;
	
	private volatile boolean mutex;
	
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
		panel = new JPanelWrap(this);
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
	
//---  Setter Methods   -----------------------------------------------------------------------

	/**
	 * Setter method to assign a provided Frame object as this Panel's containing Frame.
	 * 
	 * @param fram - Frame object which this Panel object will have reference to.
	 */
	
	public void setParentFrame(Frame fram) {
		parentFrame = fram;
		
	}
	
	public void setPriority(int in) {
		priority = in;
		panel.setDrawPriority(in);
	}
	
	public void setAttention(boolean atten) {
		attention = atten;
	}
		
	public void setOffsetX(int inX) {
		offsetX = inX;
	}
	
	public void setOffsetY(int inY) {
		offsetY = inY;
	}

	public void setOffsetXBounded(int inX) {
		if(inX > -getMinimumScreenX()) {
			offsetX = -getMinimumScreenX();
		}
		else if(inX < getWidth() - getMaximumScreenX()) {
			offsetX = getWidth() - getMaximumScreenX();
		}
		else {
			offsetX = inX;
		}
	}
	
	public void setOffsetYBounded(int inY) {
		if(inY > -getMinimumScreenY()) {
			offsetY = -getMinimumScreenY();
		}
		else if(inY < getHeight() - getMaximumScreenY()) {
			offsetY = getHeight() - getMaximumScreenY();
		}
		else {
			offsetY = inY;
		}
	}
	
	public void setLocation(int x, int y) {
		panel.setLocation(x, y);
	}
	
	private void bullshitResize(int width, int height) {
		panel.setSize(width - Frame.BULLSHIT_OFFSET_X, height - Frame.BULLSHIT_OFFSET_Y);
		panel.setPreferredSize(new Dimension(width - Frame.BULLSHIT_OFFSET_X, height - Frame.BULLSHIT_OFFSET_Y));
	}
	
	public void resize(int width, int height) {
		panel.setSize(width, height);
		panel.setPreferredSize(new Dimension(width, height));
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
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
	
	public int getOffsetX() {
		return offsetX;
	}
	
	public int getOffsetY() {
		return offsetY;
	}
	
	public abstract int getMinimumScreenX();
	
	public abstract int getMaximumScreenX();
	
	public abstract int getMinimumScreenY();
	
	public abstract int getMaximumScreenY();
	
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
	
	public void addClickRegion(Detectable detect) {
		eventHandler.addClickRegion(detect);
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
	
	public boolean removeClickRegion(int code) {
		return eventHandler.removeDetectionRegion(code);
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
	
	protected void openLock() {
		while(mutex) {}
		mutex = true;
	}
	
	protected void closeLock() {
		mutex = false;
	}
	
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
	
//---  Support Class   ------------------------------------------------------------------------
	
	class JPanelWrap extends JPanel implements Comparable<JPanelWrap>{
		
		private Panel container;
		private int priority;
		
		public JPanelWrap(Panel pan) {
			container = pan;
		}
				
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			container.paintComponent(g);
		}
		
		public int getDrawPriority() {
			return priority;
		}
		
		public void setDrawPriority(int in) {
			priority = in;
		}
		
		@Override
		public int compareTo(JPanelWrap p) {
			int a = this.getDrawPriority();
			int b = p.getDrawPriority();
			if(a > b)
				return 1;
			if(a < b)
				return -1;
			return 0;
		}
	}
	
}
