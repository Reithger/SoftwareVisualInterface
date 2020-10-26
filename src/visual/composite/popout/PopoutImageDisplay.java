package visual.composite.popout;

import visual.composite.ImageDisplay;

public class PopoutImageDisplay extends PopoutWindow {

	private ImageDisplay imageDisplay;
	
	private int dragStartX;
	private int dragStartY;
	private boolean dragState;
	
	public PopoutImageDisplay(int wid, int hei, String ref) {
		super(wid, hei);
		this.allowScrollbars(false);
		this.setResizable(true);
		imageDisplay = new ImageDisplay(ref, getElementPanel());
		imageDisplay.designatePopout();
		imageDisplay.drawPage();
	}
	
	@Override
	public void popoutResize(int wid, int hei) {
		super.popoutResize(wid, hei);
		if(imageDisplay != null)
			imageDisplay.refresh();
	}
	
	@Override
	public void clickAction(int code, int x, int y) {
		imageDisplay.processClickInput(code);
	}

	@Override
	public void keyAction(char code) {
		if(imageDisplay != null)
			imageDisplay.processKeyInput(code);
	}

	@Override
	public void scrollAction(int scroll) {
		if(scroll < 0) {
			imageDisplay.increaseZoom();
		}
		else {
			imageDisplay.decreaseZoom();
		}
		imageDisplay.drawPage();
	}
	
	@Override
	public void clickPressAction(int code, int x, int y) {
		dragStartX = x;
		dragStartY = y;
		dragState = true;
	}
	
	@Override
	public void clickReleaseAction(int code, int x, int y) {
		dragState = false;
	}
	
	@Override
	public void dragAction(int code, int x, int y) {
		if(dragState) {
			imageDisplay.dragOriginX(x - dragStartX);
			imageDisplay.dragOriginY(y - dragStartY);
			dragStartX = x;
			dragStartY = y;
		}
	}

}
