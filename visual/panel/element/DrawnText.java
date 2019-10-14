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
	/** */
	private int x;
	
	private int y;
	
	private int width;
	
	private int height;
	
	private boolean centered;
	
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
	
	public DrawnText(int inX, int inY, int wid, int hei, int prior, boolean center, String word, Font inFont) {
		x = inX;
		y = inY;
		width = wid;
		height = hei;
		font = inFont;
		message = word;
		centered = center;
		setDrawPriority(prior);
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void drawToScreen(Graphics g) {
		Font save = g.getFont();
		g.setFont(font);
		FontMetrics fM = g.getFontMetrics();
		String[] words = message.split(" ");
		if(centered)
			drawCentered(g, x, y, words, fM);
		else
			drawNonCentered(g, x, y, words, fM);
		g.setFont(save);
	}
	
	private void drawNonCentered(Graphics g, int otX, int otY, String[] words, FontMetrics fM) {
		top:
			for(String s : words) {
				int wid = fM.stringWidth(s + "w");
				if(wid >= width) {
					String[] letters = s.split("");
					for(String l : letters) {
						int letWid = fM.stringWidth(l);
						if(otX + 2 * letWid >= (x + width)) {
							otX = x;
							otY += fM.getHeight();
						}
						if(otY > height) {
							break top;
						}
						g.drawString(l, otX, otY);
						otX += letWid;
					}
					continue top;
				}
				if(otX + wid >= width) {
					otX = x;
					otY += fM.getHeight();	
				}
				if(otY > height) {
					break top;
				}
				g.drawString(s, otX, otY);
				otX += wid;
			}
	}
	
	private void drawCentered(Graphics g, int otX, int otY, String[] words, FontMetrics fM) {
		String currentDraw = "";
		ArrayList<String> finalSet = new ArrayList<String>();
		top:
			for(String s : words) {
				int wid = fM.stringWidth(s + "w");
				if(wid >= width) {
					String[] letters = s.split("");
					for(String l : letters) {
						int letWid = fM.stringWidth(l);
						if(otX + 2 * letWid >= width) {
							otX = x;
							otY += fM.getHeight();
						}
						if(otY > height) {
							break top;
						}
						g.drawString(l, otX, otY);
						otX += letWid;
					}
					continue top;
				}
				if(otX + wid >= (x + width)) {
					otX = x;
					finalSet.add(currentDraw);
					//g.drawString(currentDraw, xLow + ((xHigh - xLow) - outWid)/2, y);
					currentDraw = "";
					otY += fM.getHeight();	
				}
				if(otY > height) {
					break top;
				}
				currentDraw += " " + s;
				otX += wid;
			}
		if(!currentDraw.equals("")){
			finalSet.add(currentDraw);//g.drawString(currentDraw, xLow + ((xHigh - xLow) - fM.stringWidth(currentDraw))/2, y);
		}
		int eachHeight = fM.getHeight();
		int totalHeight = eachHeight * finalSet.size();
		int start = y - (height - totalHeight)/2 + fM.getAscent();
		for(String s : finalSet) {
			int outWid = fM.stringWidth(s);
			g.drawString(s, otX - (width - outWid)/2, start);
			start += eachHeight;
		}
	}
	
}
