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
		xLow = xL;
		yLow = yL;
		xHigh = xH;
		yHigh = yH;
		font = f;
		codeVal = code;
		setDrawPriority(prior);
		storedText = "";
	}

	@Override
	public void drawToScreen(Graphics g) {
		Font save = g.getFont();
		g.setFont(font);
		FontMetrics fM = g.getFontMetrics();
		int x = xLow;
		int y = yLow + fM.getHeight();
		String[] words = storedText.split(" ");
		top:
		for(String s : words) {
			int wid = fM.stringWidth(s + "w");
			if(wid >= xHigh - xLow) {
				String[] letters = s.split("");
				for(String l : letters) {
					int letWid = fM.stringWidth(l);
					if(x + 2 * letWid >= xHigh) {
						x = xLow;
						y += fM.getHeight();
					}
					if(y > yHigh) {
						break top;
					}
					g.drawString(l, x, y);
					x += letWid;
				}
				continue top;
			}
			if(x + wid >= xHigh) {
				x = xLow;
				y += fM.getHeight();
			}
			if(y > yHigh) {
				break top;
			}
			g.drawString(s, x, y);
			x += wid;
		}
		g.setFont(save);
	}

	@Override
	public Detectable getDetectionRegion() {
		return new ClickRegionRectangle(xLow, yLow, xHigh, yHigh, codeVal);
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
		else if((int) in == 10) {		//Do something for new line entry
			storedText += "";
		}
		else {
			storedText += in;
		}
		return false;
	}
	
}
