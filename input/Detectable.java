package input;

/**
 * This interface functions to define the behavior of objects which are used
 * to define regions of the screen that respond to input.
 * 
 * @author Mac Clevinger
 *
 */

public interface Detectable {

//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * This method provides the location that the user clicked relative to the
	 * entity containing this Detectable object, for which the implementing class
	 * defines the means by which that object decides if it was clicked or not.
	 * 
	 * @param xPos - int value representing the x coordinate of the point that the user clicked
	 * @param yPos - int value representing the y coordinate of the point that the user clicked
	 * @return - Returns a boolean value representing whether or not this Detectable object was clicked
	 */
	
	public abstract boolean wasClicked(int xPos, int yPos);
	
	/**
	 * 
	 * @return
	 */
	
	public abstract int getCode();
	
}
