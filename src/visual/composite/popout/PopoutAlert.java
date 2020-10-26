package visual.composite.popout;

import java.awt.Color;

public class PopoutAlert extends PopoutWindow{

	public PopoutAlert(int width, int height, String text) {
		super(width, height);
		int topHeight = getHeight() * 7 / 10;
		handleText("tex", false, getWidth() / 2, topHeight / 2, getWidth() * 9 / 10, topHeight * 8 / 10, null, text);
		this.handleRectangle("rect", false, 5, getWidth() / 2,  topHeight / 2, getWidth() * 9 / 10, topHeight * 8 / 10, Color.white, Color.black);
		this.handleTextButton("close", false, width / 2, topHeight + (height - topHeight) / 2, width / 5,  2 * (height - topHeight) / 3, null, "Close", -1, Color.gray, Color.black);
	}

	@Override
	public void clickAction(int code, int x, int y) {
		if(code == -1)
			dispose();	
	}

	@Override
	public void keyAction(char code) {
		dispose();	
	}

	@Override
	public void scrollAction(int scroll) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void clickPressAction(int code, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickReleaseAction(int code, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragAction(int code, int x, int y) {
		// TODO Auto-generated method stub
		
	}
	
}
