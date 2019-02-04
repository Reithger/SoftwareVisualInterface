package visual.frame;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Timer;

import javax.swing.JFrame;

import timer.TimerRefresh;
import visual.panel.Panel;

/**
 * With the timer, make sure it refreshes the Frame to repaint, not the Panels themselves. Top down.
 * 
 * @author Reithger
 *
 */

public class WindowFrame extends Frame{

//---  Constants   ----------------------------------------------------------------------------
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private HashMap<String, Panel> windows;
	/** */
	private Timer timer;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 * @param width
	 * @param height
	 */
	
	public WindowFrame(int width, int height) {
		super();

		timer = new Timer();
		timer.schedule(new TimerRefresh(this), 0, 1000/15);
		setSize(width, height);
		setVisible(true);
		setLayout(null);
		windows = new HashMap<String, Panel>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

//---  Adder Methods   ------------------------------------------------------------------------
	
	/**
	 * 
	 */
	
	public void addPanel(String title, Panel panel) {
		windows.put(title, panel);
		this.add(panel);
		panel.setParentFrame(this);
	}
	
//---  Remover Methods   ----------------------------------------------------------------------
	
	/**
	 * 
	 * @param name
	 */
	
	public void removePanel(String name) {
		remove(windows.get(name));
		windows.get(name).setParentFrame(null);
		windows.remove(name);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * 
	 * @param name
	 */
	
	public void hidePanel(String name) {
		remove(windows.get(name));
	}
	
	/**
	 * 
	 * @param name
	 */
	
	public void showPanel(String name) {
		add(windows.get(name));
	}
	
}
