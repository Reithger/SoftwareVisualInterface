package visual.panel.element;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import input.ClickRegionRectangle;
import input.Detectable;

public class DrawnTextArea extends Element implements Clickable, TextStorage{

	private int xHigh;
	private int yHigh;
	private int xLow;
	private int yLow;
	private int codeVal;
	private Font font;
	private String storedText;
	
	public DrawnTextArea (int xL, int yL, int xH, int yH, int prior, int code, Font f) {
		xHigh = xL;
		yHigh = yL;
		xLow = xH;
		yLow = yH;
		font = f;
		codeVal = code;
		setDrawPriority(prior);
		storedText = " ";
	}

	@Override
	public void drawToScreen(Graphics g) {
		Font save = g.getFont();
		g.setFont(font);
		FontMetrics fM = g.getFontMetrics();
		int x = xHigh;
		int y = yHigh + fM.getHeight();
		String[] words = storedText.split(" ");
		for(String s : words) {
			int wid = fM.stringWidth(s + "w");
			if(x + wid >= xLow) {
				x = xHigh;
				y += fM.getHeight();	
			}
			g.drawString(s, x, y);
			x += wid;
		}
		g.setFont(save);
	}

	@Override
	public Detectable getDetectionRegion() {
		return new ClickRegionRectangle(xHigh, yHigh, xLow, yLow, codeVal);
	}

	public void addText(String in) {
		storedText += in;
	}
	
	public String getText() {
		return storedText;
	}
	
	public boolean focusEvent(char in) {
		if((int) in == 65535)
			return false;
		if((int) in == 8){
			if(storedText.length() == 0)
				return false;
			storedText = storedText.substring(0, storedText.length()-1);
		}
		else {
			storedText += in;
		}
		return false;
	}
	
}
