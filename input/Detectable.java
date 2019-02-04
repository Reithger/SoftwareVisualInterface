package input;

public interface Detectable {

//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * 
	 * @param xPos
	 * @param yPos
	 * @return
	 */
	
	public abstract boolean wasClicked(int xPos, int yPos);
	
	/**
	 * 
	 * @return
	 */
	
	public abstract int getCode();
	
}
