package visual.panel.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class DrawnLine extends Element{

//---  Instance Variables   -------------------------------------------------------------------
	
	private Polygon p;
	private Color color;
	private int x2;
	private int y2;
	private int thick;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DrawnLine(int x1, int y1, int inx2, int iny2, int inThick, int prior, Color in) {
		color = in;
		setDrawPriority(prior);
		setX(x1);
		setY(y1);
		x2 = inx2;
		y2 = iny2;
		thick = inThick;
		generatePolygon();
	}

//---  Operations   ---------------------------------------------------------------------------

	@Override
	public void drawToScreen(Graphics g) {
		Color save = g.getColor();
		g.setColor(color);
		g.fillPolygon(p);
		g.drawPolygon(p);
		g.setColor(save);
	}
	
	private void generatePolygon() {
		double angle = 0;
		int offsetX = 0;
		int offsetY = 0;
		int x1 = getX();
		int y1 = getY();
		double rise = y2 - y1;
		double run = x2 - x1;
		
		angle = Math.atan(-1.0 / (rise / run));
		offsetX = (int)(Math.cos(angle) * thick/2.0);
		offsetY = (int)(Math.sin(angle) * thick/2.0);
		p = new Polygon(new int[] {x1 - offsetX, x1 + offsetX, x2 + offsetX, x2 - offsetX}, new int[] {y1 - offsetY, y1 + offsetY, y2 + offsetY, y2 - offsetY}, 4);
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	@Override
	public void setX(int inX) {
		x2 = x2 + (inX - getX());
		super.setX(inX);
		generatePolygon();
	}
	
	@Override
	public void setY(int inY) {
		y2 = y2 + (inY - getY());
		super.setY(inY);
		generatePolygon();
	}
	
}
