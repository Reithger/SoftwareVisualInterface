package visual.panel.element.drawn;

import java.awt.Image;

import visual.panel.element.Element;

import java.awt.Graphics;

public class DrawnImage extends Element{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private Image image;
	
	private boolean center;
	
	private int width;
	
	private int height;
	
	private boolean maintainProportion;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DrawnImage(int x, int y, int prior, boolean inCenter, Image img, int inWidth, int inHeight, boolean proportion) {
		setX(x);
		setY(y);
		image = img;
		center = inCenter;
		width = inWidth;
		height = inHeight;
		setDrawPriority(prior);
		maintainProportion = proportion;
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
		setDrawPriority(prior);
		width = img.getWidth(null);
		height = img.getHeight(null);
		maintainProportion = true;
	}	

//---  Operations   ---------------------------------------------------------------------------
	
	public void drawToScreen(Graphics g, int offsetX, int offsetY) {
		int useWid = width;
		int useHei = height;
		if(maintainProportion) {
			int imgWid = image.getWidth(null);
			int imgHei = image.getHeight(null);
			if(useWid != imgWid || useHei != imgHei) {
				double widStretch = (double) useWid / imgWid;
				double heiStretch = (double) useHei / imgHei;
				if(widStretch < heiStretch) {
					useWid = (int) (widStretch * imgWid);
					useHei = (int) (widStretch * imgHei);
				}
				else {
					useWid = (int) (heiStretch * imgWid);
					useHei = (int) (heiStretch * imgHei);
				}
			}
		}
		g.drawImage(image, getX() - (center ? useWid / 2 : 0) + offsetX, getY() - (center ? useHei / 2 : 0) + offsetY, useWid, useHei, null);
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	@Override
	public int getMinimumX() {
		return center ? getX() - (center ? image.getWidth(null) / 2 : 0) : getX();
	}

	@Override
	public int getMaximumX() {
		return image.getWidth(null) + getMinimumX();
	}

	@Override
	public int getMinimumY() {
		return center ? getY() - (center ? image.getHeight(null) / 2 : 0) : getY();
	}

	@Override
	public int getMaximumY() {
		return image.getHeight(null) + getMinimumY();
	}
}
