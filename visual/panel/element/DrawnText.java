package visual.panel.element;

import java.awt.Font;
import java.awt.Graphics;

public class DrawnText extends Element{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private String message;
	/** */
	private Font font;
	/** */
	private int xLocation;
	/** */
	private int yLocation;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param word
	 * @param inFont
	 */
	
	public DrawnText(int x, int y, int prior, String word, Font inFont) {
		xLocation = x;
		yLocation = y;
		font = inFont;
		message = word;
		setDrawPriority(prior);
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void drawToScreen(Graphics g) {
		Font save = g.getFont();
		g.setFont(font);
		g.drawString(message, xLocation, yLocation);
		g.setFont(save);
	}
	
}
