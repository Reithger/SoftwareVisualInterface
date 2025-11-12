package visual.panel.element;

import java.awt.Graphics;

/**
 * 
 * The Point class represents an arbitrary (x, y) coordinate on the screen and does not have draw
 * instructions for itself. It is meant to be used in conjunction with an arbitrary Shape but be
 * able to have each individual Point be moved independently of the overall Shape.
 * 
 * This is a step towards having some perspective drawing abilities that require things like Triangles
 * and arbitrary polygons.
 * 
 * 
 */

public class Point extends Element{

	public Point(int x, int y, int prior) {
		setX(x);
		setY(y);
		setDrawPriority(prior);
	}
	
	@Override
	public void drawToScreen(Graphics g, int offsetX, int offsetY) {
		return;
	}

	@Override
	public int getMinimumX() {
		return getX();
	}

	@Override
	public int getMaximumX() {
		return getX();
	}

	@Override
	public int getMinimumY() {
		return getY();
	}

	@Override
	public int getMaximumY() {
		return getY();
	}

}
