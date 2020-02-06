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
		double angle = 0;
		int offsetX = 0;
		int offsetY = 0;
		double rise = y2 - y1;
		double run = x2 - x1;
		
		angle = Math.atan(-1.0 / (rise / run));
		offsetX = (int)(Math.cos(angle) * thick/2.0);
		offsetY = (int)(Math.sin(angle) * thick/2.0);
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
