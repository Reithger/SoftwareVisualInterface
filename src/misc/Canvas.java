package misc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Canvas {

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
	/** */
	private int subGridSize;
	
	private int subGridSizeMaximum;

//---  Constructors   -------------------------------------------------------------------------
	
	public Canvas(int canWid, int canHei) {
		zoom = 1;
		canvas = new Color[canWid][canHei];
		subGridSizeMaximum = SUB_GRID_SIZE_MAXIMUM;
		formatSubImages();
		initialize();
	}
	
	public Canvas(Color[][] cols) {
		zoom = 1;
		subGridSizeMaximum = SUB_GRID_SIZE_MAXIMUM;
		canvas = cols;
		formatSubImages();
	}
	
	public Canvas(File in) throws IOException {
		BufferedImage bI = ImageIO.read(in);
		subGridSizeMaximum = SUB_GRID_SIZE_MAXIMUM;
		zoom = 1;
		canvas = new Color[bI.getWidth()][bI.getHeight()];
		for(int i = 0; i < bI.getWidth(); i++) {
			for(int j = 0; j < bI.getHeight(); j++) {
				canvas[i][j] = new Color(bI.getRGB(i, j));
			}
		}
		formatSubImages();
	}
	
	public Canvas(BufferedImage in) {
		zoom = 1;
		subGridSizeMaximum = SUB_GRID_SIZE_MAXIMUM;
		canvas = new Color[in.getWidth()][in.getHeight()];
		for(int i = 0; i < in.getWidth(); i++) {
			for(int j = 0; j < in.getHeight(); j++) {
				canvas[i][j] = new Color(in.getRGB(i, j));
			}
		}
		formatSubImages();
	}
	
	//--  Support  ------------------------------------
	
	public void initialize() {
		for(int i = 0; i < canvas.length; i++) {
			for(int j = 0; j < canvas[i].length; j++) {
				canvas[i][j] =  new Color(i  % 255, j % 255, (i * j) % 255);
			}
		}
	}

//---  Operations   ---------------------------------------------------------------------------

	public void commandUnder(Graphics g) {
		//Overwrite method to allow custom underlay
	}

	public void updateSubImages() {
		for(int i = 0; i < update.length; i++) {
			for(int j = 0; j < update[i].length; j++) {
				if(update[i][j]) {
					display[i][j] = buildSubImage(i, j);
					update[i][j] = false;
				}
			}
		}
	}
	
	public void draw(Graphics g, int xC, int yC, int maxWidth, int maxHeight) {
		Color save = g.getColor();
		commandUnder(g);
		
		updateSubImages();
		
		for(int i = 0; i < display.length; i++) {
			for(int j = 0; j < display[i].length; j++) {
				int x = i * subGridSize;
				int y = j * subGridSize;
				if(x < maxWidth && y < maxHeight) {
					g.drawImage(display[i][j], xC + x, yC + y, null);
				}
			}
		}
		commandOver(g);
		g.setColor(save);
	}
	
	public void commandOver(Graphics g) {
		//Overwrite method to allow custom overlay
	}

	public void input(int code) {
		//Override this
	}
	
	public void input(char code) {
		//Override this
	}
	
	public void updateCanvasSize(int canWid, int canHei) {
		Color[][] out = new Color[canWid][canHei];
		for(int i = 0; i < canWid; i++) {
			for(int j = 0; j < canHei; j++) {
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
		formatSubImages();
	}

//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setZoom(int in) {
		if(in <= 0) {
			in = 1;
		}
		zoom = in;
		formatSubImages();
	}
	
	public void setPixelColor(int x, int y, Color col) {
		int canX = x / zoom;
		int canY = y / zoom;
		if(canX < canvas.length && canY < canvas[0].length) {
			canvas[canX][canY] = col;
			update[x  / subGridSize][y / subGridSize] = true;
		}
	}
	
	public void setCanvasColor(int x, int y, Color col) {
		if(x < canvas.length && y < canvas[0].length) {
			canvas[x][y] = col;
			update[x * zoom / subGridSize][y * zoom / subGridSize] = true;
		}
	}

	public void setSubGridSizeMaximum(int in) {
		subGridSizeMaximum = in;
		formatSubImages();
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public int getSubGridSizeMaximum() {
		return subGridSizeMaximum;
	}
	
	public int getCanvasWidth() {
		return canvas.length;
	}
	
	public int getCanvasHeight() {
		if(canvas.length <= 0) {
			return 0;
		}
		return canvas[0].length;
	}
	
	public int getCanvasZoomWidth() {
		return getCanvasWidth() * getZoom();
	}
	
	public int getCanvasZoomHeight() {
		return getCanvasHeight() * getZoom();
	}
	
	public int getZoom() {
		return zoom;
	}

	public Color[][] getColorData(){
		return canvas;
	}
	
	public BufferedImage getImage() {
		BufferedImage out = new BufferedImage(canvas.length, canvas[0].length, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < canvas.length; i++) {
			for(int j = 0; j < canvas[i].length; j++) {
				if(canvas[i][j] == null) {
					out.setRGB(i, j, Color.white.getRGB());
				}
				else {
					out.setRGB(i, j, canvas[i][j].getRGB());
				}
			}
		}
		return out;
	}
	
	public BufferedImage getImage(double scale) {
		int wid = (int)(scale * canvas.length);
		int hei = (int)(scale * canvas[0].length);
		BufferedImage out = new BufferedImage(wid, hei, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < out.getWidth(); i++) {
			for(int j = 0; j < out.getHeight(); j++) {
				out.setRGB(i, j, canvas[(int)(i / scale)][(int)(j / scale)].getRGB());
			}
		}
		return out;
	}

	public Color getPixelColor(int x, int y) {
		if(x/zoom < canvas.length && y / zoom < canvas[x / zoom].length) {
			return canvas[x / zoom][y / zoom];
		}
		return null;
	}
	
	public Color getCanvasColor(int x, int y) {
		if(x < canvas.length && y < canvas[x].length) {
			return canvas[x][y];
		}
		return null;
	}

//---  Mechanics   ----------------------------------------------------------------------------

	private void formatSubImages() {
		calculateSubGridSize();
		int xBlock = getNumXBlocks();
		int yBlock = getNumYBlocks();
		update = new boolean[xBlock][yBlock];
		display = new BufferedImage[xBlock][yBlock];
		buildSubImages(xBlock, yBlock);
	}
	
	private void calculateSubGridSize() {
		subGridSize = zoom * (subGridSizeMaximum / zoom);
		subGridSize = ((subGridSize == 0) ? 1 : subGridSize);
	}
	
	private void buildSubImages(int xBlock, int yBlock) {
		for(int i = 0; i < xBlock; i++) {
			for(int j = 0; j < yBlock; j++) {
				display[i][j] = buildSubImage(i, j);
				update[i][j] = true;
			}
		}
	}
	
	private BufferedImage buildSubImage(int displayX, int displayY) {
		int wid = getSubImageWidth(displayX);
		int hei = getSubImageHeight(displayY);
		BufferedImage out = new BufferedImage(wid, hei, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < wid; i++) {
			for(int j = 0; j < hei; j++) {
				int indX = (displayX * subGridSize + i) / zoom;
				int indY = (displayY * subGridSize + j) / zoom;
				if(indX >= canvas.length || indY >= canvas[0].length || canvas[indX][indY] == null) {
					out.setRGB(i, j, new Color(255, 255, 255, 255).getRGB());
				}
				else {
					out.setRGB(i, j, canvas[indX][indY].getRGB());
				}
			}
		}
		return out;
	}
	
	private int getSubImageWidth(int displayX) {
		int width = getCanvasZoomWidth();
		int xBlock = getNumXBlocks();
		return ((displayX == xBlock - 1 && width % subGridSize != 0) ? width % subGridSize : subGridSize);
	}
	
	private int getSubImageHeight(int displayY) {
		int height = getCanvasZoomHeight();
		int yBlock = getNumYBlocks();
		return ((displayY == yBlock - 1 && height % subGridSize != 0) ? height % subGridSize : subGridSize);
	}
	
	private int getNumXBlocks() {
		int width = getCanvasZoomWidth();
		return ((width % subGridSize == 0) ? width / subGridSize : (width / subGridSize + 1));
	}
	
	private int getNumYBlocks() {
		int height = getCanvasZoomHeight();
		return ((height % subGridSize == 0) ? height / subGridSize : (height / subGridSize + 1));
	}

}
