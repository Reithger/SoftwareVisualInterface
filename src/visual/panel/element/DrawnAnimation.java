package visual.panel.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

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
		scaleImages();
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
		scaleImages();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void drawToScreen(Graphics g, int offsetX, int offsetY) {
		Color save = g.getColor();
		int imgInd = getCurrentImage();
		Image image = images[imgInd];
		g.drawImage(image, getX() - (centered ? image.getWidth(null)/2 : 0) + offsetX, getY() - (centered ? image.getHeight(null)/2 : 0) + offsetY, null);
		g.setColor(save);
		frameNumber = (frameNumber + 1) % period;
	}

	private void updatePeriod() {
		period = 0;
		for(int i : waitPeriod) {
			period += i;
		}
	}
	
	private void scaleImages() {
		for(int i = 0; i < images.length; i++) {
			Toolkit tk = Toolkit.getDefaultToolkit();
			Image img = images[i];
			Image drawImage = img.getScaledInstance((int)(img.getWidth(null) * scale), (int)(img.getHeight(null) * scale), Image.SCALE_DEFAULT);
			while(!tk.prepareImage(drawImage, -1, -1, null)){	}
			images[i] = drawImage;
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
		int low = 0;
		for(Image i : images) {
			int wid = i.getWidth(null);
			if(getX() - (centered ? wid / 2 : wid) < low) {
				low = getX() - (centered ? wid / 2 : wid);
			}
		}
		return low;
	}

	@Override
	public int getMaximumX() {
		int max = 0;
		for(Image i : images) {
			int wid = i.getWidth(null);
			if(getX() + (centered ? wid / 2 : wid) > max) {
				max = getX() + (centered ? wid / 2 : wid);
			}
		}
		return max;
	}

	@Override
	public int getMinimumY() {
		int low = 0;
		for(Image i : images) {
			int hei = i.getHeight(null);
			if(getY() - (centered ? hei / 2 : hei) < low) {
				low = getY() - (centered ? hei / 2 : hei);
			}
		}
		return low;
	}

	@Override
	public int getMaximumY() {
		int max = 0;
		for(Image i : images) {
			int hei = i.getHeight(null);
			if(getY() + (centered ? hei / 2 : hei) > max) {
				max = getY() + (centered ? hei / 2 : hei);
			}
		}
		return max;
	}

}
