package com.github.softwarevisualinterface.visual.panel.element;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import com.github.softwarevisualinterface.input.mouse.ClickRegionRectangle;
import com.github.softwarevisualinterface.input.mouse.Detectable;

public class DrawnTextEntry extends DrawnText implements Clickable, TextStorage{

//---  Constants   ----------------------------------------------------------------------------
	
	private static final int INDICATOR_INTERVAL = 1000;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private StringBuilder storedText;
	
	private int code;
	
	private int index;
	
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
		index = word.length();
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void drawToScreen(Graphics g, int offsetX, int offsetY) {
		super.drawToScreen(g, offsetX, offsetY);

		if(indicator && System.currentTimeMillis() % INDICATOR_INTERVAL < INDICATOR_INTERVAL / 2) {
			String[] lines = storedText.toString().replaceAll("\n\n", "\n \n").replaceAll("\n$", "\n ").split("\n");
			
			int posX = getX() - (getCentered() ? getWidth() / 2 : 0);
			int posY = getY() - (getCentered() ? getHeight() / 2 : 0);

			int ind = 0;
			int count = 0;
			while(count <= index && ind < lines.length) {
				count += lines[ind++].length() + 1;
			}
			
			String use = lines[ind - 1].length() == 0 ? " " : lines[ind - 1];
			
			
			FontMetrics fm = g.getFontMetrics(getFont());
			
			posY += fm.getHeight() * ind + (getCenterY() ? (getHeight() - fm.getHeight() * lines.length) / 2 : 0);
			
			int lineWid = fm.stringWidth(use);
			int posit = use.length() - (count - (index));
			int secWid = posit < 0 ? 0 : fm.stringWidth(use.substring(0, posit) + use.charAt(posit));
			
			posX += secWid + (getCenterX() ? (getWidth() - lineWid) / 2 : 0);
			
			Color save = g.getColor();
			g.setColor(Color.black);
			int indHeight = fm.getHeight() * 3 / 4;
		    g.fillRect(posX, posY - indHeight, 2, indHeight);
			g.setColor(save);
		}

	}
	
	@Override
	public Detectable getDetectionRegion(int offsetX, int offsetY) {
		return new ClickRegionRectangle(getX() - (getCenterX() ? getWidth() / 2 : 0) + offsetX, getY() - (getCenterY() ? getHeight() / 2 : 0) + offsetY, getWidth(), getHeight(), code, getDrawPriority());
	}

	public void addText(char in) {
		storedText.insert(index++, in);
		changeText(getText());
	}
	
	public void addText(String in) {
		storedText.insert(index, in);
		index += in.length();
		changeText(getText());
	}
	
	//left up right down, 37 38, 39, 40
	
	public boolean focusKeyEvent(char in) {
		switch((int)in) {
			case 65535:
				return false;
			case 8:
				if(storedText.length() == 0)
					return false;
				storedText = storedText.deleteCharAt(--index);
				break;
			case 10:
				addText("\n");
				break;
			case 1:			//VK_LEFT
				index--;
				index = index < 0 ? 0 : index;
				break;
			case 2:			//VK_UP
				index = getPriorNewline();
				break;
			case 3:			//VK_RIGHT
				index++;
				index = index >= storedText.length() ? storedText.length() : index;
				break;
			case 4:			//VK_DOWN
				index = getNextNewline();
				break;
			default:
				addText(in+"");
				break;
		}
		changeText(getText());
		return false;
	}
	
	private int getPriorNewline() {
		String[] search = storedText.toString().split("\n");
		int out = 0;
		for(int i = 0; i < search.length; i++) {
			int next = out + (search[i].length() + 1);
			if(index <= next) {
				break;
			}
			out = next;
		}
		return out;
	}
	
	private int getNextNewline() {
		String[] search = storedText.toString().split("\n");
		int out = storedText.length();
		for(int i = search.length - 1; i >= 0; i--) {
			int next = out - (search[i].length() + 1);
			if(index >= next) {
				break;
			}
			out = next;
		}
		return out;
	}
	
	public boolean focusDragEvent(int x, int y, int mouseType) {
		return true; //TODO: Everything
	}
	
	public void focus() {
		indicator = true;
	}
	
	public void unfocus() {
		indicator = false;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setText(String text) {
		storedText = new StringBuilder();
		storedText.append(text);
		changeText(getText());
		index = text.length();
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	private String getCharAtIndex() {
		if(index == 0) {
			return null;
		}
		return storedText.toString().charAt(index - 1) + "";
	}
	
	public String getText() {
		return storedText.toString();
	}

	public int getIdentity() {
		return hashCode();
	}

	public int getCode() {
		return code;
	}

}
