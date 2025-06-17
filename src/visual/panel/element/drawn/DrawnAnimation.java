package visual.panel.element.drawn;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import visual.panel.element.Element;

public class DrawnAnimation extends Element{
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private int frameNumber;
	
	private int[] waitPeriod;
	
	private int period;
	
	private boolean centered;
	
	private Image[] images;
	
	private double scale;

//---  Constructors   -------------------------------------------------------------------------
	
	public DrawnAnimation(int x, int y, int prior, int wait, boolean center, double inScale, Image[] imgs) {
		setX(x);
		setY(y);
		setDrawPriority(prior);
		centered = center;
		waitPeriod = new int[imgs.length];
		for(int i = 0; i < waitPeriod.length; i++) {
			waitPeriod[i] = wait;
		}
		images = imgs;
		scale = inScale;
		updatePeriod();
	}
	
	public DrawnAnimation(int x, int y, int prior, int[] wait, boolean center, double inScale, Image[] imgs) {
		setX(x);
		setY(y);
		setDrawPriority(prior);
		centered = center;
		waitPeriod = wait;
		if(imgs.length < waitPeriod.length) {
			int[] fix = new int[imgs.length];
			for(int i = 0; i < fix.length; i++) {
				fix[i] = waitPeriod[i];
			}
			waitPeriod = fix;
		}
		images = imgs;
		scale = inScale;
		updatePeriod();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void drawToScreen(Graphics g, int offsetX, int offsetY) {
		Color save = g.getColor();
		int imgInd = getCurrentImage();
		Image image = images[imgInd];
		int actWid = (int)(image.getWidth(null) * scale);
		int actHei = (int)(image.getHeight(null) * scale);
		g.drawImage(image, getX() - (centered ? actWid / 2 : 0) + offsetX, getY() - (centered ? actHei / 2 : 0) + offsetY, actWid, actHei, null);
		g.setColor(save);
		frameNumber = (frameNumber + 1) % period;
	}

	private void updatePeriod() {
		period = 0;
		for(int i : waitPeriod) {
			period += i;
		}
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	private int getCurrentImage() {
		int count = 0;
		for(int i = 0; i < waitPeriod.length; i++) {
			count += waitPeriod[i];
			if(frameNumber < count) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public int getMinimumX() {
		Integer low = null;
		for(Image i : images) {
			int wid = i.getWidth(null);
			if(low == null || getX() - (centered ? wid / 2 : wid) < low) {
				low = getX() - (centered ? wid / 2 : wid);
			}
		}
		return low;
	}

	@Override
	public int getMaximumX() {
		Integer max = null;
		for(Image i : images) {
			int wid = i.getWidth(null);
			if(max == null || getX() + (centered ? wid / 2 : wid) > max) {
				max = getX() + (centered ? wid / 2 : wid);
			}
		}
		return max;
	}

	@Override
	public int getMinimumY() {
		Integer low = null;
		for(Image i : images) {
			int hei = i.getHeight(null);
			if(low == null || getY() - (centered ? hei / 2 : hei) < low) {
				low = getY() - (centered ? hei / 2 : hei);
			}
		}
		return low;
	}

	@Override
	public int getMaximumY() {
		Integer max = null;
		for(Image i : images) {
			int hei = i.getHeight(null);
			if(max == null || getY() + (centered ? hei / 2 : hei) > max) {
				max = getY() + (centered ? hei / 2 : hei);
			}
		}
		return max;
	}

}
