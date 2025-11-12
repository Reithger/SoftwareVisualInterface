package visual.panel.element.drawn;

import java.awt.Graphics;

import visual.panel.element.Element;

/**
 * 
 * The DrawnPolygon class represents a visual element composed of numerous (x, y) coordinate points
 * and draws a polygon using these points as vertices.
 * 
 * Could use a system where a Polygon is named the same as an Element group, Points are all added into
 * a particular labeled Group associated to the Polygon and that's how we make that association. At time
 * of drawing we pull the Points' coordinates from that group and pass them to here.
 * 
 * Needs a way to access that data for minimum/maximumX/Y coordinates, though, so some getter interface as
 * part of the constructor is necessary.
 * 
 * ElementPanel should excise the management of Elements to a subclass to clean it up a bit and let that sub-class
 * handle the interface implementation.
 * 
 */

public class DrawnPolygon extends Element{

	public DrawnPolygon() {
		
	}
	
	@Override
	public void drawToScreen(Graphics g, int offsetX, int offsetY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMinimumX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaximumX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMinimumY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaximumY() {
		// TODO Auto-generated method stub
		return 0;
	}

}
