package visual.panel.element;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Graphics;

public class DrawnImage extends Element{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private int xLocation;
	/** */
	private int yLocation;
	/** */
	private int scaleFactor;
	/** */
	private Image image;
	
	private boolean center;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param img
	 * @param scale
	 */
	
	public DrawnImage(int x, int y, int prior, boolean inCenter, Image img, int scale) {
		xLocation = x;
		yLocation = y;
		image = img;
		scaleFactor = scale;
		center = inCenter;
		setDrawPriority(prior);
	}	

	/**
	 * 
	 * @param x
	 * @param y
	 * @param img
	 */
	
	public DrawnImage(int x, int y, int prior, boolean inCenter, Image img) {
		xLocation = x;
		yLocation = y;
		image = img;
		center = inCenter;
		scaleFactor = 1;
		setDrawPriority(prior);
	}	

//---  Operations   ---------------------------------------------------------------------------
	
	public void drawToScreen(Graphics g) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		image = image.getScaledInstance(image.getWidth(null) * scaleFactor, image.getHeight(null) * scaleFactor, Image.SCALE_DEFAULT);
		while(!tk.prepareImage(image, -1, -1, null)){	}
		g.drawImage(image, xLocation - (center ? image.getWidth(null)/2 : 0), yLocation - (center ? image.getHeight(null)/2 : 0), null);
	}
	
}
