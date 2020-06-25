package visual.panel.element;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Graphics;

public class DrawnImage extends Element{

//---  Instance Variables   -------------------------------------------------------------------
	
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
		setX(x);
		setY(y);
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
		setX(x);
		setY(y);
		image = img;
		center = inCenter;
		scaleFactor = 1;
		setDrawPriority(prior);
	}	

//---  Operations   ---------------------------------------------------------------------------
	
	public void drawToScreen(Graphics g) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image drawImage = image.getScaledInstance(image.getWidth(null) * scaleFactor, image.getHeight(null) * scaleFactor, Image.SCALE_DEFAULT);
		while(!tk.prepareImage(drawImage, -1, -1, null)){	}
		g.drawImage(drawImage, getX() - (center ? drawImage.getWidth(null)/2 : 0), getY() - (center ? drawImage.getHeight(null)/2 : 0), null);
	}
	
}
