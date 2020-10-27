package visual.composite;

import java.awt.Color;
import java.awt.Image;

public class ImageDisplay {

//---  Constant Values   ----------------------------------------------------------------------
	
	private final static double DEFAULT_ZOOM = 1.0;
	private static final double MOVEMENT_FACTOR = .1;
	private static final double ZOOM_FACTOR = 1.1;
	
	private static final int MAXIMUM_UI_WIDTH = 800;
	private static final int MAXIMUM_UI_HEIGHT = 800;
	
	private static final double UI_BOX_RATIO_Y = 3 / 4.0;
	private static final double UI_BOX_RATIO_X = 4 / 5.0;
	private static final double UI_BOX_RATIO = 3.0 / 4.0;
	public static final String IMAGE_NAME = "img";
	
	//-- Codes  -----------------------------------------------
	private static final int CODE_MOVE_RIGHT = 10;
	private static final int CODE_MOVE_DOWN = 11;
	private static final int CODE_MOVE_LEFT = 12;
	private static final int CODE_MOVE_UP = 13;
	private static final int CODE_ZOOM_IN = 14;
	private static final int CODE_ZOOM_OUT = 15;
	private static final int CODE_RESET_POSITION = 17;
	private static final int CODE_DRAG_UI = 18;
	private static final int CODE_HIDE_UI = 19;
	private static final char KEY_MOVE_RIGHT = 'd';
	private static final char KEY_MOVE_DOWN = 's';
	private static final char KEY_MOVE_LEFT = 'a';
	private static final char KEY_MOVE_UP = 'w';
	private static final char KEY_ZOOM_IN = 'q';
	private static final char KEY_ZOOM_OUT = 'e';
	private static final char KEY_RESET_POSITION = 'h';
	private static final char KEY_HIDE_UI = 't';
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private String imageName;
	private String imagePath;
	private Image reference;
	private double zoom;
	private HandlePanel p;
	private int originUIX;
	private int originUIY;
	private boolean hideUI;
	
	private int dragStartX;
	private int dragStartY;
	private boolean dragState;
	private boolean dragUI;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public ImageDisplay(String path, HandlePanel in) {
		imagePath = path;
		p = in;
		zoom = 1;
		originUIX = 0;
		originUIY = 0;
		refresh();
	}

//---  Operations   ---------------------------------------------------------------------------
	
	public void processClickInput(int code) {
		switch(code) {
			case CODE_MOVE_RIGHT:
				decreaseOriginX();
				break;
			case CODE_MOVE_LEFT:
				increaseOriginX();
				break;
			case CODE_MOVE_UP:
				increaseOriginY();
				break;
			case CODE_MOVE_DOWN:
				decreaseOriginY();
				break;
			case CODE_ZOOM_IN:
				increaseZoom();
				break;
			case CODE_ZOOM_OUT:
				decreaseZoom();
				break;
			case CODE_RESET_POSITION:
				resetPosition();
				break;
			case CODE_HIDE_UI:
				hideUI = !hideUI;
				p.removeElementPrefixed("");
				drawPage();
				break;
			}
	}
	
	public void processKeyInput(char code) {
		switch(code) {
			case KEY_MOVE_RIGHT:
				decreaseOriginX();
				break;
			case KEY_MOVE_LEFT:
				increaseOriginX();
				break;
			case KEY_MOVE_UP:
				increaseOriginY();
				break;
			case KEY_MOVE_DOWN:
				decreaseOriginY();
				break;
			case KEY_ZOOM_IN:
				increaseZoom();
				break;
			case KEY_ZOOM_OUT:
				decreaseZoom();
				break;
			case KEY_RESET_POSITION:
				resetPosition();
				break;
			case KEY_HIDE_UI:
				hideUI = !hideUI;
				p.removeElementPrefixed("");
				drawPage();
				break;
			}
	}
	
	public void processPressInput(int code, int x, int y) {
		if(originUIX + 5 > p.getWidth()) {
			originUIX = 0;
			originUIY = 0;
			drawUI();
		}
		if(originUIY + 5 > p.getHeight()) {
			originUIX = 0;
			originUIY = 0;
			drawUI();
		}
		if(code == -1) {
			dragStartX = x;
			dragStartY = y;
			dragState = true;
		}
		else if(code == CODE_DRAG_UI) {
			dragStartX = x;
			dragStartY = y;
			dragUI = true;
		}
	}
	
	public void processReleaseInput(int code, int x, int y) {
		dragState = false;
		dragUI = false;
	}
	
	public void processMouseWheelInput(int scroll) {
		if(scroll < 0) {
			increaseZoom();
		}
		else {
			decreaseZoom();
		}
		drawPage();
	}
	
	public void processDragInput(int code, int x, int y) {
		if(dragState) {
			dragOriginX(x - dragStartX);
			dragOriginY(y - dragStartY);
			dragStartX = x;
			dragStartY = y;
		}
		else if(dragUI) {
			originUIX += x - dragStartX;
			originUIY += y - dragStartY;
			dragStartX = x;
			dragStartY = y;
			drawUI();
		}
	}
	
	public void drawPage() {
		if(!p.moveElement(IMAGE_NAME, 0, 0))
			p.addImage(IMAGE_NAME, 10, false, 0, 0, false, getImage(), getZoom());
		drawUI();
	}
	
	public void drawUI() {
		int usedWidth = p.getWidth() > MAXIMUM_UI_WIDTH ? MAXIMUM_UI_WIDTH : p.getWidth();
		int usedHeight =  p.getHeight() > MAXIMUM_UI_HEIGHT ? MAXIMUM_UI_HEIGHT : p.getHeight();
		
		int wid1 = (int)(usedWidth * (1 - UI_BOX_RATIO_X));
		int hei1 = (int)(wid1 / UI_BOX_RATIO);
		int hei2 = (int)(usedHeight * (1 - UI_BOX_RATIO_Y));
		int wid2 = (int)(hei2 * UI_BOX_RATIO);
		int useWid = wid1 < wid2 ? wid1 : wid2;
		int useHei = useWid == wid1 ? hei1 : hei2;
		
		int imageSize = useHei / 5;
		
		if(!hideUI) {
			if(!p.moveElement("ui_box_drag_button", originUIX, originUIY))
				p.addButton("ui_box_drag_button", 10, true, originUIX, originUIY, useWid, useHei, CODE_DRAG_UI, false);
			int spacing = imageSize * 5 / 4;

			int posX = originUIX + useWid / 2;
			int posY = originUIY + spacing * 3 / 5;

			if(!p.moveElement("rect_ui", originUIX, originUIY))
				p.addRectangle("rect_ui", 13, true,  originUIX, originUIY, useWid, useHei, false, Color.white, Color.black);
			
			p.handleImageButton("ui_box_zoom_in", true, posX - spacing, posY, imageSize, imageSize, "/visual/composite/assets/zoom_in.png", CODE_ZOOM_IN);
			p.handleImageButton("ui_box_zoom_out", true, posX + spacing, posY, imageSize, imageSize, "/visual/composite/assets/zoom_out.png", CODE_ZOOM_OUT);
			posY += spacing;
			p.handleImageButton("ui_box_move_up", true, posX, posY, imageSize, imageSize, "/visual/composite/assets/up_arrow.png", CODE_MOVE_UP);
			posY += spacing;
			p.handleImageButton("ui_box_move_left", true, posX - spacing, posY, imageSize, imageSize, "/visual/composite/assets/left_arrow.png", CODE_MOVE_LEFT);
			p.handleImageButton("ui_box_move_right", true, posX + spacing, posY, imageSize, imageSize, "/visual/composite/assets/right_arrow.png", CODE_MOVE_RIGHT);
			p.handleImageButton("ui_box_UI_ring", true, posX, posY, imageSize, imageSize, "/visual/composite/assets/UI_ring.png", CODE_RESET_POSITION);
			posY += spacing;
			p.handleImageButton("ui_box_move_down", true, posX, posY, imageSize, imageSize, "/visual/composite/assets/down_arrow.png", CODE_MOVE_DOWN);
			
		}
		p.handleImageButton("ui_hide_ui", true, p.getWidth() - 1 * imageSize, imageSize, imageSize * 3 / 2, imageSize * 3 / 2, hideUI ? "/visual/composite/assets/eye_open-2.png" : "/visual/composite/assets/eye_closed-2.png", CODE_HIDE_UI);
		p.addRectangle("rect_hide_ui", 13, true, p.getWidth() - 1 * imageSize, imageSize, imageSize * 3 / 2, imageSize * 3 / 2, true, Color.white, Color.black);
	}

	public void refresh() {
		clear();
		reference = p.retrieveImage(imagePath);
		imageName = formatImageName(imagePath);
		drawPage();
	}
	
	public void clear() {
		p.removeCachedImage(imagePath);
		p.removeElement(IMAGE_NAME);
		p.removeElementPrefixed("");
	}
	
	public void resetPosition() {
		p.setOffsetX(0);
		p.setOffsetY(0);
		zoom = DEFAULT_ZOOM;
		p.removeElement(IMAGE_NAME);
		drawPage();
	}

//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setImagePath(String in) {
		p.removeCachedImage(imagePath);
		imagePath = in;
		refresh();
	}
	
	public void increaseOriginX() {
		p.setOffsetX((int)(p.getOffsetX() + reference.getWidth(null) * zoom * MOVEMENT_FACTOR));
	}

	public void increaseOriginY() {
		p.setOffsetY((int)(p.getOffsetY() + reference.getHeight(null) * zoom * MOVEMENT_FACTOR));
	}
	
	public void decreaseOriginX() {
		p.setOffsetX((int)(p.getOffsetX() - reference.getWidth(null) * zoom * MOVEMENT_FACTOR));
	}
	
	public void decreaseOriginY() {
		p.setOffsetY((int)(p.getOffsetY() - reference.getHeight(null) * zoom * MOVEMENT_FACTOR));
	}
	
	public void dragOriginX(int amount) {
		p.setOffsetX(p.getOffsetX() + amount);
	}
	
	public void dragOriginY(int amount) {
		p.setOffsetY(p.getOffsetY() + amount);
	}
	
	public void increaseZoom() {
		zoom *= ZOOM_FACTOR;
		p.removeElement(IMAGE_NAME);
	}
	
	public void decreaseZoom() {
		zoom /= ZOOM_FACTOR;
		p.removeElement(IMAGE_NAME);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public String getImageName() {
		return imageName;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	
	public Image getImage() {
		return reference;
	}
	
	public double getZoom() {
		return zoom;
	}
	
//---  Mechanics   ----------------------------------------------------------------------------
	
	private String formatImageName(String in) {
		return in.substring(in.lastIndexOf("\\") + 1).substring(in.lastIndexOf("/") + 1);
	}
	
}
