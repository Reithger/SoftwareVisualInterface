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
	
	/** int value describing the priority of this object for being drawn: the lower this value, the sooner it is drawn relative to other Elements*/
	private int priority;
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * This method describes how this Element should be drawn to the screen using a Graphics object.
	 * 
	 * @param g - Graphics object used to draw this Element to the screen.
	 */
	
	public abstract void drawToScreen(Graphics g);
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * Getter method that requests the numerical priority of this Element for being drawn.
	 * 
	 * @return - Returns an int value describing the drawing priority for this Element.
	 */
	
	public int getDrawPriority() {
		return priority;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
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
		int a = this.getDrawPriority();
		int b = d.getDrawPriority();
		if(a > b)
			return 1;
		if(a < b)
			return -1;
		return 0;
	}
}
