package visual.panel.element;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;

public class DrawnText extends Element{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private String message;
	/** */
	private Font font;
	
	private int width;
	
	private int height;
	
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
	
	public DrawnText(int inX, int inY, int wid, int hei, int prior, boolean centerX, boolean centerY, boolean centerText, String word, Font inFont) {
		setX(inX);
		setY(inY);
		width = wid;
		height = hei;
		font = inFont;
		message = word.replaceAll("\n", " \n ");
		centeredX = centerX;
		centeredY = centerY;
		centeredText = centerText;
		setDrawPriority(prior);
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void drawToScreen(Graphics g) {
		Font save = g.getFont();
		g.setFont(font);
		FontMetrics fM = g.getFontMetrics();
		String[] words = message.split(" ");
		if(centeredText)
			drawCentered(g, getX() - (centeredX ? width / 2 : 0), getY() - (centeredY ? height / 2 : 0) + fM.getHeight(), words, fM);
		else
			drawNonCentered(g, getX() - (centeredX ? width / 2 : 0), getY() - (centeredY ? height / 2 : 0) + fM.getHeight(), words, fM);
		g.setFont(save);
	}
	
	private void drawNonCentered(Graphics g, int otX, int otY, String[] words, FontMetrics fM) {
		for(String s : words) {
			int wordWidth = fM.stringWidth(s + "w");
			if(otX + wordWidth >= getX() + width / (centeredX ? 2 : 1) || s.contains("\n")) {
				if(s.contains("\n")) {
					s = s.substring(0, s.indexOf("\n")) + s.substring(s.indexOf("\n"), s.length());
				}
				if(wordWidth < width / 2) {
					otY += fM.getHeight();
					otX = getX() - (centeredX ? width / 2 : 0);
				}
				else {
					String[] letters = s.split("");
					for(String l : letters) {
						int letterWidth = fM.stringWidth(l);
						if(otX + 3 * letterWidth >= getX() + width / (centeredX ? 2 : 1)) {
							otX = getX() - (centeredX ? width / 2 : 0);
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
			if(otY > getY() + height / (centeredY ? 2 : 1)) {
			    break;
			}
		}
	}
	
	private void drawCentered(Graphics g, int otX, int otY, String[] words, FontMetrics fM) {
		StringBuilder sB = new StringBuilder();
		ArrayList<String> lines = new ArrayList<String>();
		for(String s : words) {
			int wordWidth = fM.stringWidth(s + "w");
			if(otX + wordWidth >= getX() + width / (centeredX ? 2 : 1) || s.contains("\n")) {
				if(s.contains("\n")) {
					s = s.substring(0, s.indexOf("\n")) + s.substring(s.indexOf("\n"), s.length());
				}
				if(wordWidth < width / 2) {
					lines.add(sB.toString());
					otY += fM.getHeight();
					otX = getX() - (centeredX ? width / 2 : 0);
					sB = new StringBuilder();
					sB.append(s + " ");
				}
				else {
					String[] letters = s.split("");
					for(String l : letters) {
						int letterWidth = fM.stringWidth(l);
						if(otX + 3 * letterWidth >= getX() + width / (centeredX ? 2 : 1)) {
							lines.add(sB.toString());
							otX = getX() - (centeredX ? width / 2 : 0);
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
			if(otY > getY() + height / (centeredY ? 2 : 1)) {
			    break;
			}
		}
		if(!sB.toString().equals("") && otY <= getY() + height / (centeredY ? 2 : 1)) {
			lines.add(sB.toString());
		}
		int i = 0;
		for(String s : lines) {
			int drawX = getX() - (centeredX ? width / 2 : 0) + (width - fM.stringWidth(s)) / 2;
			int drawY = getY() - (centeredY ? height / 2 : 0) + (height - fM.getHeight() * (lines.size() - 1)) / 2 + i++ * fM.getHeight() + fM.getAscent() / 2;
			if(drawY > getY() + height / (centeredY ? 2 : 1)) {
				break;
			}
			g.drawString(s, drawX, drawY);
		}
	}
	
	public void changeText(String in) {
		message = in.replaceAll("\n", " \n ");
	}
	
}
