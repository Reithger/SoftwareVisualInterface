package input;

import visual.panel.Panel;
import java.awt.event.*;

/**
 * This class implements the KeyListener interface to define behaviors for interpreting
 * user input via the keyboard. When a key is pressed, a new active value is stored and
 * the Panel corresponding to this KeyComponent has its KeyEvent method called with the
 * new value passed accordingly.
 * 
 * Also detects the Shift key being pressed to specify lowercase or uppercase letters.
 * 
 * @author Ada Clevinger
 *
 */

public class KeyComponent implements KeyListener{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** char value representing which keyboard key's value is being stored as the active value (most recently assigned)*/
	public char activeSelect;
	/** Panel object providing reference to the Panel to which this KeyComponent is attached*/
	public Panel containerFrame;
	/** boolean value describing whether or not the user has specified via the Shift-key that provided input is upper or lowercase*/
	public boolean toggleCapital;
	
//---  Constructors   -------------------------------------------------------------------------

	/**
	 * Constructor for objects of the KeyComponent type that assigns the containing Panel for this
	 * KeyComponent and adds this object to the Panel.
	 * 
	 * @param panel - Panel object that this KeyComponent object is attached to; where input is directed via KeyEvent.
	 */
	
	public KeyComponent(Panel panel){
		resetSelected();
		containerFrame = panel;
		panel.getPanel().addKeyListener(this);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------

	/**
	 * Getter method that requests the most recently submitted value to the KeyComponent by the user
	 * via the keyboard.
	 * 
	 * @return - Returns a char value describing the key that the user has most recently pressed as input.
	 */
	
	public char getSelected(){
		return activeSelect;
	}
	
//---  Operations   ---------------------------------------------------------------------------

	/**
	 * This method resets the selected char value to a default (char)0.
	 * 
	 */
	
	public void resetSelected(){
		activeSelect = (char)0;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------

	/**
	 * Setter method to assign a new Panel object as the container for this KeyComponent;
	 * does not add this KeyComponent to the Panel, nor remove it from the previous (if applicable).
	 * 
	 * @param panel - Panel object representing the panel from which user input is detected. 
	 */
	
	public void setParentFrame(Panel reference){
		containerFrame = reference;
	}
	
//---  Events   -------------------------------------------------------------------------------
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == 16)
			toggleCapital = true;
		char c = e.getKeyChar();
		activeSelect = toggleCapital ? (char)(((int)c) - 32) : c;
		containerFrame.keyEvent(getSelected());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == 16)
			toggleCapital = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//This space intentionally left blank, but doesn't need to be		
	}

}
