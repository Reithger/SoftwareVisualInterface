package visual.panel.element;

import java.awt.Color;
import java.awt.Graphics;

public class DrawnRectangle extends Element{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private int width;
	/** */
	private int height;
	/** */
	private Color colorBorder;
	/** */
	private Color colorFill;
	
	private boolean center;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param color
	 */
	
	public DrawnRectangle(int inX, int inY, int inWidth, int inHeight, int prior, boolean inCenter, Color color) {
		setX(inX);
		setY(inY);
		width = inWidth;
		height = inHeight;
		colorFill = color;
		colorBorder = color;
		center = inCenter;
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
	
	public DrawnRectangle(int xIn, int yIn, int inWidth, int inHeight, int prior, boolean inCenter, Color fillColor, Color outlineColor) {
		setX(xIn);
		setY(yIn);
		width = inWidth;
		height = inHeight;
		colorFill = fillColor;
		colorBorder = outlineColor;
		center = inCenter;
		setDrawPriority(prior);
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void drawToScreen(Graphics g) {
		Color save = g.getColor();
		g.setColor(colorFill);
		g.fillRect(getX() - (center ? width / 2 : 0), getY() - (center ? height / 2 : 0), width, height);
		g.setColor(colorBorder);
		g.drawRect(getX() - (center ? width / 2 : 0), getY() - (center ? height / 2 : 0), width, height);
		g.setColor(save);
	}
	
}
