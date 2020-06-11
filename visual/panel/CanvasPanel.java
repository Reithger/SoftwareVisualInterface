package visual.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class CanvasPanel extends Panel{
	
	private int SUB_GRID_SIZE = 32;

	private Color[][] canvas;
	private BufferedImage[][] display;
	private boolean[][] update;
	private int zoom;
	private Color draw;
	private int width;
	private int height;
	private int xBlock;
	private int yBlock;
	private BufferedImage save;
	
	public CanvasPanel(int x, int y, int inWidth, int inHeight) {
		super(x, y, inWidth, inHeight);
		width = inWidth;
		height = inHeight;
		zoom = 1;
		draw = new Color(0, 0, 0);
		canvas = new Color[width][height];
		formatSubImages();
		initialize();
	}
	
	public CanvasPanel(int x, int y, int inWidth, int inHeight, int defZoom) {
		super(x, y, inWidth, inHeight);
		width = inWidth;
		height = inHeight;
		zoom = defZoom;
		draw = new Color(0, 0, 0);
		canvas = new Color[inWidth][inHeight];
		formatSubImages();
		initialize();
	}
	
	public void updateSize(int newWidth, int newHeight) {
		//TODO: Retain as much information as possible
		formatSubImages();
	}
	
	private void formatSubImages() {
		xBlock = width % SUB_GRID_SIZE == 0 ? width / SUB_GRID_SIZE : (width / SUB_GRID_SIZE + 1);
		yBlock = height % SUB_GRID_SIZE == 0 ? height / SUB_GRID_SIZE : (height / SUB_GRID_SIZE + 1);
		update = new boolean[xBlock][yBlock];
		display = new BufferedImage[xBlock][yBlock];
		buildSubImages();
	}
	
	private void buildSubImages() {
		for(int i = 0; i < xBlock; i++) {
			for(int j = 0; j < yBlock; j++) {
				display[i][j] = buildSubImage(i, j);
			}
		}
	}
	
	private BufferedImage buildSubImage(int x, int y) {
		int wid = (x == xBlock - 1 && width % SUB_GRID_SIZE != 0) ? width % SUB_GRID_SIZE : SUB_GRID_SIZE;
		int hei = (y == yBlock - 1 && height % SUB_GRID_SIZE != 0) ? height % SUB_GRID_SIZE : SUB_GRID_SIZE;
		BufferedImage out = new BufferedImage(wid, hei, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < wid; i++) {
			for(int j = 0; j < hei; j++) {
				out.setRGB(i, j, canvas[x * SUB_GRID_SIZE + i][y * SUB_GRID_SIZE + j].getRGB());
			}
		}
		return out;
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
			update[x / zoom / SUB_GRID_SIZE][y / zoom / SUB_GRID_SIZE] = true;
		}
	}
	
	@Override
	public void dragEvent(int x, int y) {
		if(x > 0 && x <= width && y > 0 && y <= height) {
			canvas[x / zoom][y / zoom] = new Color(draw.getRGB());
			update[x / zoom / SUB_GRID_SIZE][y / zoom / SUB_GRID_SIZE] = true;
		}
	}

	@Override
	public void keyEvent(char event) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Color save = g.getColor();
		commandUnder(g);
		for(int i = 0; i < display.length; i++) {
			for(int j = 0; j < display[i].length; j++) {
				g.drawImage(display[i][j], i * SUB_GRID_SIZE, j * SUB_GRID_SIZE, null);
			}
		}
		commandOver(g);
		g.setColor(save);
	}
	
	@Override
	public void update(Graphics gIn) {

	}
		
	public void setColor(Color drawSet) {
		draw = drawSet;
	}
	
}
