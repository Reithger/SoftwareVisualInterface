package visual.panel.element;

import java.awt.Color;
import java.awt.Graphics;

public class DrawnRectangle extends Element{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private int xLow;
	/** */
	private int yLow;
	/** */
	private int xHigh;
	/** */
	private int yHigh;
	/** */
	private Color colorBorder;
	/** */
	private Color colorFill;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param color
	 */
	
	public DrawnRectangle(int xL, int yL, int xH, int yH, int prior, Color color) {
		xLow = xL;
		yLow = yL;
		xHigh = xH;
		yHigh = yH;
		colorFill = color;
		colorBorder = color;
		setDrawPriority(prior);
	}
	
	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param fillColor
	 * @param outlineColor
	 */
	
	public DrawnRectangle(int xL, int yL, int xH, int yH, int prior, Color fillColor, Color outlineColor) {
		xLow = xL;
		yLow = yL;
		xHigh = xH;
		yHigh = yH;
		colorFill = fillColor;
		colorBorder = outlineColor;
		setDrawPriority(prior);
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void drawToScreen(Graphics g) {
		Color save = g.getColor();
		g.setColor(colorFill);
		g.fillRect(xLow, yLow, xHigh - xLow, yHigh - yLow);
		g.setColor(colorBorder);
		g.drawRect(xLow, yLow, xHigh - xLow, yHigh - yLow);
		g.setColor(save);
	}
	
}
