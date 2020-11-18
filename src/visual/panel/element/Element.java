package visual.panel.element;

import java.awt.Graphics;

/**
 * This abstract class defines the properties of all Element objects in the SWI library,
 * used by the ElementPanel class for putting visual elements on a Frame for visual projects.
 * 
 * All Elements have a priority level for the ordering of being drawn, and can be drawn to
 * the screen. The Comparable interface is implemented for sorting purposes.
 * 
 * Priority works like this: the lower the priority, the sooner it is drawn. This causes 'high'
 * priority Elements (low values of priority) to be drawn BELOW other objects; 'low' priority
 * Elements (high values of priority) are drawn on top of 'higher' priority Elements.
 * 
 * Think of Priority as levels of a building: the ground floor may be built first, but the
 * third floor will be on top of it.
 * 
 * @author Ada Clevinger
 *
 */

public abstract class Element implements Comparable<Element>{

//---  Instance Variables   -------------------------------------------------------------------
	
	private int x;
	
	private int y;
	/** int value describing the priority of this object for being drawn; the lower this value, the sooner it is drawn relative to other Elements*/
	private int priority;
	
	private int hash;
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * This method describes how this Element should be drawn to the screen using a Graphics object.
	 * 
	 * @param g - Graphics object used to draw this Element to the screen.
	 */
	
	public abstract void drawToScreen(Graphics g, int offsetX, int offsetY);
	
	public void moveElement(int newX, int newY) {
		
	}
	
//---  Getter Methods   -----------------------------------------------------------------------

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	/**
	 * Getter method that requests the numerical priority of this Element for being drawn.
	 * 
	 * @return - Returns an int value describing the drawing priority for this Element.
	 */
	
	public int getDrawPriority() {
		return priority;
	}

	public abstract int getMinimumX();
	
	public abstract int getMaximumX();
	
	public abstract int getMinimumY();
	
	public abstract int getMaximumY();
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setX(int newX) {
		x = newX;
	}
	
	public void setY(int newY) {
		y = newY;
	}
	
	/**
	 * Setter method that assigns the provided int value to this Element object as its
	 * new priority for being drawn relative to other Element objects that are drawn in
	 * the same region (i.e, the same Panel.)
	 * 
	 * @param in - int value representing the new priority of this Element object.
	 */
	
	public void setDrawPriority(int in) {
		priority = in;
	}
	
	@Override
	public int compareTo(Element d) {
		if(d == null) {
			return 1;
		}
		int a = this.getDrawPriority();
		int b = d.getDrawPriority();
		if(a > b)
			return 1;
		if(a < b)
			return -1;
		return 0;
	}

	public void setHash(String in) {
		hash = in.hashCode();
	}
	
//---  Mechanics   ----------------------------------------------------------------------------
	
	@Override
	public int hashCode() {
		return hash;
	}
	
}
