package visual.panel.element;

import input.Detectable;

public interface Clickable {

//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * 
	 * @return
	 */
	
	public abstract Detectable getDetectionRegion();
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * 
	 * @param in
	 * 
	 * @return 
	 */
	
	public abstract boolean focusEvent(char in);
	
}
