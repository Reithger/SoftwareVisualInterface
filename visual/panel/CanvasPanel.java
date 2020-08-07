package visual.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class CanvasPanel extends Panel{
	
//---  Constant Values   ----------------------------------------------------------------------
	
	/** int constant value describing the size of the sub-images in pixels*/
	private int SUB_GRID_SIZE_MAXIMUM = 64;

//---  Instance Variables   -------------------------------------------------------------------
	
	/** 2D Color array describing the colors that compose the canvas*/
	private Color[][] canvas;
	/** 2D BufferedImage array containing the sub-images of size subGridSize x subGridSize that compose the total image*/
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
	
	private int subGridSize;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public CanvasPanel(int x, int y, int inWidth, int inHeight) {
		super(x, y, inWidth, inHeight);
		width = inWidth;
		height = inHeight;
		zoom = 5;
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
		for(int i = 0; i < canvas.length / zoom; i++) {
			for(int j = 0; j < canvas[i].length / zoom; j++) {
				canvas[i][j] =  new Color(i  % 255, j % 255, (i * j) % 255);
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
		
		for(int i = 0; i < update.length; i++) {
			for(int j = 0; j < update[i].length; j++) {
				if(update[i][j]) {
					display[i][j] = buildSubImage(i, j);
					update[i][j] = false;
				}
			}
		}
		
		for(int i = 0; i < display.length; i++) {
			for(int j = 0; j < display[i].length; j++) {
				g.drawImage(display[i][j], i * subGridSize, j * subGridSize, null);
			}
		}
		commandOver(g);
		g.setColor(save);
	}
	
	public void commandOver(Graphics g) {
		//Overwrite method to allow custom overlay
	}

	public void updateSize(int newWidth, int newHeight) {
		width = newWidth;
		height = newHeight;
		Color[][] out = new Color[newWidth][newHeight];
		for(int i = 0; i < newWidth; i++) {
			for(int j = 0; j < newHeight; j++) {
				if(i < canvas.length && j < canvas[i].length) {
					out[i][j] = canvas[i][j];
				}
			}
		}
		canvas = out;
		formatSubImages();
	}

	public void updateCanvas(Color[][] newCan) {
		canvas = newCan;
		width = newCan.length;
		height = newCan[0].length;
		formatSubImages();
	}
	
	//-- Reactions  -----------------------------------
	
	@Override
	public void clickEvent(int event, int x, int y) {
		if(x >= 0 && x <= width && y >= 0 && y <= height) {
			canvas[x / zoom][y / zoom] = new Color(draw.getRGB());
			update[x  / subGridSize][y / subGridSize] = true;
		}
	}
	
	@Override
	public void dragEvent(int event, int x, int y) {
		clickEvent(-1, x, y);
	}

	@Override
	public void keyEvent(char event) {
		// TODO Auto-generated method stub
		
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setPenColor(Color drawSet) {
		draw = drawSet;
	}
	
	public void setPixelColor(int x, int y, Color col) {
		canvas[x / zoom][y / zoom] = col;
		update[x  / subGridSize][y / subGridSize] = true;
	}
	
	public void setCanvasColor(int x, int y, Color col) {
		canvas[x][y] = col;
		update[x * zoom / subGridSize][y * zoom / subGridSize] = true;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public int getZoom() {
		return zoom;
	}

	@Override
	public int getMinimumScreenX() {
		return 0;
	}

	@Override
	public int getMaximumScreenX() {
		return getWidth();
	}

	@Override
	public int getMinimumScreenY() {
		return 0;
	}

	@Override
	public int getMaximumScreenY() {
		return getHeight();
	}

	public BufferedImage getImage() {
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < canvas.length; i++) {
			for(int j = 0; j < canvas[i].length; j++) {
				out.setRGB(i, j, canvas[i][j].getRGB());
			}
		}
		return out;
	}
	
	public BufferedImage getImage(double scale) {
		int wid = (int)(scale * width);
		int hei = (int)(scale * height);
		BufferedImage out = new BufferedImage(wid, hei, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < out.getWidth(); i++) {
			for(int j = 0; j < out.getHeight(); j++) {
				out.setRGB(i, j, canvas[(int)(i / scale)][(int)(j / scale)].getRGB());
			}
		}
		return out;
	}
	
//---  Mechanics   ----------------------------------------------------------------------------

	private void formatSubImages() {
		subGridSize = zoom * (SUB_GRID_SIZE_MAXIMUM / zoom);
		xBlock = width % subGridSize == 0 ? width / subGridSize : (width / subGridSize + 1);
		yBlock = height % subGridSize == 0 ? height / subGridSize : (height / subGridSize + 1);
		update = new boolean[xBlock][yBlock];
		display = new BufferedImage[xBlock][yBlock];
		buildSubImages();
	}
	
	private void buildSubImages() {
		for(int i = 0; i < xBlock; i++) {
			for(int j = 0; j < yBlock; j++) {
				display[i][j] = buildSubImage(i, j);
				update[i][j] = true;
			}
		}
	}
	
	private BufferedImage buildSubImage(int displayX, int displayY) {
		int wid = (displayX == xBlock - 1 && width % subGridSize != 0) ? width % subGridSize : subGridSize;
		int hei = (displayY == yBlock - 1 && height % subGridSize != 0) ? height % subGridSize : subGridSize;
		BufferedImage out = new BufferedImage(wid, hei, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < wid; i++) {
			for(int j = 0; j < hei; j++) {
				if(canvas[(displayX * subGridSize + i) / zoom][(displayY * subGridSize + j) / zoom] == null) {
					out.setRGB(i, j, Color.white.getRGB());
				}
				else {
					out.setRGB(i, j, canvas[(displayX * subGridSize + i) / zoom][(displayY * subGridSize + j) / zoom].getRGB());
				}
			}
		}
		return out;
	}

	@Override
	public void clickReleaseEvent(int event, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickPressEvent(int event, int x, int y) {
		// TODO Auto-generated method stub
		
	}

}
