package visual.panel.element;

import java.awt.Color;
import java.awt.Graphics;

import input.ClickRegionRectangle;

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
	
	public DrawnButton(int x, int y, int prior, int wid, int hei, int key, Color col) {
		cornerX = x - wid/2;
		cornerY = y - hei/2;
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
	
	public DrawnButton(int x, int y, int wid, int hei, int key) {
		cornerX = x - wid/2;
		cornerY = y - hei/2;
		width = wid;
		height = hei;
		code = key;
		color = null;
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void drawToScreen(Graphics g) {
		Color maintain = g.getColor();
		if(color != null) {
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
