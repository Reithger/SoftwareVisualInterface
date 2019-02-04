package visual.panel.element;

import java.awt.Color;
import java.awt.Graphics;

public class DrawnRectangle extends Element{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private int xCorner1;
	/** */
	private int yCorner1;
	/** */
	private int xCorner2;
	/** */
	private int yCorner2;
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
	
	public DrawnRectangle(int x1, int y1, int x2, int y2, int prior, Color color) {
		xCorner1 = x1;
		yCorner1 = y1;
		xCorner2 = x2;
		yCorner2 = y2;
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
	
	public DrawnRectangle(int x1, int y1, int x2, int y2, int prior, Color fillColor, Color outlineColor) {
		xCorner1 = x1;
		yCorner1 = y1;
		xCorner2 = x2;
		yCorner2 = y2;
		colorFill = fillColor;
		colorBorder = outlineColor;
		setDrawPriority(prior);
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void drawToScreen(Graphics g) {
		Color save = g.getColor();
		g.setColor(colorFill);
		g.fillRect(xCorner1, yCorner1, xCorner2, yCorner2);
		g.setColor(colorBorder);
		g.drawRect(xCorner1, yCorner1, xCorner2, yCorner2);
		g.setColor(save);
	}
	
}
