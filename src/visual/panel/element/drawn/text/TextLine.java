package visual.panel.element.drawn.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;

/**
 * 
 * Given width, add on words with particular font and keep track of total width
 * 
 * Once reached max width, calculate max height and adjusted position for each word
 * to write at correct position horizontally
 * 
 * Be able to output total height so can externally calculate vertical position for overall
 * line while already knowing internal specific adjustments
 * 
 * Given in-context x, y coords and graphics object, write out text in appropriate font and color
 * 
 */

public class TextLine {

	//** Each string is a sequence of space separated words with the same font*/
	private ArrayList<String> words;
	
	private ArrayList<Font> fonts;
	
	private ArrayList<Color> colors;
	
	private int maxWidth;
	
	private int tallest;
	
	private Toolkit tk;
	
	public TextLine(int wid) {
		words = new ArrayList<String>();
		fonts = new ArrayList<Font>();
		colors = new ArrayList<Color>();
		maxWidth = wid;
		tk = Toolkit.getDefaultToolkit();
	}
	
	/**
	 * 
	 * May in the future want to modify this to have different y positions for how each
	 * word draws if in different fonts; for now, they just have the same baseline which
	 * looks smoother than being technically matching on the same "middle height of word"
	 * 
	 * This function does the drawing of the various strings and fonts in this TextLine
	 * object given the in-context modified x and y positions of where it will be drawn
	 * to.
	 * 
	 * Basically just writes each string as its font and color and correctly iterates the
	 * horizontal position. Resets the font/color back to what it was originally after
	 * doing its work to avoid side effects.
	 * 
	 * @param g
	 * @param modX
	 * @param modY
	 */
	
	public void draw(Graphics g, int modX, int modY) {
		int currX = modX;
		Font saveF = g.getFont();
		Color saveC = g.getColor();
		
		for(int i = 0; i < words.size(); i++) {
			String word = words.get(i);
			Font f = fonts.get(i);
			Color c = colors.get(i);
			FontMetrics fm = tk.getFontMetrics(f);
			int localHeight = fm.getAscent();
			g.setFont(f);
			g.setColor(c);
			String write = word + (i + 1 < words.size() ? " " : "");
			g.drawString(write, currX, modY);
			currX += fm.stringWidth(write);
		}
		
		g.setFont(saveF);
		g.setColor(saveC);
	}
	
	public String tackOnExtra(String next, Font use, Color col) {
		int spareSpace = maxWidth - calculateWidth();
		int indexToAdd = 1;
		FontMetrics fm = tk.getFontMetrics(use);
		checkTallest(fm);
		while(indexToAdd < next.length() && fm.stringWidth(" " + next.substring(0, indexToAdd) + " ") < spareSpace) {
			indexToAdd++;
		}
		words.add(next.substring(0, indexToAdd));
		fonts.add(use);
		colors.add(col);
		return next.substring(indexToAdd);
	}
	
	/**
	 * 
	 * Returns false to denote that this line cannot fit another word (the word
	 * attempted to be added made this line overload the set max width)
	 * 
	 * @param next
	 * @param use
	 * @param col
	 * @return
	 */
	
	public boolean addWord(String next, Font use, Color col) {
		if(words.size() == 0 || !checkMatchesLast(use, col)) {
			FontMetrics fm = tk.getFontMetrics(use);
			checkTallest(fm);
			if(getWidthNotLast() + fm.stringWidth(next) < maxWidth) {
				words.add(next);
				fonts.add(use);
				colors.add(col);
			}
			else {
				return false;
			}
		}
		else {
			String pull = words.get(words.size() - 1) + " " + next;
			FontMetrics fm = tk.getFontMetrics(getLastFont());
			checkTallest(fm);
			if(getWidthNotLast() + fm.stringWidth(pull) < maxWidth) {
				words.set(words.size() - 1, pull);
			}
			else {
				return false;
			}
		}
		return true;
	}
	
	private int getWidthNotLast() {
		int totWidth = 0;
		for(int i = 0; i < words.size() - 1; i++) {
			String w = words.get(i);
			Font f = fonts.get(i);
			FontMetrics fm = tk.getFontMetrics(f);
			totWidth += fm.stringWidth(w + (i + 1 < words.size() ? " " : ""));
		}
		return totWidth;
	}
	
	public int calculateWidth() {
		int totWidth = 0;
		for(int i = 0; i < words.size(); i++) {
			String w = words.get(i);
			Font f = fonts.get(i);
			FontMetrics fm = tk.getFontMetrics(f);
			totWidth += fm.stringWidth(w + (i + 1 < words.size() ? " " : ""));
		}
		return totWidth;
	}
	
	private void checkTallest(FontMetrics fm) {
		if(fm.getAscent() > tallest) {
			tallest = fm.getAscent();
		}
	}
	
	private boolean checkMatchesLast(Font font, Color col) {
		return font.equals(getLastFont()) && col.equals(getLastColor());
	}
	
	public int getTallest() {
		return tallest;
	}
	
	private Font getLastFont() {
		return fonts.get(fonts.size() - 1);
	}
	
	private String getLastString() {
		return words.get(words.size() - 1);
	}
	
	private Color getLastColor() {
		return colors.get(colors.size() - 1);
	}
	
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		for(int i = 0; i < words.size(); i++) {
			out.append("TextLine: " + words.get(i) + " | " + fonts.get(i) + " | " + colors.get(i) + "\n");
		}
		return out.toString();
	}
	
}
