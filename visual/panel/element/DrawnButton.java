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
	
	public DrawnButton(int x, int y, int wid, int hei, int prior, int key, Color col) {
		cornerX = x;
		cornerY = y;
		width = wid;
		height = hei;
		code = key;
		color = col;
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
	
	public DrawnButton(int x, int y, int wid, int hei, int prior, int key) {
		cornerX = x;
		cornerY = y;
		width = wid;
		height = hei;
		code = key;
		setDrawPriority(prior);
		color = null;
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void drawToScreen(Graphics g) {
		if(color != null) {
			Color maintain = g.getColor();
			g.setColor(color);
			g.fillRect(cornerX, cornerY, width, height);
			g.setColor(maintain);
		}
	}

	@Override
	public boolean focusEvent(char in) {
		return true;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public ClickRegionRectangle getDetectionRegion() {
		return new ClickRegionRectangle(cornerX , cornerY , cornerX + width , cornerY + height , code);
	}

}
