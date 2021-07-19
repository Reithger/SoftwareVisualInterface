package visual.composite;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import input.CustomEventReceiver;
import visual.panel.ElementPanel;

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
	private static final int CODE_AUTOFIT_ZOOM = 20;
	private static final int CODE_HELP_PAGE = 21;
	private static final char KEY_MOVE_RIGHT = 'd';
	private static final char KEY_MOVE_DOWN = 's';
	private static final char KEY_MOVE_LEFT = 'a';
	private static final char KEY_MOVE_UP = 'w';
	private static final char KEY_ZOOM_IN = 'q';
	private static final char KEY_ZOOM_OUT = 'e';
	private static final char KEY_RESET_POSITION = 'h';
	private static final char KEY_HIDE_UI = 't';
	private static final char KEY_AUTOFIT_ZOOM = 'z';
	private static final char KEY_HELP_PAGE = '?';
	
	private static final String ELEMENT_HELP = "help";
	private static final Font HELP_FONT = new Font("Serif", Font.BOLD, 22);
	private static final String HELP_PAGE = 
			"Keybinds:\n"
			+ "'d' - move camera right\n"
			+ "'s' - move camera down\n"
			+ "'a' - move camera left\n"
			+ "'w' - move camera up\n"
			+ "'q' - zoom in\n"
			+ "'e' - zoom out\n"
			+ "'h' - reset coordinate position\n"
			+ "'z' - autofit image to container\n"
			+ "'t' - show/hide UI (if enabled)\n"
			+ "'?' - open Help Page";
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private Image reference;
	private double zoom;
	private ElementPanel p;
	private int originUIX;
	private int originUIY;
	private boolean hideUI;
	private boolean disableToggleUI;
	
	private int dragStartX;
	private int dragStartY;
	private boolean dragState;
	private boolean dragUI;
	
	private boolean disableHelp;
	private boolean help;
	private boolean update;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public ImageDisplay(String path, ElementPanel in) {
		reference = in.retrieveImage(path);
		originUIX = 0;
		originUIY = 0;
		zoom = 1;
		p = in;
	}
	
	public ImageDisplay(Image ref, ElementPanel in) {
		reference = ref;
		p = in;
		zoom = 1;
		originUIX = 0;
		originUIY = 0;
	}

//---  Operations   ---------------------------------------------------------------------------
	
	public CustomEventReceiver generateEventReceiver() {
		return new CustomEventReceiver() {
			
			@Override
			public void clickEvent(int code, int x, int y, int mouseType) {
				processClickInput(code);
			}
			
			@Override 
			public void clickPressEvent(int code, int x, int y, int mouseType) {
				processPressInput(code, x, y);
			}
			
			@Override
			public void clickReleaseEvent(int code, int x, int y, int mouseType) {
				processReleaseInput(code, x, y);
			}
			
			@Override
			public void mouseWheelEvent(int scroll) {
				processMouseWheelInput(scroll);
			}
			
			@Override
			public void dragEvent(int code, int x, int y, int mouseType) {
				processDragInput(code, x, y);
			}
			
			@Override
			public void keyEvent(char key) {
				processKeyInput(key);
			}
			
		};
	}
	
	public void processClickInput(int code) {
		if(help) {
			help = false;
			refresh();
			return;
		}
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
				zoomRefresh();
				break;
			case CODE_ZOOM_OUT:
				decreaseZoom();
				zoomRefresh();
				break;
			case CODE_RESET_POSITION:
				resetPosition();
				break;
			case CODE_HIDE_UI:
				toggleUI();
				refresh();
				break;
			}
	}
	
	public void processKeyInput(char code) {
		if(help) {
			help = false;
			refresh();
			return;
		}
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
				zoomRefresh();
				break;
			case KEY_ZOOM_OUT:
				decreaseZoom();
				zoomRefresh();
				break;
			case KEY_RESET_POSITION:
				resetPosition();
				break;
			case KEY_AUTOFIT_ZOOM:
				autofitImage();
				zoomRefresh();
				break;
			case KEY_HIDE_UI:
				toggleUI();
				refresh();
				break;
			case KEY_HELP_PAGE:
				if(!disableHelp) {
					help = true;
					refresh();
				}
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
		update = true;
		zoomRefresh();
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
		if(help) {
			p.removeAllElements();
			p.addRectangle(ELEMENT_HELP + "_rect", 2, "no_move", p.getWidth() / 2, p.getHeight() / 2, p.getWidth() * 9 / 10, p.getHeight() * 9 / 10, true, Color.white, Color.black);
			p.addText(ELEMENT_HELP + "_text", 5, "no_move", p.getWidth() / 2, p.getHeight() / 2, p.getWidth() * 9 / 10, p.getHeight() * 9 / 10, HELP_PAGE, HELP_FONT, true, true, true);
		}
		else {
			p.removeElementPrefixed(ELEMENT_HELP);
			if(update) {
				p.removeElement(IMAGE_NAME);
				update = false;
			}
			if(!p.moveElement(IMAGE_NAME, 0, 0)) {
				p.addImage(IMAGE_NAME, 10, "image_display_navigate", 0, 0, false, getImage(), getZoom());
			}
			drawUI();
		}
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
				p.addButton("ui_box_drag_button", 10, "no_move", originUIX, originUIY, useWid, useHei, CODE_DRAG_UI, false);
			int spacing = imageSize * 5 / 4;

			int posX = originUIX + useWid / 2;
			int posY = originUIY + spacing * 3 / 5;

			if(!p.moveElement("rect_ui", originUIX, originUIY))
				p.addRectangle("rect_ui", 13, "no_move",  originUIX, originUIY, useWid, useHei, false, Color.white, Color.black);
			
			handleImageButton("ui_box_zoom_in", "no_move", posX - spacing, posY, imageSize, imageSize, "/visual/composite/assets/zoom_in.png", CODE_ZOOM_IN);
			handleImageButton("ui_box_zoom_out", "no_move", posX + spacing, posY, imageSize, imageSize, "/visual/composite/assets/zoom_out.png", CODE_ZOOM_OUT);
			posY += spacing;
			handleImageButton("ui_box_move_up", "no_move", posX, posY, imageSize, imageSize, "/visual/composite/assets/up_arrow.png", CODE_MOVE_UP);
			posY += spacing;
			handleImageButton("ui_box_move_left", "no_move", posX - spacing, posY, imageSize, imageSize, "/visual/composite/assets/left_arrow.png", CODE_MOVE_LEFT);
			handleImageButton("ui_box_move_right", "no_move", posX + spacing, posY, imageSize, imageSize, "/visual/composite/assets/right_arrow.png", CODE_MOVE_RIGHT);
			handleImageButton("ui_box_UI_ring", "no_move", posX, posY, imageSize, imageSize, "/visual/composite/assets/UI_ring.png", CODE_RESET_POSITION);
			posY += spacing;
			handleImageButton("ui_box_move_down", "no_move", posX, posY, imageSize, imageSize, "/visual/composite/assets/down_arrow.png", CODE_MOVE_DOWN);
			
		}
		if(!disableToggleUI) {
			handleImageButton("ui_hide_ui", "no_move", p.getWidth() - 1 * imageSize, imageSize, imageSize * 3 / 2, imageSize * 3 / 2, hideUI ? "/visual/composite/assets/eye_open-2.png" : "/visual/composite/assets/eye_closed-2.png", CODE_HIDE_UI);
			p.addRectangle("rect_hide_ui", 13, "no_move", p.getWidth() - 1 * imageSize, imageSize, imageSize * 3 / 2, imageSize * 3 / 2, true, Color.white, Color.black);
		}
	}

	public void toggleUI() {
		hideUI = !hideUI;
	}
	
	/**
	 * Metaproperty version of UI toggle that removes ability to toggle its view
	 * 
	 */
	
	public void toggleDisableToggleUI() {
		disableToggleUI = !disableToggleUI;
	}
	
	public void toggleDisableHelp() {
		disableHelp = !disableHelp;
	}
	
	public void autofitImage() {
		int wid = reference.getWidth(null);
		int hei = reference.getHeight(null);
		zoom = (p.getWidth() / (double)wid);
		double ot = (p.getHeight() / (double)hei);
		zoom = zoom < ot ? zoom : ot;
	}
	
	public void zoomRefresh() {
		p.removeElement(IMAGE_NAME);
		drawPage();
	}
	
	public void refreshImage(String path) {
		p.removeCachedImage(path);
		refresh();
	}
	
	public void refresh() {
		clear();
		drawPage();
	}
	
	public void clear() {
		p.removeElementPrefixed("");
	}
	
	public void resetPosition() {
		p.setOffsetX("image_display_navigate", 0);
		p.setOffsetY("image_display_navigate", 0);
	}
	
	public void resetZoom() {
		zoom = 1;
	}

	public void handleImageButton(String name, String frame, int x, int y, int wid, int hei, String path, int code) {
		String imageName = name + "_image";
		if(!p.moveElement(imageName, x, y)) {
			double imgWid = p.retrieveImage(path).getWidth(null);
			double zoom = 1.0;
			if(imgWid != wid) {
				zoom = wid / imgWid;
			}
			p.addImage(imageName,15, frame, x, y, true, path, zoom);
		}
		String buttonName = name + "_button";
		if(!p.moveElement(buttonName, x, y)) {
			p.addButton(buttonName, 15, frame,  x, y, wid, hei, code, true);
		}
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setImage(String in) {
		setImage(p.retrieveImage(in));
	}
	
	public void setImage(Image in) {
		reference = in;
		update = true;
	}
	
	public void increaseOriginX() {
		p.setOffsetX("image_display_navigate", (int)(p.getOffsetX("image_display_navigate") + reference.getWidth(null) * zoom * MOVEMENT_FACTOR));
	}

	public void increaseOriginY() {
		p.setOffsetY("image_display_navigate", (int)(p.getOffsetY("image_display_navigate") + reference.getHeight(null) * zoom * MOVEMENT_FACTOR));
	}
	
	public void decreaseOriginX() {
		p.setOffsetX("image_display_navigate", (int)(p.getOffsetX("image_display_navigate") - reference.getWidth(null) * zoom * MOVEMENT_FACTOR));
	}
	
	public void decreaseOriginY() {
		p.setOffsetY("image_display_navigate", (int)(p.getOffsetY("image_display_navigate") - reference.getHeight(null) * zoom * MOVEMENT_FACTOR));
	}
	
	public void dragOriginX(int amount) {
		p.setOffsetX("image_display_navigate", p.getOffsetX("image_display_navigate") + amount);
	}
	
	public void dragOriginY(int amount) {
		p.setOffsetY("image_display_navigate", p.getOffsetY("image_display_navigate") + amount);
	}
	
	public void increaseZoom() {
		zoom *= ZOOM_FACTOR;
	}
	
	public void decreaseZoom() {
		zoom /= ZOOM_FACTOR;
	}
	
	public void setZoom(double in) {
		zoom = in;
	}
	
	public void setOffsetX(int in) {
		p.setOffsetX("image_display_navigate", in);
	}
	
	public void setOffsetY(int in) {
		p.setOffsetY("image_display_navigate", in);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------

	public Image getImage() {
		return reference;
	}
	
	public double getZoom() {
		return zoom;
	}
	
	public int getOffsetX() {
		return p.getOffsetX("image_display_navigate");
	}
	
	public int getOffsetY() {
		return p.getOffsetY("image_display_navigate");
	}
	
}
