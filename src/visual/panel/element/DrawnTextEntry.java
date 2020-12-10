package visual.panel.element;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;

import input.mouse.ClickRegionRectangle;
import input.mouse.Detectable;

public class DrawnTextEntry extends Element implements Clickable, TextStorage{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private StringBuilder storedText;
	
	private DrawnText drText;

	private int code;
	
	private int width;
	
	private int height;
	
	private boolean centeredX;
	
	private boolean centeredY;
	
	private boolean indicator;

//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 * 
	 * @param xH
	 * @param yH
	 * @param xL
	 * @param yL
	 * @param prior
	 * @param center
	 * @param word
	 * @param inFont
	 */
	
	public DrawnTextEntry(int inX, int inY, int wid, int hei, int prior, boolean centerX, boolean centerY, boolean centerText, String word, Font inFont, int inCode) {
		drText = new DrawnText(inX, inY, wid, hei, prior, centerX, centerY, centerText, word, inFont);
		code = inCode;
		storedText = new StringBuilder().append(word);
		setX(inX);
		setY(inY);
		width = wid;
		height = hei;
		centeredX = centerX;
		centeredY = centerY;
		indicator = false;
		setDrawPriority(prior);
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void drawToScreen(Graphics g, int offsetX, int offsetY) {
		drText.drawToScreen(g, offsetX, offsetY);
	}
	
	@Override
	public Detectable getDetectionRegion(int offsetX, int offsetY) {
		return new ClickRegionRectangle(getX() - (centeredX ? width / 2 : 0) + offsetX, getY() - (centeredY ? height / 2 : 0) + offsetY, width, height, code, getDrawPriority());
	}

	public void addText(char in) {
		storedText.append(in);
		drText.changeText(getText());
	}
	
	public void addText(String in) {
		storedText.append(in);
		drText.changeText(getText());
	}
	
	public boolean focusEvent(char in) {
		if((int) in == 65535)
			return false;
		if((int) in == 8){
			if(storedText.length() == 0)
				return false;
			storedText = storedText.deleteCharAt(storedText.length()-1);
		}
		else if((int) in == 10) {		//Do something for new line entry
			addText("\n");
		}
		else {
			addText(in+"");
		}
		drText.changeText(getText());
		return false;
	}
	
	@Override
	public void setX(int inX) {
		drText.setX(inX);
		super.setX(inX);
	}
	
	@Override
	public void setY(int inY) {
		drText.setY(inY);
		super.setY(inY);
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setText(String text) {
		storedText = new StringBuilder();
		storedText.append(text);
		drText.changeText(getText());
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public String getText() {
		return storedText.toString();
	}
	
	public int getCode() {
		return code;
	}

	@Override
	public int getMinimumX() {
		return drText.getMinimumX();
	}

	@Override
	public int getMaximumX() {
		return drText.getMaximumX();
	}

	@Override
	public int getMinimumY() {
		return drText.getMinimumY();
	}

	@Override
	public int getMaximumY() {
		return drText.getMaximumY();
	}
}
