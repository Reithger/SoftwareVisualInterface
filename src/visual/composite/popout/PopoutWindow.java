package visual.composite.popout;

import java.awt.Color;
import java.awt.Font;

import visual.composite.HandleElements;
import visual.composite.HandlePanel;
import visual.frame.WindowFrame;

public abstract class PopoutWindow implements HandleElements{

//---  Constant Values   ----------------------------------------------------------------------
	
	private final static int ROTATION_MULTIPLIER = 10;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private WindowFrame parFrame;
	private HandlePanel panel;
	
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
		panel = new HandlePanel(0, 0, width, height) {
			@Override
			public void clickBehaviour(int code, int x, int y){
				clickAction(code, x, y);
			}
			
			@Override
			public void keyBehaviour(char code) {
				keyAction(code);
			}
			
			@Override
			public void mouseWheelBehaviour(int scroll) {
				this.setOffsetYBounded(this.getOffsetY() - scroll * ROTATION_MULTIPLIER);
				scrollAction(scroll);
			}
			
			public void clickPressBehaviour(int code, int x, int y) {
				clickPressAction(code, x, y);
			}
			
			public void clickReleaseBehaviour(int code, int x, int y) {
				clickReleaseAction(code, x, y);
			}
			
			public void dragBehaviour(int code, int x, int y) {
				dragAction(code, x, y);
			}
			
		};
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

	public void dispose() {
		parFrame.disposeFrame();
	}
	
	protected void allowScrollbars(boolean set) {
		panel.setScrollBarVertical(set);
		panel.setScrollBarHorizontal(set);
	}
	
	public abstract void clickAction(int code, int x, int y);
	
	public abstract void clickPressAction(int code, int x, int y);
	
	public abstract void clickReleaseAction(int code, int x, int y);
	
	public abstract void keyAction(char code);
	
	public abstract void scrollAction(int scroll);
	
	public abstract void dragAction(int code, int x, int y);
	
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
	
//---  Drawing Support   ----------------------------------------------------------------------

	@Override
	public void handleTextButton(String nom, boolean frame, int x, int y, int wid, int hei, Font font, String phr, int code, Color fill, Color border) {
		panel.handleTextButton(nom, frame, x, y, wid, hei, font, phr, code, fill, border);
	}

	@Override
	public void handleText(String nom, boolean frame, int x, int y, int wid, int hei, Font font, String phr) {
		panel.handleText(nom, frame, x, y, wid, hei, font, phr);
	}

	@Override
	public void handleImage(String nom, boolean frame, int x, int y, String path, double scale) {
		panel.handleImage(nom, frame, x, y, path, scale);
	}

	@Override
	public void handleTextEntry(String nom, boolean frame, int x, int y, int wid, int hei, int cod, Font font, String phr) {
		panel.handleTextEntry(nom, frame, x, y, wid, hei, cod, font, phr);
	}

	@Override
	public void handleButton(String nom, boolean frame, int x, int y, int wid, int hei, int code) {
		panel.handleButton(nom, frame, x, y, wid, hei, code);
	}

	@Override
	public void handleLine(String nom, boolean frame, int prior, int x, int y, int x2, int y2, int thck, Color fill) {
		panel.handleLine(nom, frame, prior, x, y, x2, y2, thck, fill);
	}

	@Override
	public void handleRectangle(String nom, boolean frame, int prior, int x, int y, int wid, int hei, Color fill, Color border) {
		panel.handleRectangle(nom, frame, prior, x, y, wid, hei, fill, border);
	}
	
	@Override
	public void handleThickRectangle(String nom, boolean frame, int x, int y, int x2, int y2, Color border, int thick) {
		panel.handleThickRectangle(nom, frame, x, y, x2, y2, border, thick);
	}

	@Override
	public void handleImageButton(String name, boolean frame, int x, int y, int wid, int hei, String path, int code) {
		panel.handleImageButton(name, frame, x, y, wid, hei, path, code);
	}
}
