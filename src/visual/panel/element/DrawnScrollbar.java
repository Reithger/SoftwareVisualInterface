package visual.panel.element;

import java.awt.Graphics;

import input.mouse.ClickRegionRectangle;
import input.mouse.Detectable;
import visual.panel.group.OffsetManager;

public class DrawnScrollbar extends Element implements Clickable{
	
//---  Constant Values   ----------------------------------------------------------------------
	
	public static final int CODE_SCROLL_BAR_X = -58;
	public static final int CODE_SCROLL_BAR_Y = -57;
	public static final double BAR_SIZE_PROPORTION = 1.0 / 30;
	private static final int UPDATE_TIMER = 15;

//---  Instance Variables   -------------------------------------------------------------------
	
	private String groupName;

	private int code;
	
	private boolean isVert;
	
	private OffsetManager offsetManager;
	
	private int counterUpdate;
	
	//-- Scrollbar Position  ----------------------------------
	
	private int scrollbarWid;
	
	private int scrollbarHei;
	
	ClickRegionRectangle detection;
	
	//-- Element Actual Bounds  -------------------------------
	
	private int minBound;
	
	private int maxBound;

	//-- Window  ----------------------------------------------
	
	private int windowOrigin;
	
	private int windowSize;
	
	private int dynOff;

//---  Constructors   -------------------------------------------------------------------------
	
	public DrawnScrollbar(int x, int y, int wid, int hei, int winOrigin, int winAcr, OffsetManager offset, int codeIn, int prior, String groupNom, boolean vert) {
		setX(x);
		setY(y);
		setDrawPriority(prior);
		scrollbarWid = wid;
		scrollbarHei = hei;
		windowOrigin = winOrigin;
		windowSize = winAcr;
		groupName = groupNom;
		code = codeIn;
		isVert = vert;
		offsetManager = offset;
		update();
	}
	
//---  Operations   ---------------------------------------------------------------------------

	@Override
	public void drawToScreen(Graphics g, int offsetX, int offsetY) {
		counterUpdate++;
		
		dynOff = isVert ? offsetY : offsetX;
		
		if(counterUpdate > UPDATE_TIMER) {
			update();
			counterUpdate = 0;
		}

		int barButtonSize = getBarButtonSize();
		int barTopSize = getBarTopSize();
		
		g.drawRect(getX() + offsetX, getY() + offsetY, scrollbarWid, scrollbarHei);
		
		int x = (isVert ? getX() : (getX() + barTopSize)) + offsetX;
		int y = (isVert ? (getY() + barTopSize) : getY()) + offsetY;
		int wid = isVert ? scrollbarWid : barButtonSize;
		int hei = isVert ? barButtonSize : scrollbarHei;
		
		g.fillRect(x, y, wid, hei);
		detection.setRegion(x, y, wid, hei);
	}
	
	@Override
	public boolean focusKeyEvent(char in) {
		return true;
	}

	@Override
	public boolean focusDragEvent(int x, int y, int mouseType) {
		int act = isVert ? (y - dynOff) : (x - dynOff);
		int barSize = getBarButtonSize();
		act -= (isVert ? getY() : getX()) + barSize / 2;
		double prop = 1.0 - (double)act / (isVert ? (scrollbarHei - barSize) : (scrollbarWid - barSize));
		
		int low = windowOrigin - minBound;
		int high = maxBound - (windowOrigin + windowSize);
		
		int newOffset = (int)((low + high) * prop - high);
		
		int windowMax = windowOrigin + windowSize;
		int min = minBound > windowOrigin ? windowOrigin : minBound;
		int max = maxBound < windowMax ? windowMax : maxBound;
		
		newOffset = newOffset > windowOrigin - min ? windowOrigin - min : newOffset < windowMax - max ? windowMax - max : newOffset;
		if(isVert) {
			offsetManager.setOffsetY(groupName, newOffset);
		}
		else {
			offsetManager.setOffsetX(groupName, newOffset);
		}
		
		return false;
	}
	
	public void focus() {
		
	}
	
	public void unfocus() {
		
	}
	
	private void update() {
		minBound = isVert ? offsetManager.getMinimumScreenY(groupName) : offsetManager.getMinimumScreenX(groupName);
		maxBound = isVert ? offsetManager.getMaximumScreenY(groupName) : offsetManager.getMaximumScreenX(groupName);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------

	@Override
	public Detectable getDetectionRegion(int offsetX, int offsetY) {
		int barButtonSize = getBarButtonSize();
		int barTopSize = getBarTopSize();
		
		detection = new ClickRegionRectangle((isVert ? getX() : (getX() + barTopSize)) + offsetX, (isVert ? (getY() + barTopSize) : getY()) + offsetY, isVert ? scrollbarWid : barButtonSize, isVert ? barButtonSize : scrollbarHei, code, getDrawPriority());
		return detection;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public int getIdentity() {
		return hashCode();
	}

	@Override
	public int getMinimumX() {
		return getX();
	}

	@Override
	public int getMaximumX() {
		return getX() + scrollbarWid;
	}

	@Override
	public int getMinimumY() {
		return getY();
	}

	@Override
	public int getMaximumY() {
		return getY() + scrollbarHei;
	}
	
//---  Helper Methods   -----------------------------------------------------------------------
	
	private int getBarButtonSize() {
		int butSize = isVert ? scrollbarWid : scrollbarHei;
		int offset = isVert ? offsetManager.getOffsetY(groupName) : offsetManager.getOffsetX(groupName);
		int barSize = isVert ? scrollbarHei : scrollbarWid;
		
		int subSpace = Math.abs(windowOrigin - (minBound > windowOrigin ? windowOrigin : minBound) - offset);
		int max = windowOrigin + windowSize;
		int overSpace = (maxBound < max ? max : maxBound) - (max) + offset;
		
		
		
		
		boolean minSize = (subSpace + overSpace + butSize) >= barSize;
		return minSize ? butSize : barSize - subSpace - overSpace;
	}
	
	private int getBarTopSize() {
		int butSize = isVert ? scrollbarWid : scrollbarHei;
		int offset = isVert ? offsetManager.getOffsetY(groupName) : offsetManager.getOffsetX(groupName);
		int barSize = isVert ? scrollbarHei : scrollbarWid;
		
		int subSpace = Math.abs(windowOrigin - (minBound > windowOrigin ? windowOrigin : minBound) - offset);
		int max = windowOrigin + windowSize;
		int overSpace = (maxBound < max ? max : maxBound) - (max) + offset;
		
		
		
		boolean minSize = (subSpace + overSpace + butSize) >= barSize;
		int barButtonSize = minSize ? butSize : barSize - subSpace - overSpace;
		return minSize ? (int)((barSize - barButtonSize) * (double)(subSpace / (double)(subSpace + overSpace))) : subSpace;
	}

//---  Setter Methods   -----------------------------------------------------------------------

}
