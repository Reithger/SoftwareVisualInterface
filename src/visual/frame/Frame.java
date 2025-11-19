package visual.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import visual.frame.timer.TimerRefresh;
import visual.panel.Panel;

/**
 * This abstract class describes the function required from all Frame-type objects and handle some 
 * aspects of construction; that they can receive and display Panel objects as a part of UI, and have
 * a refresh-rate for their display.
 * 
 * Frame is a wrapper around the JFrame class from javax.swing with implemented functionality for
 * a 30 fps repaint using the TimerRefresh class (simple TimerTask subclass that tells this Frame
 * to repaint).
 * 
 * This class is primarily subclassed by WindowFrame, which introduces organizing the Panels added
 * to the Frame by Window categories that can be displayed/hidden as groups.
 * 
 * TODO: Some obscure issue where a Panel loses the ability to gain focus after being hidden/shown,
 * not able to replicate the circumstances that caused this but telling an unrelated Panel that
 * maintained focus to re-show itself while already visible fixed this for some reason.
 * 
 * @author Ada Clevinger
 *
 */

public abstract class Frame{
	
//---  Constant Values   ----------------------------------------------------------------------
	
	/** int constant value representing the speed at which this Frame calls repaint() to refresh itself*/
	private static final int REFRESH_RATE = 1000/30;
	
	/** For some reason drawing needs a slight offset to draw in the Frame properly */
	public static final int BULLSHIT_OFFSET_X = 16;
	public static final int BULLSHIT_OFFSET_Y = 39;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	protected JFrame frame;
	/** Timer object to automatically refresh (repaint) this Frame object at a defined frequency*/
	private Timer timer;
	/** boolean value to denote whether Frame should draw a blank white screen to reset the screen*/
	private boolean wipe;
	
//---  Constructors  --------------------------------------------------------------------------
	
	/**
	 * Constructor for the Frame class that instantiates the wrapped JFrame, sets its size to
	 * the provided width and height, and sets several default properties of the wrapped JFrame.
	 * 
	 * By default:
	 *  - resizable is false
	 *  - visible is true
	 *  - layout is null
	 *   - note, technically a JFrame is a wrapper of a JRootPane which is a LayoutManager around
	 *     a ContentPane which is the actual container that's layout is set to null
	 *  - close operation is to end the entire program
	 *  - auto request focus is true
	 *  - icon image for the program is a cute hamster I drew
	 *  
	 *  This constructor also establishes that a boolean 'wipe' can be set to true to cause
	 *  the Frame to overwrite its contents with blank white coloring to 'wipe' the screen.
	 * 
	 * @param width
	 * @param height
	 */
	
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
		frame.getContentPane().setLayout(null);
		frame.toFront();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setAutoRequestFocus(true);
		
		//TODO: jar packaged version's pathing isn't finding the image
		Image img = getImage("./visual/frame/assets/hamster_base_2.png");

		setIconImage(img);
		
		timer = new Timer();
		timer.schedule(new TimerRefresh(this), 0, REFRESH_RATE);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * Wrapper function that passes the repaint request to the wrapped JFrame.
	 * 
	 * If you want to hijack the innate 30 fps repaint timer of this Frame, you
	 * can overwrite this function; make sure you still call super.repaint() though.
	 * 
	 * Generally you should have a separate timer, do not put program logic on the same
	 * thread as graphical drawing.
	 * 
	 */
	
	public void repaint() {
		frame.repaint();
	}
	
	/**
	 * Wrapper function for assigning a new icon image to this Frame via the underlying JFrame.
	 * 
	 * @param in
	 */
	
	public void setIconImage(Image in) {
		if(in != null)
			frame.setIconImage(in);
	}
	
	/**
	 * Wrapper function for assigning a new icon image to this Frame via the underlying JFrame.
	 * 
	 * @param in
	 */
	
	public void setIconImage(String in) {
		frame.setIconImage(getImage(in));
	}

	/**
	 * This function ends the functioning of this Frame, terminating its timer thread and
	 * disposing of the underlying JFrame.
	 * 
	 * This kills the Frame.
	 * 
	 */
	
	public void disposeFrame() {
		timer.cancel();
		frame.setVisible(false);
		frame.dispose();
	}
	
	/**
	 * All Frame objects need to be able to have Panel objects added to them; the addition
	 * of a name allows for multiple Panels to be used distinctly.
	 * 
	 * This is distinct from 'addPanelToScreen' in that it gives the subclass implementing
	 * this class an opportunity to manage the storage of its Panels for repeat access/reference;
	 * also 'addPanelToScreen' is protected visibility so an end-user will not see that function.
	 * 
	 * Ideally, an implementation of this function eventually calls 'addPanelToScreen' as that
	 * accesses the underlying JFrame's underlying JRootPane's underlying Container.
	 * 
	 * @param name - String object representing the desired name of the provided Panel object
	 * @param panel - Panel object being added to this Frame object
	 */
	
	public abstract void addPanel(String name, Panel panel);
	
	public abstract Panel getPanel(String name);

	/**
	 * This function should be implemented to instruct all composite Panel objects to
	 * forsake their having attention so that a particular Panel can then request
	 * attention for itself.
	 * 
	 * Look, the focus subsystem for javax.swing components is really rough and annoying,
	 * and to maintain that the Panel you just clicked on is still the actively looked at
	 * window for key input, it has to continually grabFocus for itself by knowing it is
	 * the focused panel and telling every other Panel to stop having focus.
	 * 
	 * The implementation of this function provides a way to tell every other Panel to
	 * stop having focus.
	 * 
	 */
	
	public abstract void dispenseAttention();
	
	/**
	 * Upon resize being detected by the underlying JFrame, this function is called so
	 * that your implementation of the abstract Frame can do anything in response to the
	 * width/height changing.
	 * 
	 */
	
	public abstract void reactToResize();
	
//---  Adder Methods   ------------------------------------------------------------------------

	/**
	 * Wrapper function for the JFrame function for adding a JPanel to its content pane.
	 * 
	 * Currently, it fields the request to the JFrame's JRootPane's ContentPane to add
	 * the javax.swing.JPanel object retrieved from the Panel.
	 * 
	 * It then informs the Panel to consider this Frame its hierarchical parent, and then
	 * calls on JFrame to revalidate itself which it is unclear why that is a feature of
	 * java.awt/javax.swing
	 * 
	 * @param panel - Panel object that contains a JPanel we can retrieve to add to the JFrame
	 */
	
	protected void addPanelToScreen(Panel panel) {
		frame.getContentPane().add(panel.getPanel());
		panel.setParentFrame(this);
		frame.revalidate();
	}
	
//---  Remover Methods   ----------------------------------------------------------------------
	
	/**
	 * Wrapper function for the JFrame function for removing a JPanel from its content pane.
	 * 
	 * Currently, it fields the request to the JFrame's JRootPane's ContentPane to remove
	 * the javax.swing JPanel object retrieved from the Panel.
	 * 
	 * It then resets the Panel's parent frame to be null, sets 'wipe' to true to reset
	 * the visual display, and calls on JFrame to revalidate though we don't really know why.
	 * 
	 * @param panel
	 */
	
	protected void removePanelFromScreen(Panel panel) {
		frame.getContentPane().remove(panel.getPanel());
		panel.setParentFrame(null);
		wipe = true;
		frame.revalidate();
	}
	
	/**
	 * Wrapper function for JFrame to remove all components from its content pane, then
	 * calls to revalidate and sets 'wipe' to true to reset the visual display.
	 * 
	 */

	public void removeScreenContents() {
		frame.getContentPane().removeAll();
		frame.revalidate();
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
	
	/**
	 * 
	 * This function is a wrapper for the JFrame 'setLocationRelativeTo' function; it makes
	 * the screen position of the new Frame you have created have its origin be relative to
	 * the supplied Frame object's position.
	 * 
	 * This is so that a newly generated Frame can appear nearby another window instead of
	 * in the default 0, 0 corner of the screen (and potentially on a different monitor than
	 * the frame that generated the new frame).
	 * 
	 * @param f
	 */
	
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

	public int getWidth() {
		return frame.getWidth() - BULLSHIT_OFFSET_X;
	}
	
	public int getHeight() {
		return frame.getHeight() - BULLSHIT_OFFSET_Y;
	}
	
	protected JFrame getFrame() {
		return frame;
	}
	
//---  Mechanics   ----------------------------------------------------------------------------
	
	private Image getImage(String pathIn) {
		String path = pathIn.replace("\\", "/");
		try {
			return ImageIO.read(Frame.class.getResource(path.substring(path.indexOf("/"))));
		}
		catch(Exception e) {
			try {
				return ImageIO.read(new File(path));
			}
			catch(Exception e1) {
				return null;
			}
		}
	}

}
