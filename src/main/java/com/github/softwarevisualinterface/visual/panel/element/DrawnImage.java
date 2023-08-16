package com.github.softwarevisualinterface.visual.panel.element;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Graphics;

public class DrawnImage extends Element{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private Image image;
	
	private boolean center;
	
	private int width;
	
	private int height;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DrawnImage(int x, int y, int prior, boolean inCenter, Image img, int inWidth, int inHeight, boolean proportion) {
		setX(x);
		setY(y);
		image = img;
		center = inCenter;
		width = inWidth;
		height = inHeight;
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
		setDrawPriority(prior);
		width = img.getWidth(null);
		height = img.getHeight(null);
	}	

//---  Operations   ---------------------------------------------------------------------------
	
	public void drawToScreen(Graphics g, int offsetX, int offsetY) {
		g.drawImage(image, getX() - (center ? width / 2 : 0) + offsetX, getY() - (center ? height / 2 : 0) + offsetY, width, height, null);
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
