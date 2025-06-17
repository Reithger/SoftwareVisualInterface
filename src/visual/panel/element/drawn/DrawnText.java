package visual.panel.element.drawn;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import visual.panel.element.Element;
import visual.panel.element.drawn.text.TextLine;
import visual.panel.element.drawn.text.TextSegmentComposite;

public class DrawnText extends Element{

//---  Instance Variables   -------------------------------------------------------------------
	
	private TextSegmentComposite words;
	
	private volatile ArrayList<TextLine> lines;
	
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
	
	public DrawnText(int inX, int inY, int wid, int hei, int prior, boolean centerX, boolean centerY, boolean centerText, TextSegmentComposite text) {
		setX(inX);
		setY(inY);
		width = wid;
		height = hei;
		words = text;
		centeredX = centerX;
		centeredY = centerY;
		centeredText = centerText;
		setDrawPriority(prior);
		processTextComposite();
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void drawToScreen(Graphics g, int inOffsetX, int inOffsetY) {
		Font save = g.getFont();
		offsetX = inOffsetX;
		offsetY = inOffsetY;
		int useX = getXDraw() - (centeredText ? width / 2 : 0);
		int useY = getYDraw() - (centeredText ? height / 2 : 0);
		int totalHeight = totalTextLineHeight();
		if(lines.size() > 0) {
			int posY = useY + lines.get(0).getTallest() +  (centeredY ? (height - totalHeight) / 2 : 0);
			posY = posY < inOffsetY ? inOffsetY : posY;
			for(TextLine tl : lines) {
				tl.draw(g, useX + (centeredX ? (width - tl.calculateWidth()) / 2 : 0), posY);
				posY += tl.getTallest();
			}
		}
		g.setFont(save);
	}
	
	private int totalTextLineHeight() {
		int out = 0;
		
		for(TextLine tl : lines) {
			out += tl.getTallest();
		}
		
		return out;
	}
	
	/**
	 * 
	 * Initialize to first word of the composition of text style segments
	 * 
	 * Attempt to add each distinct word to a TextLine which keeps track of the current width of its variously formatted strings
	 * 
	 * If it can add the word without an issue (addWord function returns true), iterate to next word
	 * 
	 * If not, consider that TextLine done and add it to the list; make a new TextLine object
	 * 
	 * Attempt to add the word that didn't fit before to the new TextLine object
	 * 
	 * If it fits, iterate to next word
	 * 
	 * If it does not fit, (the string is too long for a single line of text), chop off as much as would fit to the TextLine we previously
	 *   added to the list (if a string is going to be broken up across multiple lines, might as well use up spare space in the previous TextLine)
	 *   
	 * Then progressively chop off as much text as will fit onto each TextLine and add that TextLine to the list. If there is still more string left
	 *   over after a chopping, make a new TextLine to hold as much of the string as it can fit.
	 *  
	 * Do not immediately add the TextLine that took the last chopping of the string to the list, it may have room for more words.
	 *   
	 * Once the string is empty, iterate to the next word and repeat from the top.
	 * 
	 * This gives us a list of TextLine objects that we can instruct to draw, making this a one-time operation.
	 *   
	 * 
	 */
	
	private void processTextComposite() {
		words.resetPosition();
		words.addTextSegment(" ", new Font("Sans Serif", Font.BOLD, 1), Color.black);
		lines = new ArrayList<TextLine>();
		TextLine toAdd = new TextLine(width);
		while(words.hasMoreWords()) {
			String wordUse = words.getNextWord();
			if(words.getNextWord().equals("\n")) {
				wordUse = " ";
			}
			// If the next word doesn't fit in the current TextLine we're adding
			if(!toAdd.addWord(wordUse, words.getNextWordFont(), words.getNextWordColor())) {
				lines.add(toAdd);
				toAdd = new TextLine(width);
				// If the next word doesn't fit in an empty TextLine
				if(!toAdd.addWord(wordUse, words.getNextWordFont(), words.getNextWordColor())) {
					// Slice off as much of the next word into the previous TextLine as will fit
					String pull = lines.get(lines.size() - 1).tackOnExtra(words.getNextWord(), words.getNextWordFont(), words.getNextWordColor());
					while(!pull.equals("")) {
						System.out.println("B: " + pull);
						pull = toAdd.tackOnExtra(pull, words.getNextWordFont(), words.getNextWordColor());
						System.out.println("A: " + pull);
						if(!pull.equals("")) {
							lines.add(toAdd);
							toAdd = new TextLine(width);
						}
					}
					words.iterateNextWord();
				}
				else {
					words.iterateNextWord();
				}
			}
			else {
				if(words.getNextWord().equals("\n")) {
					lines.add(toAdd);
					toAdd = new TextLine(width);
				}
				words.iterateNextWord();
			}
		}
		if(toAdd.calculateWidth() > 0)
			lines.add(toAdd);
		
		words.removeLastSegment();
	}
	
	/*
	private void drawText(Graphics g, int otX, int otY) {
		ArrayList<String> lines = new ArrayList<String>();
		String lin = "";
		for(int i = 0; i < words.length; i++) {
			String s = words[i];
		int tallest = 0;
		int wid = 0;
		while(words.hasMoreWords()) {
			String s = words.getNextWord();
			if(s.equals("\n")) {
				lines.add(lin);
				lin = " ";
			}
			else if(words.getNextWordWidth(lin + (lin.equals("") ? "" : " "), "") < width) {
				lin += (lin.equals("") ? "" : " ") + s;
				if(words.getNextWordHeight() > tallest) {
					tallest = words.getNextWordHeight();
				}
			}
			else {
				if(lin.equals("")) {
					String holdOver = s;
					while(fM.stringWidth(holdOver) > width) {
						String holdOn = "";
						for(char l : holdOver.toCharArray()) {
							if(lin.equals("") || fM.stringWidth(lin + l) < width) {
								lin += l;
								if(words.getNextWordHeight() > tallest) {
									tallest = words.getNextWordHeight();
								}
							}
							else {
								holdOn += l;
							}
						}
						holdOver = holdOn;
						if(!holdOver.equals("")) {
							lines.add(lin);
							lin = "";
							tallest = 0;
						}
					}
				}
				else {
					lines.add(lin);
					lin = "";
					tallest = 0;
					continue;
				}
			}
			words.iterateNextWord();
		}
		if(!lin.equals("")) {
			lines.add(lin);
		}
		int posY = otY + fM.getAscent() +  (centeredY ? (height - lines.size() * fM.getHeight()) / 2 : 0);
		posY = posY < otY ? otY : posY;
		for(int i = 0; i < lines.size(); i++) {
			String s = lines.get(i);
			g.drawString(s, otX + (centeredX ? (width - fM.stringWidth(s)) / 2 : 0), posY);
			posY += heights.get(i);
		}
	}
	*/
	
	public void changeText(TextSegmentComposite tsc) {
		words = tsc;
		processTextComposite();
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
	
	protected boolean getCentered() {
		return centeredText;
	}
	
	protected int getWidth() {
		return width;
	}
	
	protected int getHeight() {
		return height;
	}
	
	@Override
	public int getMinimumX() {
		return getX() - (centeredText ? width / 2 : 0);
	}

	@Override
	public int getMaximumX() {
		return width + getMinimumX();
	}

	@Override
	public int getMinimumY() {
		return getY() - (centeredText ? height / 2 : 0);
	}

	@Override
	public int getMaximumY() {
		return height + getMinimumY();
	}
	
}
