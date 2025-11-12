package visual.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;

import misc.Canvas;
import visual.panel.element.ElementFactory;
import visual.panel.group.GroupBoundings;

/**
 * 
 * Loader class for ElementPanel objects; contains numerous functions for adding
 * design objects to the ElementPanel, all of which are subclasses of Element and
 * can be added generically.
 * 
 * This class serves to isolate complicated logic from the ElementPanel class and provide
 * a facade through which a user can customize an ElementPanel without having to directly
 * manipulate it (reduces how much complexity a user has to see when trying to do one
 * specific thing, i.e., adding elements to the panel).
 * 
 * Main issue was just having all the variations of each Element adding function causing
 * extreme bloat to ElementPanel, now we can have various Loaders for different focuses/
 * assumptions to simplify input.
 * 
 * Also just isolates logic so when I need to change or fix something it's not in an incredibly
 * messy environment.
 * 
 * Potentially remove some arguments from all these functions and make them environment
 * details of the ElementLoader that can be changed (if everyone is going to have the
 * same priority or group, why specify every single time).
 * 
 */

public class ElementLoader {

	private static int SCROLLBAR_CODE_VALUE = -50;
	
	private ElementPanel ep;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public ElementLoader(ElementPanel panel) {
		ep = panel;
	}
	
//---  Operations   ---------------------------------------------------------------------------

	//TODO: Add new Elements for defining arbitrary point in coordinate space that don't draw themselves but can be moved/referenced
	//TODO: Add arbitrary Shape Element that has references to an ordered list of arbitrary points and a color it will draw
	
	//-- Canvas  ----------------------------------------------
	
	public Canvas addCanvas(String name, int priority, String frame, int x, int y, int elemWidth, int elemHeight, int canWidth, int canHeight, int inCode) {
		Canvas c = new Canvas(canWidth, canHeight);
		ep.handleAddClickable(name);
		ep.handleAddElement(name, ElementFactory.generateCanvas(priority, x, y, elemWidth, elemHeight, c, inCode), frame);
		return c;
	}
	
	public void addCanvas(String name, int priority, String frame, int x, int y, int elemWidth, int elemHeight, Canvas inCanvas, int inCode) {
		ep.handleAddClickable(name);
		ep.handleAddElement(name, ElementFactory.generateCanvas(priority, x, y, elemWidth, elemHeight, inCanvas, inCode), frame);
	}
	
	//-- Scrollbar  -------------------------------------------
	
	public void addScrollbar(String name, int priority, String frame, int scrollX, int scrollY, int scrollWid, int scrollHei, int windowOrigin, int windowSize, String groupControl, boolean isVert) {
		ep.handleAddClickable(name);
		ep.setGroupWindow(groupControl, windowOrigin, windowSize, isVert);
		ep.handleAddElement(name, ElementFactory.generateScrollbar(priority, scrollX, scrollY, scrollWid, scrollHei, windowOrigin, windowSize, SCROLLBAR_CODE_VALUE--, groupControl, isVert, ep.getOffsetManager(), ep.getBoundingAccess()), frame);
	}
	
	//-- Image  -----------------------------------------------
	
	/**
	 * This method permits the addition of a DrawnImage object to the list of objects
	 * that are drawn during the repaint() of this ElementPanel object at a specified
	 * location; this allows image files to be drawn to this ElementPanel.
	 * 
	 * This method uses a String path to access the Image to be drawn.
	 * 
	 * @param name - String object representing the name of this Element for reference.
	 * @param priority - int value describing the drawing priority of this Element (what should be drawn first/last in cases of overlap)
	 * @param x - int value describing the x coordinate of the center of this Element in the ElementPanel object
	 * @param y - int value describing the y coordinate of the center of this Element in the ElementPanel object
	 * @param path - String object representing the file path that the Image is located at in memory.
	 */
	
	public void addImage(String name, int priority, String frame, int x, int y, boolean center, String path){
		Image img = ep.retrieveImage(path);
		ep.handleAddElement(name, ElementFactory.generateImage(priority, x, y, center, img), frame);
	}
	
	/**
	 * This method permits the addition of a DrawnImage object to the list of objects
	 * that are drawn during the repaint() of this ElementPanel object at a specified
	 * location; this allows image files to be drawn to this ElementPanel.
	 * 
	 * This method uses the provided Image object to be drawn as opposed to a String path.
	 * 
	 * @param name
	 * @param priority
	 * @param x
	 * @param y
	 * @param center
	 * @param img
	 */
	
	public void addImage(String name, int priority, String frame, int x, int y, boolean center, Image img) {
		ep.handleAddElement(name, ElementFactory.generateImage(priority, x, y, center, img), frame);
	}
	
	/**
	 * This method permits the addition of a DrawnImage object to the list of objects
	 * that are drawn during the repaint() of this ElementPanel object at a specified
	 * location; this allows image files to be drawn to this ElementPanel.
	 * 
	 * This method uses a String path to access the Image to be drawn.
	 * 
	 * @param name - String object representing the name of this Element for reference.
	 * @param priority - int value describing the drawing priority of this Element (what should be drawn first/last in cases of overlap)
	 * @param x - int value describing the x coordinate of the center of this Element in the ElementPanel object
	 * @param y - int value describing the y coordinate of the center of this Element in the ElementPanel object
	 * @param path - String object representing the file path that the Image is located at in memory.
	 */
	
	public void addImage(String name, int priority, String frame, int x, int y, boolean center, String path, double scale){
		Image img = ep.retrieveImage(path);
		ep.handleAddElement(name, ElementFactory.generateImage(priority, x, y, (int)(img.getWidth(null) * scale), (int)(img.getHeight(null) * scale), center, img, true), frame);
	}
	
	/**
	 * This method permits the addition of a DrawnImage object to the list of objects
	 * that are drawn during the repaint() of this ElementPanel object at a specified
	 * location; this allows image files to be drawn to this ElementPanel.
	 * 
	 * This method uses the provided Image object to be drawn as opposed to a String path.
	 * 
	 * @param name
	 * @param priority
	 * @param x
	 * @param y
	 * @param center
	 * @param img
	 */
	
	public void addImage(String name, int priority, String frame, int x, int y, boolean center, Image img, double scale) {
		ep.handleAddElement(name, ElementFactory.generateImage(priority, x, y, (int)(img.getWidth(null) * scale), (int)(img.getHeight(null) * scale), center, img, true), frame);
	}

	/**
	 * This method permits the addition of a DrawnImage object to the list of objects
	 * that are drawn during the repaint() of this ElementPanel object at a specified
	 * location; this allows image files to be drawn to this ElementPanel.
	 * 
	 * This method uses a String path to access the Image to be drawn.
	 * 
	 * @param name - String object representing the name of this Element for reference.
	 * @param priority - int value describing the drawing priority of this Element (what should be drawn first/last in cases of overlap)
	 * @param x - int value describing the x coordinate of the center of this Element in the ElementPanel object
	 * @param y - int value describing the y coordinate of the center of this Element in the ElementPanel object
	 * @param path - String object representing the file path that the Image is located at in memory.
	 */
	
	public void addImage(String name, int priority, String frame, int x, int y, int width, int height, boolean center, String path, boolean proportion){
		Image img = ep.retrieveImage(path);
		ep.handleAddElement(name, ElementFactory.generateImage(priority, x, y, width, height, center, img, proportion), frame);
	}
	
	/**
	 * This method permits the addition of a DrawnImage object to the list of objects
	 * that are drawn during the repaint() of this ElementPanel object at a specified
	 * location; this allows image files to be drawn to this ElementPanel.
	 * 
	 * This method uses the provided Image object to be drawn as opposed to a String path.
	 * 
	 * @param name
	 * @param priority
	 * @param x
	 * @param y
	 * @param center
	 * @param img
	 */
	
	public void addImage(String name, int priority, String frame, int x, int y, int width, int height, boolean center, Image img, boolean proportion) {
		ep.handleAddElement(name, ElementFactory.generateImage(priority, x, y, width, height, center, img, proportion), frame);
	}
	
	//-- Animations  ------------------------------------------
	
	public void addAnimation(String name, int priority, String frame, int x, int y, boolean center, int period, double scale, String[] frames) {
		Image[] rec = new Image[frames.length];
		for(int i = 0; i < rec.length; i++) {
			rec[i] = ep.retrieveImage(frames[i]);
		}
		int[] periods = new int[frames.length];
		for(int i = 0; i < periods.length; i++) {
			periods[i] = period;
		}
		ep.handleAddElement(name, ElementFactory.generateAnimation(priority, x, y, center, periods, scale, rec), frame);
	}
	
	public void addAnimation(String name, int priority, String frame, int x, int y, boolean center, int[] period, double scale, String[] frames) {
		Image[] rec = new Image[frames.length];
		for(int i = 0; i < rec.length; i++) {
			rec[i] = ep.retrieveImage(frames[i]);
		}
		ep.handleAddElement(name, ElementFactory.generateAnimation(priority, x, y, center, period, scale, rec), frame);
	}
	
	public void addAnimation(String name, int priority, String frame, int x, int y, boolean center, int period, double scale, Image[] frames) {
		int[] periods = new int[frames.length];
		for(int i = 0; i < periods.length; i++) {
			periods[i] = period;
		}
		ep.handleAddElement(name, ElementFactory.generateAnimation(priority, x, y, center, periods, scale, frames), frame);
	}
	
	public void addAnimation(String name, int priority, String frame, int x, int y, boolean center, int[] period, double scale, Image[] frames) {
		ep.handleAddElement(name, ElementFactory.generateAnimation(priority, x, y, center, period, scale, frames), frame);
	}
	
	//-- Button  ----------------------------------------------
	
	/**
	 * This method permits the addition of an interactive DrawnButton that instructs this
	 * ElementPanel to produce the provided 'key' value when the described region is clicked
	 * on; this value is given to clickEvent() when it runs.
	 * 
	 * Effectively you define a rectangular region of the screen that, when clicked, causes
	 * clickEvent(key) to be called, allowing for interactivity between the user and this
	 * ElementPanel object.
	 * 
	 * This method does not draw a rectangle, creating an invisible region of the screen that
	 * can detect interaction. Other Element objects can be drawn beneath this to create custom
	 * buttons without having to contend with a drawn rectangular region.
	 * 
	 * @param name - String object representing the name of this Element for reference.
	 * @param priority - int value describing the drawing priority of this Element (what should be drawn first/last in cases of overlap)
	 * @param x - int value describing the x coordinate of the upper-left corner of this DrawnButton object
	 * @param y - int value describing the y coordinate of the upper-left corner of this DrawnButton object
	 * @param wid - int value describing the width of this DrawnButton object
	 * @param hei - int value describing the height of this DrawnButton object
	 * @param key - int value describing the value that is generated when this DrawnButton object is clicked
	 */

	public void addButton(String name, int priority, String frame, int x, int y, int wid, int hei, int key, boolean centered){
		ep.handleAddClickable(name);
		ep.handleAddElement(name, ElementFactory.generateButton(priority, x, y, wid, hei, key, centered), frame);
	}	
	
	//-- Text  ------------------------------------------------
	
	/**
	 * This method permits the drawing of a provided String to the ElementPanel
	 * at a defined location with a desired font.
	 * 
	 * @param name - String object representing the name of this Element for reference.
	 * @param priority - int value describing the drawing priority of this Element (what should be drawn first/last in cases of overlap)
	 * @param x - int value describing the x coordinate of the upper-left corner of this DrawnText object
	 * @param y - int value describing the y coordinate of the upper-left corner of this DrawnText object
	 * @param width - 
	 * @param height - 
	 * @param phrase - String object representing the string of characters to be drawn to this ElementPanel
	 * @param font - Font object describing the font with which to draw the provided String phrase
	 */
	
	public void addText(String name, int priority, String frame, int x, int y, int width, int height, String phrase, Font font, boolean centeredX, boolean centeredY, boolean centeredText){
		ep.handleAddElement(name, ElementFactory.generateText(priority, x, y, width, height, phrase, font, centeredX, centeredY, centeredText), frame);
	}
	
	public void addText(String name, int priority, String frame, int x, int y, int width, int height, String phrase, Font font, Color col, boolean centeredX, boolean centeredY, boolean centeredText){
		ep.handleAddElement(name, ElementFactory.generateText(priority, x, y, width, height, phrase, font, col, centeredX, centeredY, centeredText), frame);
	}
	
	public void addText(String name, int priority, String frame, int x, int y, int width, int height, ArrayList<String> phrases, ArrayList<Font> fonts, ArrayList<Color> colors, boolean centeredX, boolean centeredY, boolean centeredText){
		ep.handleAddElement(name, ElementFactory.generateText(priority, x, y, width, height, phrases, fonts, colors, centeredX, centeredY, centeredText), frame);
	}

	/**
	 * This method permits a region of the screen to be relegated to receiving user input;
	 * when this has been the most recently selected Element in this ElementPanel, key-input
	 * will be appended to the String attached to the DrawnTextArea object (regular rules
	 * of interaction for key-input is ignored until this is no longer selected.)
	 * 
	 * The String attached to the DrawnTextArea object can be accessed by supplying the name
	 * given to this method to the ElementPanel's getElementStoredText() method. 
	 * 
	 * @param name - String object representing the name of this Element for reference.
	 * @param priority - int value describing the drawing priority of this Element (what should be drawn first/last in cases of overlap)
	 * @param x - int value describing the x coordinate of the upper-left corner of this DrawnTextArea object
	 * @param y - int value describing the y coordinate of the upper-left corner of this DrawnTextArea object
	 * @param width - int value describing the width of this DrawnButton object
	 * @param height - int value describing the height of this DrawnButton object
	 * @param key - int value describing the value that is generated when this DrawnImageButton object is clicked
	 * @param font - Font object describing the font with which to draw the provided String phrase
	 */
	
	public void addTextEntry(String name, int priority, String frame, int x, int y, int width, int height, int code, String defaultText, Font font, boolean centeredX, boolean centeredY, boolean centeredText) {
		ep.handleAddClickable(name);
		ep.handleAddElement(name, ElementFactory.generateTextEntry(priority, x, y, width, height, code, defaultText, font, centeredX, centeredY, centeredText), frame);
	}
	
	//-- Shapes  ----------------------------------------------
	
	/**
	 * This method draws a colored Rectangle to this ElementPanel object.
	 * 
	 * @param name - String object representing the name of this Element for reference.
	 * @param priority - int value describing the drawing priority of this Element (what should be drawn first/last in cases of overlap)
	 * @param x - int value describing the x coordinate of the upper-left corner of this Element object
	 * @param y - int value describing the y coordinate of the upper-left corner of this Element object
	 * @param width - int value describing the width of this Element object
	 * @param height - int value describing the height of this Element object
	 * @param col - Color object describing the color with which to draw this DrawnRectangle Element
	 */
	
	public void addRectangle(String name, int priority, String frame, int x, int y, int width, int height, boolean center, Color col) {
		ep.handleAddElement(name, ElementFactory.generateRectangle(priority, x, y, width, height, center, col, col), frame);
	}
	
	/**
	 * This method draws a Rectangle to this ElementPanel object, with specified fill and
	 * outline colors.
	 * 
	 * @param name - String object representing the name of this Element for reference.
	 * @param priority - int value describing the drawing priority of this Element (what should be drawn first/last in cases of overlap)
	 * @param x - int value describing the x coordinate of the upper-left corner of this Element object
	 * @param y - int value describing the y coordinate of the upper-left corner of this Element object
	 * @param width - int value describing the width of this Element object
	 * @param height - int value describing the height of this Element object
	 * @param fillColor - Color object describing the color with which to fill this DrawnRectangle Element
	 * @param borderColor - Color object describing the color with which to draw the outline of this DrawnRectangle Element
	 */
	
	public void addRectangle(String name, int priority, String frame, int x, int y, int width, int height, boolean center, Color fillColor, Color borderColor) {
		ep.handleAddElement(name, ElementFactory.generateRectangle(priority, x, y, width, height, center, fillColor, borderColor), frame);
	}

	/**
	 * This method draws a Line to this ElementPanel object, with specified start and end points in an (x, y) coordinate
	 * system with a specified thickness and Color.
	 * 
	 * @param name
	 * @param priority
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param thickness
	 * @param choice
	 */
	
	public void addLine(String name, int priority, String frame, int x1, int y1, int x2, int y2, int thickness, Color choice) {
		ep.handleAddElement(name, ElementFactory.generateLine(priority, x1, y1, x2, y2, thickness, choice), frame);
	}
	
	
}
