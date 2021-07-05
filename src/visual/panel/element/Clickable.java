package visual.panel.element;

import input.mouse.Detectable;

/**
 * This interface defines the behavior of Element objects that the user can
 * use the mouse to click; Clickable objects are required to have detectable
 * regions and have reactions to user key-input after being selected. (Can
 * just do nothing if you like.)
 * 
 * @author Ada Clevinger
 *
 */

public interface Clickable {

//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * Getter method to generate a Detectable object for this Clickable object that defines
	 * the region that will detect interaction.
	 * 
	 * @return - Returns the Detectable object for this Clickable object.
	 */
	
	public abstract Detectable getDetectionRegion(int offsetX, int offsetY);
	
	/**
	 * Getter method to request the integer code that has been associated to this Clickable object;
	 * this code would be generated whenever this Element object is interacted with, typically by
	 * clicking on a region of the screen specified by the Detectable object retrieved from
	 * the getDetectionRegion() function.
	 * 
	 * @return - Returns an integer value representing the code value associated to this Clickable object
	 */
	
	public abstract int getCode();
	
	public abstract int getIdentity();
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * This method describes the behavior of Clickable objects when user actions via
	 * the keyboard occur while this object is the most recently selected one. (i.e, the
	 * user clicked on this object.)
	 * 
	 * @param in - char value describing the input to be handled by this Clickable object.
	 * 
	 * @return - Returns a boolean value describing whether or not Key Input can be interpreted after usage here.
	 */
	
	public abstract boolean focusKeyEvent(char in);
	
	public abstract boolean focusDragEvent(int x, int y, int mouseType);
	
}
