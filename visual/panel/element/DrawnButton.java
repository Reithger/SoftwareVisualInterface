package visual.panel.element;

import java.awt.Color;
import java.awt.Graphics;
import input.ClickRegionRectangle;

/**
 * This class extends the Element class and implements the Clickable interface
 * 
 * @author Mac Clevinger
 *
 */

public class DrawnButton extends Element implements Clickable{
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private int cornerX;
	/** */
	private int cornerY;
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
		cornerX = x;
		cornerY = y;
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
		cornerX = x;
		cornerY = y;
		width = wid;
		height = hei;
		code = key;
		center = inCenter;
		setDrawPriority(prior);
		color = null;
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void drawToScreen(Graphics g) {
		if(color != null) {
			Color maintain = g.getColor();
			g.setColor(color);
			g.fillRect(cornerX - (center ? width / 2 : 0), cornerY - (center ? height / 2 : 0), width, height);
			g.setColor(maintain);
		}
	}

	@Override
	public boolean focusEvent(char in) {
		return true;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public ClickRegionRectangle getDetectionRegion() {
		return new ClickRegionRectangle(cornerX - (center ? width / 2 : 0), cornerY - (center ? height / 2 : 0), cornerX + width / (center ? 2 : 1), cornerY + height / (center ? 2 : 1), code);
	}

}
