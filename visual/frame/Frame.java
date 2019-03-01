package visual.frame;

import java.util.Timer;
import javax.swing.JFrame;
import timer.TimerRefresh;
import visual.panel.Panel;

/**
 * This abstract class describes the function required from all Frame-type objects and handle some 
 * aspects of construction; that they can receive and display Panel objects as a part of UI, and have
 * a refresh-rate for their display.
 * 
 * @author Mac Clevinger
 *
 */

public abstract class Frame{
	
//---  Constant Values   ----------------------------------------------------------------------
	
	/** int constant value representing the speed at which this Frame calls repaint() to refresh itself*/
	private static final int REFRESH_RATE = 1000/60;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	protected JFrame frame;
	/** Timer object to automatically refresh (repaint) this Frame object at a defined frequency*/
	private Timer timer;
	
//---  Constructor Support   ------------------------------------------------------------------
	
	/**
	 * This method handles some aspects of the creation of Frame objects that are common across
	 * all Frames and are not expected to be actions Frames will want to do differently.
	 * 
	 * @param width - int value representing the width of this Frame object's window
	 * @param height - int value representing the height of this Frame object's window
	 */
	
	public void initiate(int width, int height) {
		frame = new JFrame();
		timer = new Timer();
		timer.schedule(new TimerRefresh(this), 0, REFRESH_RATE);
		frame.setSize(width, height);
		frame.setVisible(true);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
//---  Adder Methods   ------------------------------------------------------------------------
	
	/**
	 * All Frame objects need to be able to have Panel objects added to them; the addition
	 * of a name allows for multiple Panels to be used distinctly.
	 * 
	 * @param name - String object representing the desired name of the provided Panel object
	 * @param panel - Panel object being added to this Frame object
	 */
	
	public abstract void addPanel(String name, Panel panel);
	
	public abstract Panel getPanel(String name);
	
	public void repaint() {
		frame.repaint();
	}
	
	public void add(Panel panel) {
		frame.add(panel.getPanel());
	}
	
	public void remove(Panel panel) {
		frame.remove(panel.getPanel());
	}

	public int getWidth() {
		return frame.getWidth();
	}
	
	public int getHeight() {
		return frame.getHeight();
	}
	
}
