package visual.panel.element;

import java.awt.Color;
import java.awt.Graphics;
import input.ClickRegionRectangle;

/**
 * This class extends the Element class and implements the Clickable interface
 * 
 * @author Ada Clevinger
 *
 */

public class DrawnButton extends Element implements Clickable{
	
//---  Instance Variables   -------------------------------------------------------------------

	/** */
	private int width;
	/** */
	private int height;
	/** */
	private int code;
	/** */
	private Color color;
	
	private boolean center;

//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param wid
	 * @param hei
	 * @param key
	 * @param col
	 */
	
	public DrawnButton(int x, int y, int wid, int hei, int prior, boolean inCenter, int key, Color col) {
		setX(x);
		setY(y);
		width = wid;
		height = hei;
		code = key;
		color = col;
		center = inCenter;
		setDrawPriority(prior);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param wid
	 * @param hei
	 * @param key
	 */
	
	public DrawnButton(int x, int y, int wid, int hei, int prior, boolean inCenter, int key) {
		setX(x);
		setY(y);
		width = wid;
		height = hei;
		code = key;
		center = inCenter;
		setDrawPriority(prior);
		color = null;
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void drawToScreen(Graphics g, int offsetX, int offsetY) {
		if(color != null) {
			Color maintain = g.getColor();
			g.setColor(color);
			g.fillRect(getX() - (center ? width / 2 : 0) + offsetX, getY() - (center ? height / 2 : 0) + offsetY, width, height);
			g.setColor(maintain);
		}
	}

	@Override
	public boolean focusEvent(char in) {
		return true;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public ClickRegionRectangle getDetectionRegion() {
		return new ClickRegionRectangle(getX() - (center ? width / 2 : 0), getY() - (center ? height / 2 : 0), width, height, code);
	}

	public int getCode() {
		return code;
	}
	
}
