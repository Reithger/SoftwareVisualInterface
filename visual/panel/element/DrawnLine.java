package visual.panel.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class DrawnLine extends Element{

	private Polygon p;
	private Color color;
	
	public DrawnLine(int x1, int y1, int x2, int y2, int thick, int prior, Color in) {
		color = in;
		setDrawPriority(prior);
		double angle = Math.atan(-1 / ((double)(y2 - y1) / (double)(x2 - x1)));
		int offsetX = (int)(Math.sin(angle) * thick/2.0);
		int offsetY = (int)(Math.cos(angle) * thick/2.0);
		p = new Polygon(new int[] {x1 - offsetX, x1 + offsetX, x2 + offsetX, x2 - offsetX}, new int[] {y1 - offsetY, y1 + offsetY, y2 + offsetY, y2 - offsetY}, 4);
		
	}
	
	@Override
	public void drawToScreen(Graphics g) {
		Color save = g.getColor();
		g.setColor(color);
		g.fillPolygon(p);
		g.drawPolygon(p);
		g.setColor(save);
	}
	
}
