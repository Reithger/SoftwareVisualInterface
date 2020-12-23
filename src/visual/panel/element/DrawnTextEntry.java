package visual.panel.element;

import java.awt.Font;
import java.awt.Graphics;

import input.mouse.ClickRegionRectangle;
import input.mouse.Detectable;

public class DrawnTextEntry extends DrawnText implements Clickable, TextStorage{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private StringBuilder storedText;
	
	private int code;
	
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
		super(inX, inY, wid, hei, prior, centerX, centerY, centerText, word, inFont);
		code = inCode;
		storedText = new StringBuilder().append(word);
		indicator = false;
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public Detectable getDetectionRegion(int offsetX, int offsetY) {
		return new ClickRegionRectangle(getX() - (getCenterX() ? getWidth() / 2 : 0) + offsetX, getY() - (getCenterY() ? getHeight() / 2 : 0) + offsetY, getWidth(), getHeight(), code, getDrawPriority());
	}

	public void addText(char in) {
		storedText.append(in);
		changeText(getText());
	}
	
	public void addText(String in) {
		storedText.append(in);
		changeText(getText());
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
		changeText(getText());
		return false;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setText(String text) {
		storedText = new StringBuilder();
		storedText.append(text);
		changeText(getText());
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public String getText() {
		return storedText.toString();
	}
	
	public int getCode() {
		return code;
	}

}
