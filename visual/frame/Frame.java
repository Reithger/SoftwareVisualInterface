package visual.frame;

import javax.swing.JFrame;

import visual.panel.Panel;

public abstract class Frame extends JFrame{
	
//---  Constant Values   ----------------------------------------------------------------------
	
//---  Adder Methods   ------------------------------------------------------------------------
	
	/**
	 * 
	 * @param name
	 * @param panel
	 */
	
	public abstract void addPanel(String name, Panel panel);
	
}
