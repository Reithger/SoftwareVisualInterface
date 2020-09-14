package visual.frame;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Timer;
import javax.swing.JFrame;
import timer.TimerRefresh;
import visual.panel.Panel;

/**
 * This abstract class describes the function required from all Frame-type objects and handle some 
 * aspects of construction; that they can receive and display Panel objects as a part of UI, and have
 * a refresh-rate for their display.
 * 
 * @author Ada Clevinger
 *
 */

public abstract class Frame{
	
//---  Constant Values   ----------------------------------------------------------------------
	
	/** int constant value representing the speed at which this Frame calls repaint() to refresh itself*/
	private static final int REFRESH_RATE = 1000/30;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	protected JFrame frame;
	/** Timer object to automatically refresh (repaint) this Frame object at a defined frequency*/
	private Timer timer;
	
//---  Constructors  --------------------------------------------------------------------------
	
	public Frame(int width, int height) {
		frame = new JFrame();
		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				reactToResize();
			}
		});
		frame.getContentPane().setSize(width, height);
		frame.getContentPane().setPreferredSize(new Dimension(width, height));
		frame.setMinimumSize(frame.getSize());
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLayout(null);
		frame.toFront();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		timer = new Timer();
		timer.schedule(new TimerRefresh(this), 0, REFRESH_RATE);
	}
	
//---  Adder Methods   ------------------------------------------------------------------------

	public void addPanelToScreen(Panel panel) {
		frame.add(panel.getPanel());
		panel.setParentFrame(this);
		frame.pack();
	}
	
//---  Remover Methods   ----------------------------------------------------------------------
	
	public void removePanelFromScreen(Panel panel) {
		frame.remove(panel.getPanel());
	}

	public void removeScreenContents() {
		frame.getContentPane().removeAll();
	}
	
//---  Setter Methods   -----------------------------------------------------------------------

	public void setName(String name) {
		frame.setTitle(name);
	}
	
	public void setExitOnClose(boolean decide) {
		if(decide) {
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		else {
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
	}
	
	public void setResizable(boolean decide) {
		frame.setResizable(decide);
	}
	
	public void resize(int wid, int hei) {
		frame.setSize(wid, hei);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public abstract Panel getPanel(String name);

	public int getWidth() {
		return frame.getWidth();
	}
	
	public int getHeight() {
		return frame.getHeight();
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void repaint() {
		frame.repaint();
	}

	public void disposeFrame() {
		frame.dispose();
	}
	
	/**
	 * All Frame objects need to be able to have Panel objects added to them; the addition
	 * of a name allows for multiple Panels to be used distinctly.
	 * 
	 * @param name - String object representing the desired name of the provided Panel object
	 * @param panel - Panel object being added to this Frame object
	 */
	
	public abstract void reservePanel(String name, Panel panel);
	
	public abstract void dispenseAttention();
	
	public abstract void reactToResize();
	
}
