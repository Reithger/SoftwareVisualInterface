package visual.panel.element;

import java.awt.Color;
import java.awt.Graphics;

import input.mouse.ClickRegionRectangle;
import input.mouse.Detectable;
import misc.Canvas;

public class DrawnCanvas extends Element implements Clickable{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private int elemWidth;
	/** */
	private int elemHeight;
	
	private int code;
	
	private Canvas canvas;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DrawnCanvas(int x, int y, int priority, int inWidth, int inHeight, int inCode, int canWid, int canHei) {
		setX(x);
		setY(y);
		code = inCode;
		setDrawPriority(priority);
		elemWidth = inWidth;
		elemHeight = inHeight;
		canvas = new Canvas(canWid, canHei);
	}
	
	public DrawnCanvas(int x, int y, int priority, int inWidth, int inHeight, int inCode, Canvas inCanvas) {
		setX(x);
		setY(y);
		code = inCode;
		setDrawPriority(priority);
		elemWidth = inWidth;
		elemHeight = inHeight;
		canvas = inCanvas;
	}
	
//---  Operations   ---------------------------------------------------------------------------

	public boolean focusKeyEvent(char in) {
		canvas.input(in);
		return true;
	}
	
	public boolean focusDragEvent(int x, int y, int mouseType) {
		return true;
	}

	@Override
	public void drawToScreen(Graphics g, int offsetX, int offsetY) {
		Color save = g.getColor();
		canvas.draw(g, getX() + offsetX, getY() + offsetY, getWidth(), getHeight());
		g.setColor(save);
	}

	public void updateElementSize(int elWid, int elHei) {
		elemWidth = elWid;
		elemHeight = elHei;
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	public Detectable getDetectionRegion(int offsetX, int offsetY) {
		return new ClickRegionRectangle(getX() + offsetX, getY() + offsetY, canvas.getCanvasZoomWidth(), canvas.getCanvasZoomHeight(), getCode(), getDrawPriority());
	}
	
	public Canvas getCanvas() {
		return canvas;
	}

	public int getIdentity() {
		return hashCode();
	}

	public int getCode() {
		return code;
	}

	@Override
	public int getMinimumX() {
		return getX();
	}

	@Override
	public int getMaximumX() {
		return getX() + getWidth();
	}

	@Override
	public int getMinimumY() {
		return getY();
	}

	@Override
	public int getMaximumY() {
		return getY() + getHeight();
	}

	public int getWidth() {
		return elemWidth;
	}
	
	public int getHeight() {
		return elemHeight;
	}

}
