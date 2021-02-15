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
	
	private int offsetX;
	
	private int offsetY;
	
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
		changeText(word);
		centeredX = centerX;
		centeredY = centerY;
		centeredText = centerText;
		setDrawPriority(prior);
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void drawToScreen(Graphics g, int inOffsetX, int inOffsetY) {
		Font save = g.getFont();
		offsetX = inOffsetX;
		offsetY = inOffsetY;
		g.setFont(font);
		FontMetrics fM = g.getFontMetrics();
		String[] words = message.split(" ");
		drawText(g, getXDraw() - (centeredText ? width / 2 : 0), getYDraw() - (centeredText ? height / 2 : 0), words, fM);
		g.setFont(save);
	}
	
	private void drawText(Graphics g, int otX, int otY, String[] words, FontMetrics fM) {
		ArrayList<String> lines = new ArrayList<String>();
		String lin = "";
		for(int i = 0; i < words.length; i++) {
			String s = words[i];
			if(s.equals("\n")) {
				lines.add(lin);
				lin = "";
			}
			else if(fM.stringWidth(lin + (lin.equals("") ? "" : " ") + s) < width) {
				lin += (lin.equals("") ? "" : " ") + s;
			}
			else {
				if(lin.equals("")) {
					String holdOver = s;
					while(fM.stringWidth(holdOver) > width) {
						String holdOn = "";
						for(char l : holdOver.toCharArray()) {
							if(lin.equals("") || fM.stringWidth(lin + l) < width) {
								lin += l;
							}
							else {
								holdOn += l;
							}
						}
						holdOver = holdOn;
						if(!holdOver.equals("")) {
							lines.add(lin);
							lin = "";
						}
					}
				}
				else {
					lines.add(lin);
					lin = "";
					i--;
				}
			}
		}
		if(!lin.equals("")) {
			lines.add(lin);
		}
		int posY = otY + fM.getAscent() +  (centeredY ? (height - lines.size() * fM.getHeight()) / 2 : 0);
		posY = posY < otY ? otY : posY;
		for(String s : lines) {
			g.drawString(s, otX + (centeredX ? (width - fM.stringWidth(s)) / 2 : 0), posY);
			posY += fM.getHeight();
		}
	}
	
	public void changeText(String in) {
		message = in.replaceAll("\n", " \n ");
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public int getXDraw() {
		return getX() + offsetX;
	}
	
	public int getYDraw() {
		return getY() + offsetY;
	}
	
	protected boolean getCenterX() {
		return centeredX;
	}
	
	protected boolean getCenterY() {
		return centeredY;
	}
	
	protected int getWidth() {
		return width;
	}
	
	protected int getHeight() {
		return height;
	}
	
	@Override
	public int getMinimumX() {
		return centeredX ? getX() - (centeredX ? width / 2 : 0) : getX();
	}

	@Override
	public int getMaximumX() {
		return width + (centeredX ? getX() - (centeredX ? width / 2 : 0) : getX());
	}

	@Override
	public int getMinimumY() {
		return centeredY ? getY() - (centeredY ? height / 2 : 0) : getY();
	}

	@Override
	public int getMaximumY() {
		return height + (centeredY ? getY() - (centeredY ? height / 2 : 0) : getY());
	}
	
}
