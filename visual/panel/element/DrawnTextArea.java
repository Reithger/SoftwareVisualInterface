package visual.panel.element;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;

import input.ClickRegionRectangle;
import input.Detectable;

public class DrawnTextArea extends Element implements Clickable, TextStorage{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private String storedText;
	/** */
	private Font font;
	/** */
	private int x;
	
	private int y;
	
	private int width;
	
	private int height;
	
	private int code;
	
	private boolean centeredX;
	
	private boolean centeredY;
	
	private boolean centeredText;
	
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
	
	public DrawnTextArea(int inX, int inY, int wid, int hei, int prior, boolean centerX, boolean centerY, boolean centerText, String word, Font inFont, int inCode) {
		x = inX;
		y = inY;
		width = wid;
		height = hei;
		font = inFont;
		storedText = word;
		centeredX = centerX;
		centeredY = centerY;
		centeredText = centerText;
		code = inCode;
		setDrawPriority(prior);
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void drawToScreen(Graphics g) {
		Font save = g.getFont();
		g.setFont(font);
		FontMetrics fM = g.getFontMetrics();
		String[] words = storedText.split(" ");
		if(centeredText)
			drawCentered(g, x - (centeredX ? width / 2 : 0), y - (centeredY ? height / 2 : 0) + fM.getHeight(), words, fM);
		else
			drawNonCentered(g, x - (centeredX ? width / 2 : 0), y - (centeredY ? height / 2 : 0) + fM.getHeight(), words, fM);
		g.setFont(save);
	}
	
	private void drawNonCentered(Graphics g, int otX, int otY, String[] words, FontMetrics fM) {
		for(String s : words) {
			int wordWidth = fM.stringWidth(s + "w");
			if(otX + wordWidth >= x + width / (centeredX ? 2 : 1)) {
				if(wordWidth < width / 2) {
					otY += fM.getHeight();
					otX = x - (centeredX ? width / 2 : 0);
				}
				else {
					String[] letters = s.split("");
					for(String l : letters) {
						int letterWidth = fM.stringWidth(l);
						if(otX + 3 * letterWidth >= x + width / (centeredX ? 2 : 1)) {
							otX = x - (centeredX ? width / 2 : 0);
							otY += fM.getHeight();
						}
						g.drawString(l, otX, otY);
						otX += letterWidth;
					}
					g.drawString(" ", otX, otY);
					otX += fM.stringWidth(" ");
				}
			}
			else {
				g.drawString(s + " ", otX, otY);
				otX += fM.stringWidth(s + " ");
			}
			if(otY > y + height / (centeredY ? 2 : 1)) {
			    break;
			}
		}
	}
	
	private void drawCentered(Graphics g, int otX, int otY, String[] words, FontMetrics fM) {
		StringBuilder sB = new StringBuilder();
		ArrayList<String> lines = new ArrayList<String>();
		for(String s : words) {
			int wordWidth = fM.stringWidth(s + "w");
			if(otX + wordWidth >= x + width / (centeredX ? 2 : 1)) {
				if(wordWidth < width / 2) {
					lines.add(sB.toString());
					otY += fM.getHeight();
					otX = x - (centeredX ? width / 2 : 0);
					sB = new StringBuilder();
					sB.append(s + " ");
				}
				else {
					String[] letters = s.split("");
					for(String l : letters) {
						int letterWidth = fM.stringWidth(l);
						if(otX + 3 * letterWidth >= x + width / (centeredX ? 2 : 1)) {
							lines.add(sB.toString());
							otX = x - (centeredX ? width / 2 : 0);
							otY += fM.getHeight();
							sB = new StringBuilder();
						}
						sB.append(l);
						otX += letterWidth;
					}
					sB.append(" ");
					otX += fM.stringWidth(" ");
				}
			}
			else {
				sB.append(s + " ");
				otX += fM.stringWidth(s + " ");
			}
			if(otY > y + height / (centeredY ? 2 : 1)) {
			    break;
			}
		}
		if(!sB.toString().equals("")) {
			lines.add(sB.toString());
		}
		int i = 0;
		for(String s : lines) {
			g.drawString(s, x - (centeredX ? width / 2 : 0) + (width - fM.stringWidth(s)) / 2, y - (centeredY ? height / 2 : 0) + (height - fM.getHeight() * lines.size()) / 2 + i++ * fM.getHeight());
		}
	}
	
	@Override
	public Detectable getDetectionRegion() {
		return new ClickRegionRectangle(x - (centeredX ? width / 2 : 0), y - (centeredY ? width / 2 : 0), width, height, code);
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
