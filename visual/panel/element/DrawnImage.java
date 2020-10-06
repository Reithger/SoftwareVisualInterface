package visual.panel.element;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Graphics;

public class DrawnImage extends Element{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private double scaleFactor;
	/** */
	private Image image;
	
	private boolean center;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DrawnImage(int x, int y, int prior, boolean inCenter, Image img, int width, int height, boolean proportion) {
		setX(x);
		setY(y);
		Image drawImage = null;
		center = inCenter;
		scaleFactor = 1;
		if(proportion) {
			int small = width < height ? width : height;
			drawImage = img.getScaledInstance(small, small, Image.SCALE_DEFAULT);
		}
		else {
			drawImage = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		}
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		while(!tk.prepareImage(drawImage, -1, -1, null)){	}
		image = drawImage;
		setDrawPriority(prior);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param img
	 * @param scale
	 */
	
	public DrawnImage(int x, int y, int prior, boolean inCenter, Image img, double scale) {
		setX(x);
		setY(y);
		scaleFactor = scale;
		center = inCenter;
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image drawImage = img.getScaledInstance((int)(img.getWidth(null) * scaleFactor), (int)(img.getHeight(null) * scaleFactor), Image.SCALE_DEFAULT);
		while(!tk.prepareImage(drawImage, -1, -1, null)){	}
		image = drawImage;
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
	
	public void drawToScreen(Graphics g, int offsetX, int offsetY) {
		g.drawImage(image, getX() - (center ? image.getWidth(null)/2 : 0) + offsetX, getY() - (center ? image.getHeight(null)/2 : 0) + offsetY, null);
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	@Override
	public int getMinimumX() {
		return center ? getX() - (center ? image.getWidth(null) / 2 : 0) : getX();
	}

	@Override
	public int getMaximumX() {
		return image.getWidth(null) + (center ? getX() - (center ? image.getWidth(null) / 2 : 0) : getX());
	}

	@Override
	public int getMinimumY() {
		return center ? getY() - (center ? image.getHeight(null) / 2 : 0) : getY();
	}

	@Override
	public int getMaximumY() {
		return image.getHeight(null) + (center ? getY() - (center ? image.getHeight(null) / 2 : 0) : getY());
	}
}
