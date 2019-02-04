package visual.panel;

import java.awt.Graphics;
import java.util.Timer;
import javax.swing.JPanel;
import input.ClickComponent;
import input.Detectable;
import input.KeyComponent;
import timer.TimerRefresh;
import visual.frame.Frame;
import visual.frame.WindowFrame;

public abstract class Panel extends JPanel{

//---  Constant Values   ----------------------------------------------------------------------


//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private Frame parentFrame;

	/** ClickComponent object that handles the user's mouse clicking to interpret it against defined button regions*/
	private ClickComponent mouseEvent;
	/** KeyComponent object that handles the user's key inputs to inform a caller of what that key is*/
	private KeyComponent keyPress;
	
	private int xPosition;
	
	private int yPosition;
	
//---  Constructor Support   ------------------------------------------------------------------
	
	public void initiate(int x, int y, int width, int height) {
		mouseEvent = new ClickComponent(this);
		keyPress = new KeyComponent(this);
		this.setDoubleBuffered(true);
		setFocusable(true);
		setLocation(x, y);
		setSize(width, height);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public abstract void paintComponent(Graphics g);

	public abstract void clickEvent(int event);
	
	public abstract void keyEvent(char event);
	
	public void resetDetectionRegions() {
		mouseEvent.resetDetectionRegions();
	}

//---  Setter Methods   -----------------------------------------------------------------------

	public void setParentFrame(WindowFrame fram) {
		parentFrame = fram;
		
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public int getPanelXLocation() {
		return xPosition;
	}
	
	public int getPanelYLocation() {
		return yPosition;
	}
	
//---  Adder Methods   ------------------------------------------------------------------------
	
	public boolean addClickRegion(Detectable detect) {
		return mouseEvent.addClickRegion(detect);
	}
	
//---  Remove Methods   -----------------------------------------------------------------------
	
	public boolean removeClickRegion(int code) {
		return mouseEvent.removeDetectionRegion(code);
	}
	
	public boolean removeClickRegions(int x, int y) {
		return mouseEvent.removeDetectionRegions(x, y);
	}
	
}
