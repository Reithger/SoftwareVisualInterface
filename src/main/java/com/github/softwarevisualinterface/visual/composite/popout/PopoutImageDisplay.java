package com.github.softwarevisualinterface.visual.composite.popout;

import com.github.softwarevisualinterface.visual.composite.ImageDisplay;

public class PopoutImageDisplay extends PopoutWindow {

	private ImageDisplay imageDisplay;
	
	public PopoutImageDisplay(int wid, int hei, String ref) {
		super(wid, hei);
		this.setResizable(true);
		imageDisplay = new ImageDisplay(ref, getHandlePanel());
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
		imageDisplay.processPressInput(code, x, y);
	}
	
	@Override
	public void clickReleaseAction(int code, int x, int y) {
		imageDisplay.processReleaseInput(code, x, y);
	}
	
	@Override
	public void dragAction(int code, int x, int y) {
		imageDisplay.processDragInput(code,  x,  y);
	}

}
