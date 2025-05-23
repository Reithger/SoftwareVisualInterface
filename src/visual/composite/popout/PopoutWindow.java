package visual.composite.popout;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;

import input.CustomEventReceiver;
import visual.composite.HandleElements;
import visual.composite.HandlePanel;
import visual.frame.Frame;
import visual.frame.WindowFrame;

public abstract class PopoutWindow implements HandleElements{

//---  Constant Values   ----------------------------------------------------------------------
	
	private final static int ROTATION_MULTIPLIER = 10;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private WindowFrame parFrame;
	private HandlePanel panel;
	
	private boolean clickAndDragMode;
	
	private int lastX;
	private int lastY;
	private boolean dragged;
	
//---  Constructors   -------------------------------------------------------------------------
	
 	public PopoutWindow(int width, int height) {
		panel = null;
		parFrame = new WindowFrame(width, height) {
			@Override
			public void reactToResize() {
				if(panel != null) {
					popoutResize(parFrame.getWidth(), parFrame.getHeight());
				}
			}
			
		};
		parFrame.setName("Popup Window");
		parFrame.setResizable(false);
		parFrame.setExitOnClose(false);
		parFrame.setLocationRelativeTo(null);
		panel = new HandlePanel(0, 0, width, height);
		panel.setEventReceiver(new CustomEventReceiver(){
			@Override
			public void clickEvent(int code, int x, int y, int clickType){
				clickAction(code, x, y);
			}
			
			@Override
			public void keyEvent(char code) {
				keyAction(code);
			}
			
			@Override
			public void mouseWheelEvent(int scroll) {
				panel.setOffsetY("move", panel.getOffsetY("move") - scroll * ROTATION_MULTIPLIER);
				scrollAction(scroll);
			}

			@Override
			public void clickPressEvent(int code, int x, int y, int clickType) {
				clickPressAction(code, x, y);
			}

			@Override
			public void clickReleaseEvent(int code, int x, int y, int clickType) {
				clickReleaseAction(code, x, y);
			}

			@Override
			public void dragEvent(int code, int x, int y, int clickType) {
				dragAction(code, x, y);
			}
			
		});
		parFrame.reserveWindow("popout");
		parFrame.showActiveWindow("popout");
		parFrame.addPanelToWindow("popout", "pan", panel);
	}
	
	public PopoutWindow(int width, int height, Frame locReference) {
		panel = null;
		parFrame = new WindowFrame(width, height) {
			@Override
			public void reactToResize() {
				if(panel != null) {
					popoutResize(parFrame.getWidth(), parFrame.getHeight());
				}
			}
			
		};
		parFrame.setName("Popup Window");
		parFrame.setResizable(false);
		parFrame.setExitOnClose(false);
		parFrame.setLocationRelativeTo(locReference);
		panel = new HandlePanel(0, 0, width, height);
		panel.setEventReceiver(new CustomEventReceiver(){
			@Override
			public void clickEvent(int code, int x, int y, int clickType){
				clickAction(code, x, y);
			}
			
			@Override
			public void keyEvent(char code) {
				keyAction(code);
			}
			
			@Override
			public void mouseWheelEvent(int scroll) {
				panel.setOffsetY("move", panel.getOffsetY("move") - scroll * ROTATION_MULTIPLIER);
				scrollAction(scroll);
			}

			@Override
			public void clickPressEvent(int code, int x, int y, int clickType) {
				clickPressAction(code, x, y);
			}

			@Override
			public void clickReleaseEvent(int code, int x, int y, int clickType) {
				clickReleaseAction(code, x, y);
			}

			@Override
			public void dragEvent(int code, int x, int y, int clickType) {
				dragAction(code, x, y);
			}
			
		});
		parFrame.reserveWindow("popout");
		parFrame.showActiveWindow("popout");
		parFrame.addPanelToWindow("popout", "pan", panel);
	}

//---  Operations   ---------------------------------------------------------------------------

	public void popoutResize(int wid, int hei) {
		if(panel != null) {
			panel.resize(wid, hei);
			panel.repaint();
		}
	}

	public void toggleClickAndDragMode() {
		clickAndDragMode = !clickAndDragMode;
	}
	
	public void dispose() {
		parFrame.disposeFrame();
	}

	public void resize(int wid, int hei) {
		parFrame.resize(wid, hei);
	}
	
	public void clickAction(int code, int x, int y) {
		
	}
	
	public void clickPressAction(int code, int x, int y) {
		if(clickAndDragMode) {
			lastX = x;
			lastY = y;
			dragged = true;
		}
	}
	
	public void clickReleaseAction(int code, int x, int y) {
		if(clickAndDragMode) {
			dragged = false;
		}
	}
	
	public void keyAction(char code) {
		
	}
	
	public void scrollAction(int scroll) {
		
	}
	
	public void dragAction(int code, int x, int y) {
		if(clickAndDragMode) {
			if(dragged) {
				Point p = parFrame.getLocation();
				parFrame.setLocation(new Point((int)(p.getX() + (x - lastX)), (int)(p.getY() + (y - lastY))));
			}
		}
	}
	
	protected void removeElementPrefixed(String in) {
		panel.removeElementPrefixed(in);
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setTitle(String in) {
		parFrame.setName(in);
	}
	
	public void setResizable(boolean in) {
		parFrame.setResizable(in);
	}
	
	public void setStoredText(String ref, String now) {
		panel.setElementStoredText(ref, now);
	}
	
	public void setOffsetX(String group, int newOffX) {
		panel.setOffsetX(group, newOffX);
	}
	
	public void setOffsetY(String group, int newOffY) {
		panel.setOffsetY(group, newOffY);
	}

	public void setFrameShapeDisc() {
		parFrame.setFrameShapeDisc();
	}
	
	public void setFrameShapeNormal() {
		parFrame.setFrameShapeNormal();
	}
	
	public void setExitOnClose(boolean in) {
		parFrame.setExitOnClose(in);
	}

	public void setBackgroundColor(Color col) {
		parFrame.setBackgroundColor(col);
	}
	
	public void setFrameShapeArbitrary(Shape frameShape) {
		parFrame.setFrameShapeArbitrary(frameShape);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public int getWidth() {
		return parFrame.getWidth();
	}
	
	public int getHeight() {
		return parFrame.getHeight();
	}
	
	public String getStoredText(String ref) {
		return panel.getElementStoredText(ref);
	}
	
	public HandlePanel getHandlePanel() {
		return panel;
	}
	
	public int getOffsetX(String group) {
		return panel.getOffsetX(group);
	}
	
	public int getOffsetY(String group) {
		return panel.getOffsetY(group);
	}
	
//---  Drawing Support   ----------------------------------------------------------------------

	@Override
	public void handleTextButton(String nom, String group, int prior, int x, int y, int wid, int hei, Font font, String phr, int code, Color fill, Color border) {
		panel.handleTextButton(nom, group, prior, x, y, wid, hei, font, phr, code, fill, border);
	}

	@Override
	public void handleText(String nom, String group, int prior, int x, int y, int wid, int hei, Font font, String phr) {
		panel.handleText(nom, group, prior, x, y, wid, hei, font, phr);
	}

	@Override
	public void handleImage(String nom, String group, int prior, int x, int y, String path, double scale) {
		panel.handleImage(nom, group, prior, x, y, path, scale);
	}

	@Override
	public void handleImage(String nom, String group, int prior, int x, int y, Image img, double scale) {
		panel.handleImage(nom, group, prior, x, y, img, scale);
	}

	@Override
	public void handleImage(String nom, String group, int prior, int x, int y, int wid, int hei, boolean prop, String imgPath) {
		panel.handleImage(nom, group, prior, x, y, wid, hei, prop, imgPath);
	}

	@Override
	public void handleImage(String nom, String group, int prior, int x, int y, int wid, int hei, boolean prop, Image img) {
		panel.handleImage(nom, group, prior, x, y, wid, hei, prop, img);
	}
	
	@Override
	public void handleTextEntry(String nom, String group, int prior, int x, int y, int wid, int hei, int cod, Font font, String phr) {
		panel.handleTextEntry(nom, group, prior, x, y, wid, hei, cod, font, phr);
	}

	@Override
	public void handleButton(String nom, String group, int prior, int x, int y, int wid, int hei, int code) {
		panel.handleButton(nom, group, prior, x, y, wid, hei, code);
	}

	@Override
	public void handleLine(String nom, String group, int prior, int x, int y, int x2, int y2, int thck, Color fill) {
		panel.handleLine(nom, group, prior, x, y, x2, y2, thck, fill);
	}

	@Override
	public void handleRectangle(String nom, String group, int prior, int x, int y, int wid, int hei, Color fill, Color border) {
		panel.handleRectangle(nom, group, prior, x, y, wid, hei, fill, border);
	}
	
	@Override
	public void handleThickRectangle(String nom, String group, int prior, int x, int y, int x2, int y2, Color border, int thick) {
		panel.handleThickRectangle(nom, group, prior, x, y, x2, y2, border, thick);
	}

	@Override
	public void handleImageButton(String name, String group, int prior, int x, int y, int wid, int hei, String path, int code) {
		panel.handleImageButton(name, group, prior, x, y, wid, hei, path, code);
	}
	
	@Override
	public void handleScrollbar(String name, String group, String controlledGroup, int prior, int scrollX, int scrollY, int scrollWid, int scrollHei, int windowAxisOrigin, int windowSize, boolean isBarVert) {
		panel.handleScrollbar(name, group, controlledGroup, prior, scrollX,  scrollY, scrollWid, scrollHei, windowAxisOrigin, windowSize, isBarVert);
	}
	
	@Override
	public void handleCanvas(String name, String group, int prior, int x, int y, int wid, int hei, int canWid, int canHei, int code) {
		panel.handleCanvas(name, group, prior, x, y, wid, hei, canWid, canHei, code);
	}
}
