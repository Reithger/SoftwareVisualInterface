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
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param img
	 * @param scale
	 */
	
	public DrawnImage(int x, int y, int prior, Image img, int scale) {
		xLocation = x;
		yLocation = y;
		image = img;
		scaleFactor = scale;
		setDrawPriority(prior);
	}	

	/**
	 * 
	 * @param x
	 * @param y
	 * @param img
	 */
	
	public DrawnImage(int x, int y, Image img) {
		xLocation = x;
		yLocation = y;
		image = img;
		scaleFactor = 1;
	}	

//---  Operations   ---------------------------------------------------------------------------
	
	public void drawToScreen(Graphics g) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		image = image.getScaledInstance(image.getWidth(null) * scaleFactor, image.getHeight(null) * scaleFactor, Image.SCALE_DEFAULT);
		while(!tk.prepareImage(image, -1, -1, null)){	}
		g.drawImage(image, xLocation - image.getWidth(null)/2, yLocation - image.getHeight(null)/2, null);
	}
	
}
