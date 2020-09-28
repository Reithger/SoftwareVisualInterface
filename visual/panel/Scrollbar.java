package visual.panel;

import java.awt.Color;
import java.awt.Graphics;

import input.ClickRegionRectangle;

public class Scrollbar {
	
//---  Constant Values   ----------------------------------------------------------------------
	
	public static final int CODE_SCROLL_BAR_X = -58;
	public static final int CODE_SCROLL_BAR_Y = -57;
	public static final double BAR_SIZE_PROPORTION = 1.0 / 30;
	private static final int MINIMUM_BAR_SIZE = 8;
	private static final int MAXIMUM_BAR_SIZE = 24;

//---  Instance Variables   -------------------------------------------------------------------
	
	private Panel panel;
	
	//-- Scrollbar  -------------------------------------------
	
	private boolean scrollBarHoriz;
	
	private boolean scrollBarVert;
	
	private boolean vertBarSelect;
	
	private boolean horizBarSelect;
	
	private boolean liveVert;
	
	private int startYVertBar;
	
	private int startXHorizBar;
	
	private int yVertChange;

	private int xHorizChange;

	private boolean updateRange;
	
	private int minX;
	
	private int maxX;
	
	private int minY;
	
	private int maxY;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Scrollbar(Panel in) {
		scrollBarHoriz = true;
		scrollBarVert = true;
		panel = in;
		updateRange = true;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void update(Graphics g) {
		Color save = g.getColor();
		if(updateRange) {
			minY = panel.getMinimumScreenY();
			maxY = panel.getMaximumScreenY();
			minX = panel.getMinimumScreenX();
			maxX = panel.getMaximumScreenX();
		}
		if(scrollBarHoriz) {
			updateHorizontal(g);
		}
		if(scrollBarVert) {
			updateVertical(g);
		}
		g.setColor(save);
	}
	
	private void updateVertical(Graphics g) {
		int offY = panel.getOffsetY();
		int hei = panel.getHeight(); // 
		liveVert = false;
		if((offY > minY || offY + hei < maxY) && scrollBarVert) {
			liveVert = true;
			panel.removeClickRegion(CODE_SCROLL_BAR_Y);
			
			int wid = panel.getWidth();
			int butWid = (int)((double)wid * BAR_SIZE_PROPORTION);
			butWid = butWid < MINIMUM_BAR_SIZE ? MINIMUM_BAR_SIZE : butWid;
			butWid = butWid > MAXIMUM_BAR_SIZE ? MAXIMUM_BAR_SIZE : butWid;
			
			int subSpaceY = Math.abs(minY + offY);
			int overSpaceY = maxY - hei + offY;
			boolean minSize = (subSpaceY + overSpaceY + butWid) >= hei;
			int barButtonSizeY = minSize ? butWid : hei - subSpaceY - overSpaceY;
			int barTopSizeY = minSize ? (int)((hei - barButtonSizeY) * (double)(subSpaceY / (double)(subSpaceY + overSpaceY))) : subSpaceY;
			yVertChange = minSize ? (int)((subSpaceY + overSpaceY) / (double)(hei - barButtonSizeY)) : 1;
			g.drawRect(wid - butWid - 1, 0, butWid, hei);
			g.fillRect(wid - butWid - 1, barTopSizeY, butWid, barButtonSizeY);
			panel.addClickRegion(new ClickRegionRectangle(wid - butWid - 1, barTopSizeY, butWid, barButtonSizeY, CODE_SCROLL_BAR_Y));
		}
	}
	
	private void updateHorizontal(Graphics g) {
		int offX = panel.getOffsetX();
		int wid = panel.getWidth();
		if((offX > minX || offX + wid < maxX) && scrollBarHoriz) {
			panel.removeClickRegion(CODE_SCROLL_BAR_X);

			int hei = panel.getHeight();
			int butHei = (int)((double)hei * BAR_SIZE_PROPORTION);
			butHei = butHei < MINIMUM_BAR_SIZE ? MINIMUM_BAR_SIZE : butHei;
			butHei = butHei > MAXIMUM_BAR_SIZE ? MAXIMUM_BAR_SIZE : butHei;
			
			int subSpaceX = Math.abs(offX + minX);
			int overSpaceX = maxX - wid + offX;
			boolean minSize = (subSpaceX + overSpaceX + butHei) >= wid;
			int barButtonSizeX = minSize ? butHei : wid - subSpaceX - overSpaceX;
			int barTopSizeX = minSize ? (int)(wid * (double)(subSpaceX / (double)(subSpaceX + overSpaceX)) - barButtonSizeX / 2) : subSpaceX;
			xHorizChange = minSize ? (int)((subSpaceX + overSpaceX) / (double)(wid - barButtonSizeX)) : 1;
			
			int butWid = (int)((double)panel.getWidth() * BAR_SIZE_PROPORTION);
			butWid = butWid < MINIMUM_BAR_SIZE ? MINIMUM_BAR_SIZE : butWid;
			butWid = butWid > MAXIMUM_BAR_SIZE ? MAXIMUM_BAR_SIZE : butWid;
			butWid = liveVert ? butWid : 0;
			
			g.drawRect(0, hei - butHei - 1, wid - butWid - 1, butHei);
			double prop = (wid - (liveVert ? butWid : 0)) / (double)wid;
			int presentBarWid = (int)(prop * barButtonSizeX);
			g.fillRect((int)(prop * barTopSizeX), hei - butHei - 1, presentBarWid, butHei);
			panel.addClickRegion(new ClickRegionRectangle(subSpaceX, hei - butHei - 1, barButtonSizeX, butHei, CODE_SCROLL_BAR_X));
		}
	}
	
	public void processClickRelease() {
		vertBarSelect = false;
		horizBarSelect = false;
	}
	
	public boolean processClickPress(int event, int x, int y) {
		if(event == CODE_SCROLL_BAR_Y) {
			vertBarSelect = true;
			startYVertBar = y;
			return false;
		}
		if(event == CODE_SCROLL_BAR_X) {
			horizBarSelect = true;
			startXHorizBar = x;
			return false;
		}
		return true;
	}

	public void processDrag(int event, int x, int y) {
		if(vertBarSelect == true) {
			int difY = y - startYVertBar;
			int newOffset = panel.getOffsetY() - difY * yVertChange;
			newOffset = newOffset > 0 - minY ? 0 - minY : newOffset < panel.getHeight() - maxY ? panel.getHeight() - maxY : newOffset;
			panel.setOffsetY(newOffset);
			startYVertBar = y;
		}
		if(horizBarSelect == true) {
			int difX = x - startXHorizBar;
			int newOffset = panel.getOffsetX() - difX * xHorizChange;
			newOffset = newOffset > 0 - minX ? 0 - minX : newOffset < panel.getWidth() - maxX ? panel.getWidth() - maxX : newOffset;
			panel.setOffsetX(newOffset);
			startXHorizBar = x;
		}
	}
	
//---  Getter Methods   -----------------------------------------------------------------------

//---  Setter Methods   -----------------------------------------------------------------------

	public void designateUpdate() {
		updateRange = true;
	}
	
	public void setScrollBarHorizontal(boolean in) {
		scrollBarHoriz = in;
	}
	
	public void setScrollBarVertical(boolean in) {
		scrollBarVert = in;
	}
}
