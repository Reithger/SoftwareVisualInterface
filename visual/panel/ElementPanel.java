package visual.panel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.io.File;
import java.awt.*;

import javax.imageio.ImageIO;

import input.ClickRegionRectangle;
import visual.panel.element.*;

/**
 * This class implements the Panel abstract class to provide a suite of drawing tools and interactivity
 * to the programmer via the element package, as well as handling some low-level complexities that arise.
 * 
 * If using the SVI library, you should not need to look any deeper than this unless you wish to change
 * base functionality. All features are provided by methods describing those features.
 * 
 * @author Ada Clevinger
 * 
 * TODO: Small pixel font options for text
 *
 */

public class ElementPanel extends Panel{

//---  Constant Values   ----------------------------------------------------------------------
	
	/** boolean value used to denote the meaning of the boolean value given to many methods*/
	public static final boolean CENTERED = true;
	/** boolean value used to denote the meaning of the boolean value given to many methods*/
	public static final boolean NON_CENTERED = false;
	
	public static final int CODE_SCROLL_BAR_X = -58;
	public static final int CODE_SCROLL_BAR_Y = -57;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	//-- Element Storage/Management  --------------------------
	
	/** HashMap that assigns a name to objects that can be drawn to the screen; each repaint uses this list to draw to the screen*/
	private HashMap<String, Element> drawList;
	/** HashMap that assigns a name to defined regions of the screen that generate a specified code upon interaction*/
	private LinkedList<String> clickList;	
	/** 
	 * 	Clickable object representing the most recently selected interactive Element by the User for directing Key Inputs towards.
	 * 
	 * 	Being a Focused Element allows specific behavior programmed into the specific class that implements Clickable to
	 * 	occur, typically absorbing other forms of input such as keyboard input into a TextEntry. This can sometimes stop
	 * 	that input from reaching behavioral functions such as clickBehavior or keyBehavior.
	 */
	private Clickable focusElement;
		
	//-- 

	/** HashMap linking String system paths to Images to cache images that may be used repeatedly*/
	private HashMap<String, Image> images;
	
	private boolean mutex;
	
	//-- Scrollbar  -------------------------------------------
	
	private boolean scrollBarHoriz;
	
	private boolean scrollBarVert;
	
	private boolean vertBarSelect;
	
	private boolean horizBarSelect;
	
	private int startYVertBar;
	
	private int startXHorizBar;
	
	private int yVertChange;

	private int xHorizChange;

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
		super(x, y, width, height);
		drawList = new HashMap<String, Element>();
		clickList = new LinkedList<String>();
		images = new HashMap<String, Image>();
		mutex = false;
		scrollBarHoriz = true;
		scrollBarVert = true;
	}
	
//---  Operations   ---------------------------------------------------------------------------

	/**
	 * This method iterates over all Element objects that have been added to this
	 * ElementPanel object and calls their individual methods to draw themselves.
	 * 
	 * Elements are sorted ahead of time according to their priority values; the
	 * lower the value, the sooner it is drawn (higher values are drawn on top of
	 * lower values.)
	 * 
	 */
	
	public void paintComponent(Graphics gIn) {
		Graphics g = gIn.create();
		openLock();
		ArrayList<Element> elements = new ArrayList<Element>(drawList.values());
		closeLock();
		Collections.sort(elements);
		for(int i = 0; i < elements.size(); i++) {
			elements.get(i).drawToScreen(g, getOffsetX(), getOffsetY());
		}
		updateClickRegions();
		if(scrollBarHoriz || scrollBarVert)
			updateScrollBar(g);
	}
	
	private void updateScrollBar(Graphics g) {
		Color save = g.getColor();
		openLock();
		ArrayList<Element> elements = new ArrayList<Element>(drawList.values());
		closeLock();
		Collections.sort(elements);
		int minY = getMinimumScreenY();
		int maxY = getMaximumScreenY();
		if((getOffsetY() > minY || getOffsetY() + getHeight() < maxY) && scrollBarVert) {
			removeClickRegion(CODE_SCROLL_BAR_Y);
			int subSpaceY = Math.abs(getOffsetY() - minY);
			int overSpaceY = getMaximumScreenY() - getHeight() + getOffsetY();
			boolean minSize = subSpaceY + overSpaceY + getWidth() / 40 >= getHeight();
			int barButtonSizeY = minSize ? getWidth() / 40 : getHeight() - subSpaceY - overSpaceY;
			int barTopSizeY = minSize ? (int)(getHeight() * (double)(subSpaceY / (double)(subSpaceY + overSpaceY + getWidth() / 40))) : subSpaceY;
			yVertChange = minSize ? (int)((subSpaceY + overSpaceY) / (double)(getHeight() - barButtonSizeY)) : 1;
			g.drawRect(getWidth() - getWidth() / 40 - 2, 0, getWidth() / 40, getHeight());
			g.fillRect(getWidth() - getWidth() / 40 - 2, barTopSizeY, getWidth() / 40, barButtonSizeY);
			addClickRegion(new ClickRegionRectangle(getWidth() - getWidth() / 40 - 2, barTopSizeY, getWidth() / 40, barButtonSizeY, CODE_SCROLL_BAR_Y));
		}
		int minX = getMinimumScreenX();
		int maxX = getMaximumScreenX();
		if((getOffsetX() > minX || getOffsetX() + getHeight() < maxX) && scrollBarHoriz) {
			removeClickRegion(CODE_SCROLL_BAR_X);
			int subSpaceX = Math.abs(getOffsetX() - minX);
			int overSpaceX = getMaximumScreenX() - getWidth() + getOffsetX();
			boolean minSize = subSpaceX + overSpaceX + getHeight() / 40 >= getWidth();
			int barButtonSizeX = minSize ? getHeight() / 40 : getWidth() - subSpaceX - overSpaceX;
			int barTopSizeX = minSize ? (int)(getWidth() * (double)(subSpaceX / (double)(subSpaceX + overSpaceX + getHeight() / 40))) : subSpaceX;
			xHorizChange = minSize ? (int)((subSpaceX + overSpaceX) / (double)(getWidth() - barButtonSizeX)) : 1;
			g.drawRect(0, getHeight() - getHeight() / 40 - 2, getWidth(), getHeight() / 40);
			g.fillRect(barTopSizeX, getHeight() - getHeight() / 40 - 2, barButtonSizeX, getHeight() / 40);
			addClickRegion(new ClickRegionRectangle(subSpaceX, getHeight() - getHeight() / 40 - 2, barButtonSizeX, getHeight() / 40, CODE_SCROLL_BAR_X));
		}
		g.setColor(save);
	}

	public boolean moveElement(String name, int x, int y) {
		Element e = getElement(name);
		if(e == null) {
			return false;
		}
		e.setX(x);
		e.setY(y);
		if(clickList.contains(name)) {
			updateClickRegions();
		}
		return true;
	}
	
	public void moveElementPrefixed(String prefix, int x, int y) {
		openLock();
		ArrayList<String> cs = new ArrayList<String>(drawList.keySet());
		closeLock();
		for(String s : cs) {
			if(s.matches(prefix + ".*")) {
				moveElement(s, x, y);
			}
		}
	}

	/**
	 * Support method to consolidate the actions necessary when adding an Element to this
	 * ElementPanel object.
	 * 
	 * @param n - String object representing the name of the Element to add to the screen
	 * @param e - Element object being added to the screen associated to the String n
	 */
	
	private void handleAddElement(String n, Element e) {
		if(n == null || e == null) {
			System.out.println("Error: Null added to drawList for pair <" + n + ", " + e.toString() + ">");
			return;
		}
		openLock();
		drawList.put(n, e);
		closeLock();
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
		for(int i = 0; i < clickList.size(); i++) {
			Clickable c = getClickableElement(clickList.get(i));
			if(c != null) {
				addClickRegion(c.getDetectionRegion());
			}
		}
	}

	//-- Mouse Reactions  -------------------------------------
	
	/**
	 * Certain UI Elements have behavior that requires a communication between higher-level
	 * objects, so that is handled before the clickBehaviour() method which the programmer
	 * can overwrite to define the interactive behavior of this Panel.
	 * 
	 */
	
	public void clickEvent(int event, int x, int y){
		getParentFrame().dispenseAttention();
		setAttention(true);
		focusElement = null;
		for(String s : clickList) {
			Clickable c = getClickableElement(s);
			if(c == null) {
				continue;
			}
			if(c.getCode() == event) {
				focusElement = c;
			}
		}
		clickBehaviour(event, x, y);
	}
	
	public void clickReleaseEvent(int event, int x, int y) {
		vertBarSelect = false;
		horizBarSelect = false;
		clickReleaseBehaviour(event, x, y);
	}
	
	@Override
	public void clickPressEvent(int event, int x, int y) {
		if(event == CODE_SCROLL_BAR_Y) {
			vertBarSelect = true;
			startYVertBar = y;
			return;
		}
		if(event == CODE_SCROLL_BAR_X) {
			horizBarSelect = true;
			startXHorizBar = x;
			return;
		}
		clickPressBehaviour(event, x, y);
	}
	
	/**
	 * Certain UI Elements have behavior that requires a communication between higher-level
	 * objects, so that is handled before the dragBehaviour() method which the programmer
	 * can overwrite to define the interactive behavior of this Panel.
	 */
	
	@Override
	public void dragEvent(int event, int x, int y) {
		if(vertBarSelect == true) {
			int difY = y - startYVertBar;
			int newOffset = getOffsetY() - difY * yVertChange;
			newOffset = newOffset > 0 - getMinimumScreenY() ? 0 - getMinimumScreenY() : newOffset < getHeight() - getMaximumScreenY() ? getHeight() - getMaximumScreenY() : newOffset;
			setOffsetY(newOffset);
			startYVertBar = y;
		}
		if(horizBarSelect == true) {
			int difX = x - startXHorizBar;
			int newOffset = getOffsetX() - difX * xHorizChange;
			newOffset = newOffset > 0 - getMinimumScreenX() ? 0 - getMinimumScreenX() : newOffset < getWidth() - getMaximumScreenX() ? getWidth() - getMaximumScreenX() : newOffset;
			setOffsetX(newOffset);
			startXHorizBar = x;
		}
		dragBehaviour(event, x, y);
	}
	
	//-- Mouse Event Behaviours  ------------------------------
	
	/**
	 * This method exists to be overwritten on an object-by-object basis by the programmer
	 * to define the interactive behavior of this ElementPanel object in regards to click
	 * actions generated by the user's mouse/mousepad being clicked.
	 * 
	 * @param event - int value representing the code generated by user interaction (mouse click on region of screen)
	 * @param x - int value representing the x position of the click event in the ElementPanel
	 * @param y - int value representing the y position of the click event in the ElementPanel
	 */
	
	public void clickBehaviour(int event, int x, int y) {
		System.out.println("Overwrite this method");
	}

	public void clickReleaseBehaviour(int event, int x, int y){
		
	}

	public void clickPressBehaviour(int event, int x, int y) {
		
	}

	/**
	 * This method exists to be overwritten on an object-by-object basis by the programmer
	 * to define the interactive behavior of this ElementPanel object in regards to drag
	 * actions generated by the user's mouse/mousepad being used to click and drag.
	 * 
	 * @param x
	 * @param y
	 */
	
	public void dragBehaviour(int event, int x, int y) {
		
	}
	
	//-- Keyboard Reactions  ----------------------------------
	
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
	
	//-- Keyboard Event Behaviours  ---------------------------
	
	/**
	 * This method exists to be overwritten on an object-by-object basis by the programmer
	 * to define the interactive behavior of this ElementPanel object in regards to key
	 * actions generated by the user's keyboard.
	 * 
	 * @param event - char value representing the key that the user pressed
	 */
	
	public void keyBehaviour(char event) {
		System.out.println("Overwrite this method");
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setScrollBarHorizontal(boolean in) {
		scrollBarHoriz = in;
	}
	
	public void setScrollBarVertical(boolean in) {
		scrollBarVert = in;
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
		openLock();
		Element e = drawList.get(name);
		closeLock();
		return e;
	}
	
	/**
	 * This method retrieves an Element object associated to the provided String object that
	 * implements the Clickable interface, this method catching the potential exception if
	 * the requested Element does not implement the Clickable interface.
	 * 
	 * @param name - String object representing the Clickable object being retrieved
	 * @return - Returns a Clickable implementing object associated to the provided String object
	 */
	
	public Clickable getClickableElement(String name) {
		try {
			Element e = getElement(name);
			if(e != null) {
				return (Clickable)e;
			}
			else {
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Failure to retrieve Clickable implementing Element object; \"getClickableElement(String name)\" function.");
			System.out.println("Attempted to retrieve " + name);
			return null;
		}
	}
	
	/**
	 * This method retrieves an Element object associated to the provided String object that
	 * implements the TextStorage interface, this method catching the potential exception if
	 * the requested Element does not implement the TextStorage interface.
	 * 
	 * @param name - String object representing the TextStorage object being retrieved
	 * @return - Returns a TextStorage implementing object associated to the provided String object
	 */
	
	public TextStorage getStoredTextElement(String name) {
		try {
			return (TextStorage)getElement(name);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Failure to retrieve TextStorage implementing Element object; \"getStoredTextElement(String name)\" function.");
			return null;
		}
	}
	
	/**
	 * This method requests the text stored by the Element object specified by the
	 * provided String name under the assumption that the specified Element object
	 * implements the TextStorage interface which permits the text to be retrieved.
	 * 
	 * This is intended for retrieving user input from Elements that can receive text-based
	 * input from the user, via such Elements as DrawnTextArea, or any other that implements
	 * the TextStorage interface.
	 * 
	 * @param name - String object representing the name of the Element whose text is being retrieved.
	 * @return - Returns a String object representing the text stored by the specified Element object.
	 */
	
	public String getElementStoredText(String name) {
		TextStorage sT = getStoredTextElement(name);
		if(sT == null) {
			return null;
		}
		return sT.getText();
	}

	/**
	 * This function returns the Clickable-implementing object that is currently the focusElement (the most recently
	 * clicked on Clickable-implementing object supposing that it has not been reset by clicking on an empty space).
	 * 
	 * Being a Focused Element allows specific behavior programmed into the specific class that implements Clickable to
	 * occur, typically absorbing other forms of input such as keyboard input into a TextEntry. This can sometimes stop
	 * that input from reaching behavioral functions such as clickBehavior or keyBehavior.
	 * 
	 * @return - Returns a Clickable-implementing object representing the current Clickable object the ElementPanel is focusing on.
	 */
	
	public Clickable getFocusElement() {
		return focusElement;
	}
	
	/**
	 * This function returns an int value describing the number of Element objects currently in the drawList HashMap, roughly
	 * describing the number of Element objects currently being displayed on the screen.
	 * 
	 * @return - Returns an int value representing the number of Element objects currently on the screen (roughly)
	 */
	
	public int getNumberActiveElements() {
		openLock();
		int out = drawList.values().size();
		closeLock();
		return out;
	}
	
	private int getMinimumScreenX() {
		int minX = 0;
		openLock();
		ArrayList<Element> elements = new ArrayList<Element>(drawList.values());
		closeLock();
		for(int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if(e.getMinimumX() < minX) {
				minX = e.getMinimumX();
			}
		}
		return minX > 0 ? 0 : minX;
	}
	
	private int getMaximumScreenX() {
		int maxX = 0;
		openLock();
		ArrayList<Element> elements = new ArrayList<Element>(drawList.values());
		closeLock();
		for(int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if(e.getMaximumX() > maxX) {
				maxX = e.getMaximumX();
			}
		}
		return maxX < getWidth() ? getWidth() : maxX;
	}
	
	private int getMinimumScreenY() {
		int minY = 0;
		openLock();
		ArrayList<Element> elements = new ArrayList<Element>(drawList.values());
		closeLock();
		for(int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if(e.getMinimumY() < minY) {
				minY = e.getMinimumY();
			}
		}
		return minY > 0 ? 0 : minY;
	}
	
	private int getMaximumScreenY() {
		int maxY = 0;
		openLock();
		ArrayList<Element> elements = new ArrayList<Element>(drawList.values());
		closeLock();
		for(int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if(e.getMaximumY() > maxY) {
				maxY = e.getMaximumY();
			}
		}
		return maxY < getHeight() ? getHeight() : maxY;
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
	
	public Image retrieveImage(String pathIn) {
		String path = pathIn.replace("\\", "/");
		if(images.containsKey(path)) {
			return images.get(path);
		}
		try {
			images.put(path, ImageIO.read(ElementPanel.class.getResource(path.substring(path.indexOf("/")))));
		}
		catch(Exception e) {
			try {
				images.put(path, ImageIO.read(new File(path)));
			}
			catch(Exception e1) {
				e1.printStackTrace();
				return null;
			}
		}
		return images.get(path);
	}
	
	private void openLock() {
		while(mutex) {
		}
		mutex = true;
	}
	
	private void closeLock() {
		mutex = false;
	}
	
//---  Draw   ---------------------------------------------------------------------------------
	
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
	
	public void addImage(String name, int priority, int x, int y, boolean center, String path){
		DrawnImage d = new DrawnImage(x, y, priority, center, retrieveImage(path));
		handleAddElement(name, d);
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
	
	public void addImage(String name, int priority, int x, int y, boolean center, Image img) {
		DrawnImage d = new DrawnImage(x, y, priority, center, img);
		handleAddElement(name, d);
	}
	
	/**
	 * This method is a variation of the addImage() method that allows the drawn image to
	 * be scaled up in size according to a positive integer amount.
	 * 
	 * This method uses a String path to access the Image to be drawn.
	 * 
	 * @param name - String object representing the name of this Element for reference.
	 * @param priority - int value describing the drawing priority of this Element (what should be drawn first/last in cases of overlap)
	 * @param x - int value describing the x coordinate of the center of this Element in the ElementPanel object
	 * @param y - int value describing the y coordinate of the center of this Element in the ElementPanel object
	 * @param path - String object representing the file path that the Image is located at in memory.
	 * @param scale - int value describing what scale at which to draw this Image (i.e, 2 would be double the size)
	 */
	
	public void addImage(String name, int priority, int x, int y, boolean center, String path, double scale){
		DrawnImage d = new DrawnImage(x, y, priority, center, retrieveImage(path), scale);
		handleAddElement(name, d);
	}
	
	/**
	 * This method is a variation of the addImage() method that allows the drawn image to
	 * be scaled up in size according to a positive integer amount.
	 * 
	 * This method uses the provided Image object to be drawn as opposed to a String path.
	 * 
	 * @param name
	 * @param priority
	 * @param x
	 * @param y
	 * @param center
	 * @param img
	 * @param scale
	 */
	
	public void addImage(String name, int priority, int x, int y, boolean center, Image img, double scale) {
		DrawnImage d = new DrawnImage(x, y, priority, center, img, scale);
		handleAddElement(name, d);
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
		DrawnButton drawn = new DrawnButton(x, y, wid, hei, priority, centered, key);
		handleAddElement(name, drawn);
		clickList.add(name);
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
		DrawnButton drawn = new DrawnButton(x, y, wid, hei, priority, centered, key, col);
		handleAddElement(name, drawn);
		clickList.add(name);
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
	
	public void addText(String name, int priority, int x, int y, int width, int height, String phrase, Font font, boolean centeredX, boolean centeredY, boolean centeredText){
		DrawnText text = new DrawnText(x, y, width, height, priority, centeredX, centeredY, centeredText, phrase, font);
		handleAddElement(name, text);
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
	
	public void addTextEntry(String name, int priority, int x, int y, int width, int height, int code, String defaultText, Font font, boolean centeredX, boolean centeredY, boolean centeredText) {
		DrawnTextEntry dTA = new DrawnTextEntry(x, y, width, height, priority, centeredX, centeredY, centeredText, defaultText, font, code);
		handleAddElement(name, dTA);
		clickList.add(name);
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
	
	public void addRectangle(String name, int priority, int x, int y, int width, int height, boolean center, Color col) {
		DrawnRectangle d = new DrawnRectangle(x, y, width, height, priority, center, col);
		handleAddElement(name, d);
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
	
	public void addRectangle(String name, int priority, int x, int y, int width, int height, boolean center, Color fillColor, Color borderColor) {
		DrawnRectangle d = new DrawnRectangle(x, y, width, height, priority, center, fillColor, borderColor);
		handleAddElement(name, d);
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
	
	public void addLine(String name, int priority, int x1, int y1, int x2, int y2, int thickness, Color choice) {
		DrawnLine d = new DrawnLine(x1, y1, x2, y2, thickness, priority, choice);
		handleAddElement(name, d);
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
		openLock();
		drawList.remove(name);
		closeLock();
		clickList.remove(name);
	}
	
	/**
	 * This method augments the removeElement method by removing every Element object in drawList
	 * that matches the regex: [prefix].*
	 * 
	 * That is to say, all Element objects whose names are prefixed by the provided String phrase are
	 * removed instead of looking for an exact match to the provided String.
	 * 
	 * @param prefix - String object describing the phrase that is used to search for all Elements who have this phrase as a prefix
	 */
	
	public void removeElementPrefixed(String prefix) {
		openLock();
		ArrayList<String> cs = new ArrayList<String>(drawList.keySet());
		HashSet<String> remv = new HashSet<String>();
		for(int i = 0; i < cs.size(); i++) {
			String s = cs.get(i);
			if(s == null) {
				continue;
			}
			if(s.matches(prefix + ".*")) {
				remv.add(s);
			}
		}
		closeLock();
		for(String s : remv) {
			removeElement(s);
		}
	}
	
}
