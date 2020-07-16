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
	private int offsetX;
	private int offsetY;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DrawnLine(int x1, int y1, int inx2, int iny2, int inThick, int prior, Color in) {
		color = in;
		setDrawPriority(prior);
		setX(x1);
		setY(y1);
		x2 = inx2;
		y2 = iny2;
		thick = inThick;
		offsetX = 0;
		offsetY = 0;
		generatePolygon();
	}

//---  Operations   ---------------------------------------------------------------------------

	@Override
	public void drawToScreen(Graphics g, int offsetXIn, int offsetYIn) {
		Color save = g.getColor();
		if(offsetX != offsetXIn || offsetY != offsetYIn) {
			offsetX = offsetXIn;
			offsetY = offsetYIn;
			generatePolygon();
		}
		g.setColor(color);
		g.fillPolygon(p);
		g.drawPolygon(p);
		g.setColor(save);
	}
	
	private void generatePolygon() {
		double angle = 0;
		int adjustX = 0;
		int adjustY = 0;
		int x1 = getX();
		int y1 = getY();
		double rise = y2 - y1;
		double run = x2 - x1;
		
		angle = Math.atan(-1.0 / (rise / run));
		adjustX = (int)(Math.cos(angle) * thick/2.0);
		adjustY = (int)(Math.sin(angle) * thick/2.0);
		System.out.println(offsetX + " " + offsetY);
		p = new Polygon(new int[] {x1 - adjustX + offsetX, x1 + adjustX + offsetX, x2 + adjustX + offsetX, x2 - adjustX + offsetX}, new int[] {y1 - adjustY + offsetY, y1 + adjustY + offsetY, y2 + adjustY + offsetY, y2 - adjustY + offsetY}, 4);
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

	@Override
	public int getMinimumX() {
		return getX() < x2 ? getX() - thick/2 : x2 - thick/2;
	}

	@Override
	public int getMaximumX() {
		return getX() > x2 ? getX() + thick/2 : x2 + thick/2;
	}

	@Override
	public int getMinimumY() {
		return getY() < y2 ? getY() - thick/2 : y2 - thick/2;
	}

	@Override
	public int getMaximumY() {
		return getY() > y2 ? getY() + thick/2 : y2 + thick/2;
	}
}
