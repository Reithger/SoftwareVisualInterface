package visual.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class CanvasPanel extends Panel{
	
//---  Constant Values   ----------------------------------------------------------------------
	
	/** int constant value describing the size of the sub-images in pixels*/
	private int SUB_GRID_SIZE = 32;

//---  Instance Variables   -------------------------------------------------------------------
	
	/** 2D Color array describing the colors that compose the canvas*/
	private Color[][] canvas;
	/** 2D BufferedImage array containing the sub-images of size SUB_GRID_SIZE x SUB_GRID_SIZE that compose the total image*/
	private BufferedImage[][] display;
	/** 2D boolean array describing which sub-images need to be redrawn based off input that has changed the 2D Color array*/
	private boolean[][] update;
	/** int value describing how large to draw each entry in the canvas (at zoom = 1, each color is 1 pixel). Does not affect the size of the canvas, just its representation*/
	private int zoom;
	/** Color object describing the current Color that any selected point in the canvas Color[][] array will be set as (the 'pen' color)*/
	private Color draw;
	/** */
	private int width;
	/** */
	private int height;
	/** */
	private int xBlock;
	/** */
	private int yBlock;
	/** */
	private BufferedImage save;
	
//---  Constructors   -------------------------------------------------------------------------
	
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
	
	//--  Support  ------------------------------------
	
	private void initialize() {
		for(int i = 0; i < canvas.length; i++) {
			for(int j = 0; j < canvas[i].length; j++) {
				canvas[i][j] = new Color(0, 0, 0);
			}
		}
	}
	
//---  Operations   ---------------------------------------------------------------------------

	public void commandUnder(Graphics g) {
		//Overwrite method to allow custom underlay
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
	
	public void commandOver(Graphics g) {
		//Overwrite method to allow custom overlay
	}

	public void updateSize(int newWidth, int newHeight) {
		//TODO: Retain as much information as possible
		formatSubImages();
	}

	public void setColor(Color drawSet) {
		draw = drawSet;
	}
	
	//-- Reactions  -----------------------------------
	
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
	
//---  Mechanics   ----------------------------------------------------------------------------

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
				if(canvas[x * SUB_GRID_SIZE + i][y * SUB_GRID_SIZE + j] == null) {
					out.setRGB(i, j, Color.white.getRGB());
				}
				else {
					out.setRGB(i, j, canvas[x * SUB_GRID_SIZE + i][y * SUB_GRID_SIZE + j].getRGB());
				}
			}
		}
		return out;
	}

}
