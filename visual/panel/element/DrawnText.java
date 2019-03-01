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
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 * 
	 * @param xH
	 * @param yH
	 * @param xL
	 * @param yL
	 * @param prior
	 * @param word
	 * @param inFont
	 */
	
	public DrawnText(int xL, int yL, int xH, int yH, int prior, String word, Font inFont) {
		xHigh = xH;
		yHigh = yH;
		xLow = xL;
		yLow = yL;
		font = inFont;
		message = word;
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
	
}
