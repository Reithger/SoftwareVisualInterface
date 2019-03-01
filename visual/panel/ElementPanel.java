package visual.panel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.io.File;
import java.awt.*;
import javax.imageio.ImageIO;
import input.Detectable;
import visual.panel.element.*;

/**
 * This class implements the Panel abstract class to provide a suite of drawing tools and interactivity
 * to the programmer via the element package, as well as handling some low-level complexities that arise.
 * 
 * If using the SWI library, you should not need to look any deeper than this unless you wish to change
 * base functionality. All features are provided by methods describing those features.
 * 
 * @author Mac Clevinger
 *
 */

public class ElementPanel extends Panel{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** HashMap that assigns a name to objects that can be drawn to the screen; each repaint uses this list to draw to the screen*/
	private HashMap<String, Element> drawList;
	/** HashMap that assigns a name to defined regions of the screen that generate a specified code upon interaction*/
	private HashMap<String, Detectable> clickList;
	/** Clickable object representing the most recently selected interactive Element by the User for directing Key Inputs towards*/
	private Clickable focusElement;

//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * Constructor for objects of the ElementPanel type that uses the Panel abstract class' initiate()
	 * method and initializes the storage of Element and Image objects.
	 * 
	 * @param x - int value representing the x coordinate of this ElementPanel object's origin in its containing Frame
	 * @param y - int value representing the y coordinate of this ElementPanel object's origin in its containing Frame
	 * @param width - int value representing the width of this ElementPanel object
	 * @param height - int value representing the height of this ElementPanel object
	 */
	
	public ElementPanel(int x, int y, int width, int height){
		super();
		initiate(x, y, width, height);
		drawList = new HashMap<String, Element>();
		clickList = new HashMap<String, Detectable>();
	}
	
//---  Operations   ---------------------------------------------------------------------------

	/**
	 * Certain UI Elements have behavior that requires a communication between higher-level
	 * objects, so that is handled before the clickBehaviour() method which the programmer
	 * can overwrite to define the interactive behavior of this Panel.
	 * 
	 */
	
	public void clickEvent(int event){
		focusElement = null;
		top:
		for(Detectable d : clickList.values()) {
			if(d.getCode() == event) {
				for(String s : drawList.keySet()) {
					if(clickList.get(s) != null && clickList.get(s).equals(d)) {
						focusElement = (Clickable)drawList.get(s);
						break top;
					}
				}
			}
		}
		clickBehaviour(event);
	}
	
	/**
	 * This method exists to be overwritten on an object-by-object basis by the programmer
	 * to define the interactive behavior of this ElementPanel object.
	 * 
	 * @param event - int value representing the code generated by user interaction (mouse click on region of screen)
	 */
	
	public void clickBehaviour(int event) {
		System.out.println("Overwrite this method");
	}
	
	/**
	 * Certain UI Elements have behavior that requires a communication between higher-level
	 * objects, so that is handled before the keyBehaviour() method which the programmer
	 * can overwrite to define the interactive behavior of this Panel.
	 * 
	 */
	
	public void keyEvent(char event){
		if(focusElement != null) {
			if(!focusElement.focusEvent(event)) {
				return;
			}
		}
		keyBehaviour(event);
	}
	
	/**
	 * This method exists to be overwritten on an object-by-object basis by the programmer
	 * to define the interactive behavior of this ElementPanel object.
	 * 
	 * @param event - char value representing the key that the user pressed
	 */
	
	public void keyBehaviour(char event) {
		System.out.println("Overwrite this method");
	}
	
	/**
	 * This method iterates over all Element objects that have been added to this
	 * ElementPanel object and calls their individual methods to draw themselves.
	 * 
	 * Elements are sorted ahead of time according to their priority values; the
	 * lower the value, the sooner it is drawn (higher values are drawn on top of
	 * lower values.)
	 * 
	 */
	
	public void paintComponent(Graphics g) {
		ArrayList<Element> elements = new ArrayList<Element>(drawList.values());
		Collections.sort(elements);
		for(Element d : elements) {
			d.drawToScreen(g);
		}
	}
	
	/**
	 * This method reassigns the regions of the screen that detect user input and
	 * trigger a clickEvent() with the corresponding generated code value.
	 * 
	 * It first removes all Detection regions from this ElementPanel, and then calls
	 * on all Clickable objects to generate their Detectable click regions to be added
	 * to this ElementPanel. 
	 * 
	 */
	
	private void updateClickRegions() {
		resetDetectionRegions();
		for(Detectable d : clickList.values()) {
			if(!addClickRegion(d)) {
				removeClickRegion(d.getCode());
				addClickRegion(d);
			}
		}
	}

//---  Getter Methods   -----------------------------------------------------------------------

	/**
	 * This method requests one of the Elements attributed to this ElementPanel to be returned,
	 * defined by the provided String object representing a name associated to that Element.
	 * 
	 * @param name - String object representing the name of the Element being requested.
	 * @return - Returns an Element object specified by a provided name
	 */
	
	public Element getElement(String name) {
		return drawList.get(name);
	}
	
	/**
	 * This method requests the text stored by the Element object specified by the
	 * provided String name under the assumption that the specified Element object
	 * implements the TextStorage interface which permits the text to be retrieved.
	 * 
	 * If the specified Element object does not implement the interface, an error is
	 * printed to the terminal.
	 * 
	 * This is intended for retrieving user input from Elements that can receive text-based
	 * input from the user, via such Elements as DrawnTextArea, or any other that implements
	 * the TextStorage interface.
	 * 
	 * @param name - String object representing the name of the Element whose text is being retrieved.
	 * @return - Returns a String object representing the text stored by the specified Element object.
	 */
	
	public String getElementStoredText(String name) {
		try {
			return ((TextStorage)drawList.get(name)).getText();
		}
		catch(Exception e) {
			System.out.println("Illegal Cast Exception; Element object specified was not of type TextStorage");
			return "";
		}
	}
	
//---  Mechanics   ----------------------------------------------------------------------------
	
	/**
	 * This method handles the process of retrieving an image from a location in memory
	 * specified by the provided String pathIn; this handling permits the file address to
	 * be located within the project, such that exporting an executable .jar file will
	 * include that image file in the .jar and still be correctly accessed (both in the
	 * editor file pathing and in the exported .jar 'file' pathing.)
	 * 
	 * Once an image file has been accessed, it is stored in a HashMap<<r>String, Image> so
	 * that repeated uses of that image object do not require accessing slower memory to
	 * retrieve it. (Significant speed up from this.)
	 * 
	 * @param pathIn - String object representing the file path of the desired Image file
	 * @return - Returns an Image object corresponding to the provided file path given as a String.
	 */
	
	private Image retrieveImage(String pathIn) {
		String path = pathIn.replace("\\", "/");
		try {
			return ImageIO.read(ElementPanel.class.getResource(path.substring(path.indexOf("/"))));
		}
		catch(Exception e) {
			try {
				return ImageIO.read(new File(path));
			}
			catch(Exception e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}
	
//---  Draw   ---------------------------------------------------------------------------------
	
	//-- Image  -----------------------------------------------
	
	/**
	 * This method permits the addition of a DrawnImage object to the list of objects
	 * that are drawn during the repaint() of this ElementPanel object at a specified
	 * location; this allows image files to be drawn to this ElementPanel.
	 * 
	 * @param name - String object representing the name of this Element for reference.
	 * @param priority - int value describing the drawing priority of this Element (what should be drawn first/last in cases of overlap)
	 * @param x - int value describing the x coordinate of the center of this Element in the ElementPanel object
	 * @param y - int value describing the y coordinate of the center of this Element in the ElementPanel object
	 * @param path - String object representing the file path that the Image is located at in memory.
	 */
	
	public void addImage(String name, int priority, int x, int y, String path){
		drawList.put(name, new DrawnImage(x, y, retrieveImage(path)));
	}
	
	/**
	 * This method is a variation of the addImage() method that allows the drawn image to
	 * be scaled up in size according to a positive integer amount.
	 * 
	 * @param name - String object representing the name of this Element for reference.
	 * @param priority - int value describing the drawing priority of this Element (what should be drawn first/last in cases of overlap)
	 * @param x - int value describing the x coordinate of the center of this Element in the ElementPanel object
	 * @param y - int value describing the y coordinate of the center of this Element in the ElementPanel object
	 * @param path - String object representing the file path that the Image is located at in memory.
	 * @param scale - int value describing what scale at which to draw this Image (i.e, 2 would be double the size)
	 */
	
	public void addImage(String name, int priority, int x, int y, String path, int scale){
		drawList.put(name, new DrawnImage(x, y, priority, retrieveImage(path), scale));
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

	public void addButton(String name, int priority, int x, int y, int wid, int hei, int key, boolean centered){
		DrawnButton drawn;
		if(!centered)
			drawn = new DrawnButton(x, y, wid, hei, priority, key);
		else
			drawn = new DrawnButton(x - wid/2, y - hei/2, wid, hei, priority, key);
		drawList.put(name, drawn);
		clickList.put(name, drawn.getDetectionRegion());
		updateClickRegions();
	}	
	
	/**
	 * This method permits the addition of an interactive DrawnButton that instructs this
	 * ElementPanel to produce the provided 'key' value when the described region is clicked
	 * on; this value is given to clickEvent() when it runs.
	 * 
	 * Effectively you define a rectangular region of the screen that, when clicked, causes
	 * clickEvent(key) to be called, allowing for interactivity between the user and this
	 * ElementPanel object.
	 * 
	 * This method draws a filled rectangle to denote the region that can be clicked to trigger
	 * this DrawnButton object.
	 * 
	 * @param name - String object representing the name of this Element for reference.
	 * @param priority - int value describing the drawing priority of this Element (what should be drawn first/last in cases of overlap)
	 * @param x - int value describing the x coordinate of the upper-left corner of this DrawnButton object
	 * @param y - int value describing the y coordinate of the upper-left corner of this DrawnButton object
	 * @param wid - int value describing the width of this DrawnButton object
	 * @param hei - int value describing the height of this DrawnButton object
	 * @param col - Color object describing the color that this DrawnButton object should be
	 * @param key - int value describing the value that is generated when this DrawnButton object is clicked
	 */
	
	public void addButton(String name, int priority, int x, int y, int wid, int hei, Color col, int key, boolean centered){
		DrawnButton drawn;
		if(!centered)
			drawn = new DrawnButton(x, y, wid, hei, priority, key, col);
		else
			drawn = new DrawnButton(x - wid/2, y - hei/2, wid, hei, priority, key, col);
		drawList.put(name, drawn);
		clickList.put(name, drawn.getDetectionRegion());
		updateClickRegions();
	}
	
	/**
	 * This method permits the addition of an image that is interactive; that is, a custom image
	 * whose filled region will generate a defined code value for the clickEvent() method when
	 * clicked by the user.
	 * 
	 * @param name - String object representing the name of this Element for reference.
	 * @param priority - int value describing the drawing priority of this Element (what should be drawn first/last in cases of overlap)
	 * @param x - int value describing the x coordinate of the center of this Element in the ElementPanel object
	 * @param y - int value describing the y coordinate of the center of this Element in the ElementPanel object
	 * @param path - String object representing the file path that the Image is located at in memory.
	 * @param key - int value describing the value that is generated when this DrawnImageButton object is clicked
	 */
	
	public void addImageButton(String name, int priority, int x, int y, String path, int key){
		DrawnImageButton but = new DrawnImageButton(x, y, priority, retrieveImage(path), key);
		drawList.put(name, but);
		clickList.put(name, but.getDetectionRegion());
		updateClickRegions();
	}
	
	/**
	 * This method is a version of the addImageButton() method that permits the drawn image to be
	 * scaled by whole values to increase the image's size.
	 * 
	 * @param name - String object representing the name of this Element for reference.
	 * @param priority - int value describing the drawing priority of this Element (what should be drawn first/last in cases of overlap)
	 * @param x - int value describing the x coordinate of the center of this Element in the ElementPanel object
	 * @param y - int value describing the y coordinate of the center of this Element in the ElementPanel object
	 * @param path - String object representing the file path that the Image is located at in memory.
	 * @param key - int value describing the value that is generated when this DrawnImageButton object is clicked
	 * @param scale - int value describing what scale at which to draw this DrawnImageButton (i.e, 2 would be double the size)
	 */
	
	public void addImageButton(String name, int priority, int x, int y, String path, int key, int scale){
		DrawnImageButton but = new DrawnImageButton(x, y, priority, retrieveImage(path), key, scale);
		drawList.put(name, but);
		clickList.put(name, but.getDetectionRegion());
		updateClickRegions();
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
	
	public void addText(String name, int priority, int x, int y, int width, int height, String phrase, Font font, boolean centered){
		DrawnText text;
		if(!centered) 
			text = new DrawnText(x, y, x + width, y + height, priority, phrase, font);
		else
			text = new DrawnText(x - width/2, y - height/2, x + width/2, y + height/2, priority, phrase, font);
		drawList.put(name, text);
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
	
	public void addTextEntry(String name, int priority, int x, int y, int width, int height, int code, Font font, boolean centered) {
		DrawnTextArea dTA;
		if(!centered)
			dTA = new DrawnTextArea(x, y, x + width, y + height, priority, code, font);
		else
			dTA = new DrawnTextArea(x - width/2, y - height/2, x + width/2, y + height/2, priority, code, font);
		drawList.put(name, dTA);
		clickList.put(name,  dTA.getDetectionRegion());
		updateClickRegions();
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
	
	public void addRectangle(String name, int priority, int x, int y, int width, int height, Color col, boolean centered) {
		DrawnRectangle rect;
		if(!centered)
			rect = new DrawnRectangle(x, y, x + width, y + height, priority, col);
		else
			rect = new DrawnRectangle(x - width/2, y - height/2, x + width/2, y + height/2, priority, col);
		drawList.put(name, rect);
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
	
	public void addRectangle(String name, int priority, int x, int y, int width, int height, Color fillColor, Color borderColor, boolean centered) {
		DrawnRectangle rect;
		if(!centered)
			rect = new DrawnRectangle(x, y, x + width, y + height, priority, fillColor, borderColor);
		else
			rect = new DrawnRectangle(x - width/2, y - height/2, x + width/2, y + height/2, priority, fillColor, borderColor);
		drawList.put(name, rect);
	}

//---  Remove Elements   ----------------------------------------------------------------------
	
	/**
	 * This method allows the removal of an Element object from this ElementPanel (both in
	 * being drawn and in being an interactive field) through the use of the String name
	 * associated to the Element at its construction.
	 * 
	 * @param name - String object representing the name of the Element to be removed.
	 */
	
	public void removeElement(String name) {
		drawList.remove(name);
		clickList.remove(name);
		updateClickRegions();
	}
	
}
