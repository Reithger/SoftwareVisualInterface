package visual.panel.element;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import input.ClickRegionRectangle;
import input.Detectable;

public class DrawnImageButton extends Element implements Clickable{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private int xLocation;
	/** */
	private int yLocation;
	/** */
	private int scaleFactor;
	/** */
	private Image image;
	/** */
	private int code;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param img
	 * @param key
	 * @param scale
	 */
	
	public DrawnImageButton(int x, int y, int prior, Image img, int key, int scale) {
		xLocation = x;
		yLocation = y;
		scaleFactor = scale;
		image = img;
		code = key;
		setDrawPriority(prior);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param img
	 * @param key
	 */
	
	public DrawnImageButton(int x, int y, int prior, Image img, int key) {
		xLocation = x;
		yLocation = y;
		scaleFactor = 1;
		image = img;
		code = key;
		setDrawPriority(prior);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	@Override
	public Detectable getDetectionRegion() {
		int wid = image.getWidth(null) * scaleFactor;
		int hei = image.getHeight(null) * scaleFactor;
		return new ClickRegionRectangle(xLocation - wid / 2, yLocation - hei / 2, xLocation + wid / 2, yLocation + hei / 2, code);
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void drawToScreen(Graphics g) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		image = image.getScaledInstance(image.getWidth(null) * scaleFactor, image.getHeight(null) * scaleFactor, Image.SCALE_DEFAULT);
		while(!tk.prepareImage(image, -1, -1, null)){	}
		g.drawImage(image, xLocation - image.getWidth(null)/2, yLocation - image.getHeight(null)/2, null);
	}

	
	@Override
	public boolean focusEvent(char in) {
		return true;
	}

}
