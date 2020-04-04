package visual.panel.element;

import input.Detectable;

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
	
	public abstract Detectable getDetectionRegion();
	
	public abstract int getCode();
	
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
	
	public abstract boolean focusEvent(char in);
	
}
