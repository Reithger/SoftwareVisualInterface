package visual.panel.element.drawn.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;

public class TextStyleSegment {

	private String[] text;
	
	private Font font;
	
	private Color color;
	
	private FontMetrics fm;
	
	private int fontHeight;
	
	public TextStyleSegment(String in, Font inFont) {
		text = in.replaceAll("\n", " \n ").split(" ");
		font = inFont;
		color = Color.black;
		fm = Toolkit.getDefaultToolkit().getFontMetrics(font);
		fontHeight = fm.getAscent();
	}
	
	public TextStyleSegment(String in, Font inFont, Color inColor) {
		text = in.replaceAll("\n", " \n ").trim().split(" ");
		font = inFont;
		color = inColor;
		fm = Toolkit.getDefaultToolkit().getFontMetrics(font);
		fontHeight = fm.getAscent();
	}
	
	public int getStyleHeight() {
		return fontHeight;
	}
	
	public int getStyleWidth(String in) {
		return fm.stringWidth(in);
	}
	
	public String[] getText() {
		return text;
	}
	
	public String getText(int index) {
		return text[index];
	}
	
	public int getNumberWords() {
		return text.length;
	}
	
	public Font getFont() {
		return font;
	}
	
	public Color getColor() {
		return color;
	}
	
}
