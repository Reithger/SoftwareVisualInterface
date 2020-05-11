package visual.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class CanvasPanel extends Panel{

	private Color[][] canvas;
	private boolean[][] update;
	private int zoom;
	private Color draw;
	private int width;
	private int height;
	private BufferedImage save;
	
	public CanvasPanel(int x, int y, int inWidth, int inHeight) {
		super(x, y, inWidth, inHeight, true);
		width = inWidth;
		height = inHeight;
		zoom = 1;
		draw = new Color(0, 0, 0);
		canvas = new Color[inWidth][inHeight];
		update = new boolean[inWidth][inHeight];
		initialize();
	}
	
	public CanvasPanel(int x, int y, int inWidth, int inHeight, int defZoom) {
		super(x, y, inWidth, inHeight, true);
		width = inWidth;
		height = inHeight;
		zoom = defZoom;
		draw = new Color(0, 0, 0);
		canvas = new Color[inWidth][inHeight];
		update = new boolean[inWidth][inHeight];
		initialize();
	}
	
	private void initialize() {
		for(int i = 0; i < canvas.length; i++) {
			for(int j = 0; j < canvas[i].length; j++) {
				canvas[i][j] = new Color(0, 0, 0);
			}
		}
	}
	
	public void commandUnder(Graphics g) {
		//Overwrite method to allow custom underlay
	}
	
	public void commandOver(Graphics g) {
		//Overwrite method to allow custom overlay
	}

	@Override
	public void clickEvent(int event, int x, int y) {
		if(x > 0 && x <= width && y > 0 && y <= height) {
			canvas[x / zoom][y / zoom] = new Color(draw.getRGB());
			update[x / zoom][y / zoom] = true;
		}
	}
	
	@Override
	public void dragEvent(int x, int y) {
		if(x > 0 && x <= width && y > 0 && y <= height) {
			canvas[x / zoom][y / zoom] = new Color(draw.getRGB());
			update[x / zoom][y / zoom] = true;
		}
	}

	@Override
	public void keyEvent(char event) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
	}
	
	@Override
	public void update(Graphics gIn) {
		Graphics g = gIn.create();
		Color save = g.getColor();
		commandUnder(g);
		for(int i = 0; i < canvas.length; i++) {
			for(int j = 0; j < canvas[i].length; j++) {
				if(update[i][j]) {
					update[i][j] = false;
					g.setColor(canvas[i][j]);
					g.fillRect(i * zoom, j * zoom, zoom, zoom);
				}
			}
		}
		commandOver(g);
		g.setColor(save);
	}
		
	public void setColor(Color drawSet) {
		draw = drawSet;
	}
	
}
