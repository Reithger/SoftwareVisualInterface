package visual.panel.element.drawn.text;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

public class TextSegmentComposite {

	private ArrayList<TextStyleSegment> textParts;
	
	private int currSegment;
	
	private int currWord;
	
	public TextSegmentComposite() {
		textParts = new ArrayList<TextStyleSegment>();
		currSegment = 0;
		currWord = 0;
	}
	
	public TextSegmentComposite(String phrase, Font use, Color col) {
		textParts = new ArrayList<TextStyleSegment>();
		currSegment = 0;
		currWord = 0;
		textParts.add(new TextStyleSegment(phrase, use, col));
	}
	
	public void addTextSegment(TextStyleSegment in) {
		textParts.add(in);
	}
	
	public void addTextSegment(String phrase, Font use, Color col) {
		textParts.add(new TextStyleSegment(phrase, use, col));
	}
	
	public void removeLastSegment() {
		textParts.remove(textParts.size() - 1);
	}
	
	public String getNextWord() {
		return getNextStyle().getText(currWord);
	}
	
	public int getNextWordWidth(String pre, String post) {
		return getNextStyle().getStyleWidth(pre + getNextWord() + post);
	}
	
	public int getNextWordHeight() {
		return getNextStyle().getStyleHeight();
	}
	
	private TextStyleSegment getNextStyle() {
		return textParts.get(currSegment);
	}
	
	public Font getNextWordFont() {
		return textParts.get(currSegment).getFont();
	}
	
	public Color getNextWordColor() {
		return textParts.get(currSegment).getColor();
	}
	
	public void resetPosition() {
		currSegment = 0;
		currWord = 0;
	}
	
	/**
	 * 
	 * Currently this is cutting off the last word so I'm doing a workaround to add a fake
	 * last word that I then remove after processing, not sure why exactly.
	 * 
	 * Or, rather, I do see what the issue is because on the last word it will return false,
	 * but modifying it to be off by one makes it run in an infinite loop for some reason.
	 * 
	 * @return
	 */
	
	public boolean hasMoreWords() {
		if(currSegment + 1 < textParts.size()) {
			return true;
		}
		if(currWord + 1 < textParts.get(currSegment).getNumberWords()) {
			return true;
		}
		return false;
	}
	
	public boolean iterateNextWord() {
		currWord++;
		if(currWord >= textParts.get(currSegment).getNumberWords()) {
			currSegment++;
			currWord = 0;
			if(currSegment >= textParts.size()) {
				return false;
			}
		}
		return true;
	}
	
	
	
}
