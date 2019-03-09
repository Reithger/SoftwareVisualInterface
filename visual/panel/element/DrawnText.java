package visual.panel.element;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class DrawnText extends Element{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private String message;
	/** */
	private Font font;
	/** */
	private int xHigh;
	/** */
	private int yHigh;
	/** */
	private int xLow;
	/** */
	private int yLow;
	
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
	
	public DrawnText(int xL, int yL, int xH, int yH, int prior, boolean center, String word, Font inFont) {
		xHigh = xH;
		yHigh = yH;
		xLow = xL;
		yLow = yL;
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
		int x = xLow;
		int y = yLow + fM.getHeight();
		String[] words = message.split(" ");
		if(centered)
			drawCentered(g, x, y, words, fM);
		else
			drawNonCentered(g, x, y, words, fM);
		g.setFont(save);
	}
	
	private void drawNonCentered(Graphics g, int x, int y, String[] words, FontMetrics fM) {
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
	}
	
	private void drawCentered(Graphics g, int x, int y, String[] words, FontMetrics fM) {
		String currentDraw = "";
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
					int outWid = fM.stringWidth(currentDraw);
					System.out.println(outWid);
					g.drawString(currentDraw, xLow + ((xHigh - xLow) - outWid)/2, y);
					currentDraw = "";
					y += fM.getHeight();	
				}
				if(y > yHigh) {
					break top;
				}
				currentDraw += " " + s;
				//g.drawString(s, x, y);
				x += wid;
			}
		if(!currentDraw.equals("")){
			g.drawString(currentDraw, xLow + ((xHigh - xLow) - fM.stringWidth(currentDraw))/2, y);
		}
	}
	
}
