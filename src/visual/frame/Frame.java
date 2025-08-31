package visual.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Timer;
import javax.swing.JFrame;

import visual.frame.timer.TimerRefresh;
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
	public static final int BULLSHIT_OFFSET_X = 16;
	public static final int BULLSHIT_OFFSET_Y = 39;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	protected JFrame frame;
	/** Timer object to automatically refresh (repaint) this Frame object at a defined frequency*/
	private Timer timer;
	
	private boolean wipe;
	
//---  Constructors  --------------------------------------------------------------------------
	
	public Frame(int width, int height) {
		frame = new JFrame() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponents(Graphics g) {
				if(wipe) {
					Color save = g.getColor();
					g.setColor(Color.white);
					g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
					g.setColor(save);
					wipe = false;
				}
				super.paintComponents(g);
			}
		};

		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				reactToResize();
			}
		});
		resize(width, height);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLayout(null);
		frame.toFront();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		timer = new Timer();
		timer.schedule(new TimerRefresh(this), 0, REFRESH_RATE);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void repaint() {
		frame.repaint();
	}

	public void disposeFrame() {
		timer.cancel();
		frame.setVisible(false);
		frame.dispose();
	}
	
	/**
	 * All Frame objects need to be able to have Panel objects added to them; the addition
	 * of a name allows for multiple Panels to be used distinctly.
	 * 
	 * @param name - String object representing the desired name of the provided Panel object
	 * @param panel - Panel object being added to this Frame object
	 */
	
	public abstract void addPanel(String name, Panel panel);
	
	public abstract void dispenseAttention();
	
	public abstract void reactToResize();
	
//---  Adder Methods   ------------------------------------------------------------------------

	public void addPanelToScreen(Panel panel) {
		frame.add(panel.getPanel());
		panel.setParentFrame(this);
	}
	
//---  Remover Methods   ----------------------------------------------------------------------
	
	public void removePanelFromScreen(Panel panel) {
		frame.remove(panel.getPanel());
		wipe = true;
	}

	public void removeScreenContents() {
		frame.getContentPane().removeAll();
		wipe = true;
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
		Dimension d = new Dimension(wid + BULLSHIT_OFFSET_X, hei + BULLSHIT_OFFSET_Y);
		frame.setSize(d);
		frame.getContentPane().setPreferredSize(d);
	}
	
	public void setLocationRelativeTo(Frame f) {
		frame.setLocationRelativeTo(f == null ? null : f.getFrame());
	}
	
	public void setBackgroundColor(Color col) {
		getFrame().getContentPane().setBackground(col);
		getFrame().setBackground(col);
	}
	
	public void setFrameShapeNormal() {
		setFrameShapeArbitrary(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
	}
	
	public void setFrameShapeDisc() {
		setFrameShapeArbitrary(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
	}
	
	public void setFrameShapeArbitrary(Shape frameShape) {
		frame.setVisible(false);
		frame.dispose();
		frame.setUndecorated(true);
		frame.setShape(frameShape);
		frame.setVisible(true);
	}
	
	public void setShapedFrameTransparent(float transp) {
		frame.setOpacity(transp);
	}
	
	public void setLocation(Point in) {
		frame.setLocation(in);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public Point getLocation() {
		return frame.getLocationOnScreen();
	}
	
	public abstract Panel getPanel(String name);

	public int getWidth() {
		return frame.getWidth() - BULLSHIT_OFFSET_X;
	}
	
	public int getHeight() {
		return frame.getHeight() - BULLSHIT_OFFSET_Y;
	}
	
	protected JFrame getFrame() {
		return frame;
	}

}
