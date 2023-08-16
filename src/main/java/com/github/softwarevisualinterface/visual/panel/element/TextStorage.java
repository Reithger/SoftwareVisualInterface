package com.github.softwarevisualinterface.visual.panel.element;

/**
 * This interface describes the behavior of TextStorage objects, which are those that
 * contain some manner of text that it is desirable to retrieve.
 * 
 * @author Ada Clevinger
 *
 */

public interface TextStorage {

//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * This method requests the text associated to this TextStorage object (presumably
	 * user input of some kind, but any Element that can contain variable text should
	 * be able to have it retrieved.)
	 * 
	 * @return - Returns a String object representing the text stored by this TextStorage object.
	 */

	public abstract String getText();
	
	public abstract void setText(String newText);
	
}
