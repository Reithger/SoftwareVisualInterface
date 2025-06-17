package visual.panel.element;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;

import misc.Canvas;
import visual.panel.element.drawn.DrawnAnimation;
import visual.panel.element.drawn.DrawnButton;
import visual.panel.element.drawn.DrawnCanvas;
import visual.panel.element.drawn.DrawnImage;
import visual.panel.element.drawn.DrawnLine;
import visual.panel.element.drawn.DrawnRectangle;
import visual.panel.element.drawn.DrawnScrollbar;
import visual.panel.element.drawn.DrawnText;
import visual.panel.element.drawn.DrawnTextEntry;
import visual.panel.element.drawn.text.TextSegmentComposite;
import visual.panel.group.OffsetManager;

public class ElementFactory {
	
	//-- Canvas  ----------------------------------------------
	
	public static Element generateCanvas(int priority, int x, int y, int elemWidth, int elemHeight, Canvas inCanvas, int inCode) {
		return new DrawnCanvas(x, y, priority, elemWidth, elemHeight, inCode, inCanvas);
	}
	
	//-- Scrollbar  -------------------------------------------
	
	public static Element generateScrollbar(int priority, int x, int y, int scrollWid, int scrollHei, int windowOrigin, int windowSize, int code, String groupName, boolean isVert, OffsetManager offset) {
		return new DrawnScrollbar(x, y, scrollWid, scrollHei, windowOrigin, windowSize, offset, code, priority, groupName, isVert);
	}
	
	//-- Image  -----------------------------------------------

	public static Element generateImage(int priority, int x, int y, boolean center, Image img) {
		return new DrawnImage(x, y, priority, center, img);
	}
	
	public static Element generateImage(int priority, int x, int y, int width, int height, boolean center, Image img, boolean proportion) {
		return new DrawnImage(x, y, priority, center, img, width, height, proportion);
	}
	
	//-- Animations  ------------------------------------------
	
	public static Element generateAnimation(int priority, int x, int y, boolean center, int[] period, double scale, Image[] images) {
		return new DrawnAnimation(x, y, priority, period, center, scale, images);
	}
	
	//-- Button  ----------------------------------------------

	public static Element generateButton(int priority, int x, int y, int wid, int hei, int key, boolean centered){
		return new DrawnButton(x, y, wid, hei, priority, centered, key);
	}	
	
	//-- Text  ------------------------------------------------
	
	public static Element generateText(int priority, int x, int y, int width, int height, String phrase, Font font, boolean centeredX, boolean centeredY, boolean centeredText){
		return new DrawnText(x, y, width, height, priority, centeredX, centeredY, centeredText, new TextSegmentComposite(phrase, font, Color.black));
	}
	
	public static Element generateText(int priority, int x, int y, int width, int height, String phrase, Font font, Color col, boolean centeredX, boolean centeredY, boolean centeredText){
		return new DrawnText(x, y, width, height, priority, centeredX, centeredY, centeredText, new TextSegmentComposite(phrase, font, col));
	}
	
	public static Element generateText(int priority, int x, int y, int width, int height, ArrayList<String> phrases, ArrayList<Font> fonts, ArrayList<Color> colors, boolean centeredX, boolean centeredY, boolean centeredText){
		TextSegmentComposite tsc = new TextSegmentComposite();
		for(int i = 0; i < phrases.size(); i++) {
			tsc.addTextSegment(phrases.get(i), fonts.get(i), colors.get(i));
		}
		return new DrawnText(x, y, width, height, priority, centeredX, centeredY, centeredText, tsc);
	}

	public static Element generateTextEntry(int priority, int x, int y, int width, int height, int code, String defaultText, Font font, boolean centeredX, boolean centeredY, boolean centeredText) {
		return new DrawnTextEntry(x, y, width, height, priority, centeredX, centeredY, centeredText, defaultText, font, code);
	}
	
	//-- Shapes  ----------------------------------------------
	
	public static Element generateRectangle(int priority, int x, int y, int width, int height, boolean center, Color fillColor, Color borderColor) {
		return new DrawnRectangle(x, y, width, height, priority, center, fillColor, borderColor);
	}
	
	public static Element generateLine(int priority, int x1, int y1, int x2, int y2, int thickness, Color choice) {
		return new DrawnLine(x1, y1, x2, y2, thickness, priority, choice);
	}
	
}
