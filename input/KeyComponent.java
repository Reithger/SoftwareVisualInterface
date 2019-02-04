package input;
import javax.swing.*;

import visual.panel.Panel;

import java.awt.event.*;


public class KeyComponent extends JComponent implements KeyListener{

//---  Constant Values   ----------------------------------------------------------------------
	
	/** */
	private static final long serialVersionUID = 1L;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** char value representing which keyboard key's value is being stored as the active value*/
	public char activeSelect;
	/** Panel object providing reference to the Panel to which this KeyComponent is attached*/
	public Panel containerFrame;
	/** */
	public boolean toggleCapital;
	
//---  Constructors   -------------------------------------------------------------------------

	/**
	 * 
	 * @param panel
	 */
	
	public KeyComponent(Panel panel){
		resetSelected();
		containerFrame = panel;
		panel.addKeyListener(this);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------

	/**
	 * 
	 * @param panel
	 */
	
	public char getSelected(){
		return activeSelect;
	}
	
//---  Operations   ---------------------------------------------------------------------------

	/**
	 * 
	 * @param panel
	 */
	
	public void resetSelected(){
		activeSelect = (char)0;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------

	/**
	 * 
	 * @param panel
	 */
	
	public void setParentFrame(Panel reference){
		containerFrame = reference;
	}
	
//---  Events   -------------------------------------------------------------------------------
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == 16)
			toggleCapital = true;
		String c = e.getKeyChar() + "";
		activeSelect = toggleCapital ? c.toUpperCase().charAt(0) : c.toLowerCase().charAt(0); 
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
