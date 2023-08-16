package com.github.softwarevisualinterface.visual.panel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;

import com.github.softwarevisualinterface.misc.Canvas;
import com.github.softwarevisualinterface.visual.panel.element.ElementFactory;
import com.github.softwarevisualinterface.visual.panel.element.Clickable;
import com.github.softwarevisualinterface.visual.panel.element.Element;
import com.github.softwarevisualinterface.visual.panel.element.TextStorage;
import com.github.softwarevisualinterface.visual.panel.group.ElementGroupManager;
import com.github.softwarevisualinterface.visual.panel.group.OffsetManager;


/**
 * This class implements the Panel abstract class to provide a suite of drawing tools and interactivity
 * to the programmer via the element package, as well as handling some low-level complexities that arise.
 * 
 * If using the SVI library, you should not need to look any deeper than this unless you wish to change
 * base functionality. All features are provided by methods describing those features.
 * 
 * Additionally, a composite package has been made that builds on top of this basic foundation to make less
 * flexible but more convenient tools which can themselves be overridden.
 * 
 * TODO: Big memory leak issues, need to fix that; maybe not? Still needs a good reworking though
 * TODO: ElementFactory to reduce number of dependencies on specific subclasses; simplify their input?
 * TODO: Small pixel font options for text
 * TODO: Colored text options
 * TODO: Diagnostic output of all Elements, instance variables
 * TODO: Review adding functions, they're getting cluttered
 * TODO: Decorator class to wrap it in Scrollbar and Drag Navigation functionality? Otherwise cluttered with booleans.
 * TODO: This needs major refactoring, it's almost 1000 lines! That was the size of that whole game you made in the summer of 2016! Good memories...
 * TODO: NestedEventReceiver should be able to remove composite receivers and label them
 * TODO: Element groupings with custom offset values - can remove FrameList?
 * TODO: Hover Text (text appears when hovering over a region/element)
 * TODO: Make an object with many attributes to assign to cut down on size of input for each function?
 * TODO: Reconsider what HandlePanel can do, add comments to it
 * TODO: FocusElement currently can be updated to the wrong element if both share a code value
 * TODO: Input Event functions can't discern which element was selected just from the code value alone
 * TODO: Let TextEntry ignore certain key inputs so they can be interpreted at a higher level even if key input is being fed to that Element
 * 
 * @author Ada Clevinger
 * 
 *
 */

public class ElementPanel extends Panel implements OffsetManager{

//---  Constant Values   ----------------------------------------------------------------------
	
	/** boolean value used to denote the meaning of the boolean value given to many methods*/
	public static final boolean CENTERED = true;
	/** boolean value used to denote the meaning of the boolean value given to many methods*/
	public static final boolean NON_CENTERED = false;
	
	private static final int DRAG_CLICK_SENSITIVITY = 25;
	
	private static int SCROLLBAR_CODE_VALUE = -50;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	//-- Element Storage/Management  --------------------------
	
	/** HashMap that assigns a name to objects that can be drawn to the screen; each repaint uses this list to draw to the screen*/
	private volatile HashMap<String, Element> drawList;	//TODO: Abstract these out
	
	private volatile ElementGroupManager groupInfoManager;
	
	/** HashMap that assigns a name to defined regions of the screen that generate a specified code upon interaction*/
	private volatile LinkedList<String> clickList;	
	/** 
	 * 	Clickable object representing the most recently selected interactive Element by the User for directing Key Inputs towards.
	 * 
	 * 	Being a Focused Element allows specific behavior programmed into the specific class that implements Clickable to
	 * 	occur, typically absorbing other forms of input such as keyboard input into a TextEntry. This can sometimes stop
	 * 	that input from reaching behavioral functions such as clickBehavior or keyBehavior.
	 */
	private String focusElement;
		
	//-- 

	/** HashMap linking String system paths to Images to cache images that may be used repeatedly*/
	private HashMap<String, Image> images;	//TODO: Image manager? Can be a small class, but still
	
	private int dragClickX;
	
	private int dragClickY;
	
	private int dragClickSensitivity;	//This is so you can have some grace period in having clicks be detected
	
	private volatile boolean clickFired;	//This is to avoid double-firing a clickEvent
	
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
		groupInfoManager = new ElementGroupManager();
		clickList = new LinkedList<String>();
		images = new HashMap<String, Image>();
		dragClickSensitivity = DRAG_CLICK_SENSITIVITY;
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
	 * Elements are also sorted into two categories, regular and frame; this decides
	 * whether or not changing the origin point of the perspective space will move the
	 * element or not. As it stands, any Frame Element will always be on top of regular
	 * elements due to some design issues.
	 * 
	 */
	
	public void paintComponent(Graphics gIn) {
		Graphics g = gIn.create();
		openLock();
		ArrayList<Element> elements = new ArrayList<Element>(drawList.values());
		closeLock();
		try {
			Collections.sort(elements);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		for(int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			HashSet<String> group = groupInfoManager.getGroups(e.hashCode());
			if(group == null) {
				elements.get(i).drawToScreen(g, 0, 0);
			}
			else {
				int offX = 0;
				int offY = 0;
				boolean compl = false;
				for(String s : group) {
					offX += groupInfoManager.getOffsetX(s);
					offY += groupInfoManager.getOffsetY(s);
					if(!groupInfoManager.getGroupDrawSetting(s)) {
						compl = true;
					}
				}
				if(compl) {
					if(canDrawElement(e, group)) {
						e.drawToScreen(g, offX, offY);
					}
				}
				else {
					e.drawToScreen(g, offX, offY);
				}

			}
		}
	}
	
	private boolean canDrawElement(Element e, HashSet<String> group) {
		boolean canDraw = true;
		int offX = 0;
		int offY = 0;
		for(String s : group) {
			offX += groupInfoManager.getOffsetX(s);
			offY += groupInfoManager.getOffsetY(s);
		}
		for(String s : group) {	
			if(!groupInfoManager.getGroupDrawSetting(s)) {
				canDraw = canDraw && ((groupInfoManager.isPositionInWindowBounds(s, e.getMinimumX() + offX, e.getMaximumX() + offX, false) && groupInfoManager.isPositionInWindowBounds(s, e.getMinimumY() + offY, e.getMaximumY() + offY, true)));
			}
		}
		return canDraw;
	}

	public boolean setFocusElement(String nom) {
		if(getClickableElement(focusElement) != null) {
			getClickableElement(focusElement).unfocus();
		}
		if(getClickableElement(nom) != null) {
			focusElement = nom;
			getClickableElement(nom).focus();
			return true;
		}
		return false;
	}
	
	//-- Element Adding/Removing  -----------------------------
	
	/**
	 * Support method to consolidate the actions necessary when adding an Element to this
	 * ElementPanel object.
	 * 
	 * @param n - String object representing the name of the Element to add to the screen
	 * @param e - Element object being added to the screen associated to the String n
	 */
	
	private void handleAddElement(String n, Element e, String frame) {
		if(n == null || e == null) {
			System.out.println("Error: Null added to drawList or frameList for pair <" + n + ", " + e.toString() + ">");
			return;
		}
		openLock();
		e.setHash(n);
		drawList.put(n, e);
		if(frame != null) {
			groupInfoManager.addMapping(e.hashCode(), frame);
		}
		closeLock();
		if(clickList.contains(n))
			updateClickRegion(n);
	}
	
	/**
	 * This method allows the removal of an Element object from this ElementPanel (both in
	 * being drawn and in being an interactive field) through the use of the String name
	 * associated to the Element at its construction.
	 * 
	 * @param name - String object representing the name of the Element to be removed.
	 */
	
	public void removeElement(String name) {
		if(clickList.contains(name)) {
			Clickable c = getClickableElement(name);
			openLock();
			if(c != null && clickList.contains(name))
				removeClickRegion(c.getIdentity());
			closeLock();
		}
		openLock();
		Element e = drawList.get(name);
		if(e != null)
			groupInfoManager.removeMapping(e.hashCode());
		drawList.remove(name);
		clickList.remove(name);
		closeLock();
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
	
	public void removeAllElements() {
		removeElementPrefixed("");
		resetDetectionRegions();
	}
	
	//-- Group Stuff  -----------------------------------------
	
	public boolean adjustGroupOffset(String ref, int newOffsetX, int newOffsetY) {
		if(!groupInfoManager.hasGroup(ref)) {
			return false;
		}
		groupInfoManager.setOffsetX(ref, newOffsetX);
		groupInfoManager.setOffsetY(ref, newOffsetY);
		return true;
	}
	
	public boolean addElementToGroup(String elemName, String groupName) {
		Element e = getElement(elemName);
		if(e == null) {
			return false;
		}
		groupInfoManager.addMapping(e.hashCode(), groupName);
		return true;
	}
	
	public boolean removeElementFromGroup(String elemName, String groupName) {
		Element e = getElement(elemName);
		if(e == null) {
			return false;
		}
		
		groupInfoManager.removeMapping(e.hashCode(), groupName);
		return true;
	}
	
	public void removeElementPrefixedFromGroup(String pref, String groupName) {
		openLock();
		for(String n : drawList.keySet()) {
			if(n.matches(pref + ".*")) {
				removeElementFromGroup(n, groupName);
			}
		}
		closeLock();
	}
	
	public void addElementPrefixedToGroup(String pref, String groupName) {
		openLock();
		for(String n : drawList.keySet()) {
			if(n.matches(pref + ".*")) {
				addElementToGroup(n, groupName);
			}
		}
		closeLock();
	}

	public boolean addGroup(String nom) {
		if(groupInfoManager.hasGroup(nom)) {
			return false;
		}
		groupInfoManager.addGroup(nom);
		return true;
	}
	
	public void removeGroup(String nom) {
		groupInfoManager.removeGroup(nom);
	}
	
	public void setGroupWindow(String groupName, int origin, int breadth, boolean isVert) {
		groupInfoManager.setWindow(groupName, origin, breadth, isVert);
	}
	
	public void setGroupDrawOutsideWindow(String groupName, boolean set) {
		groupInfoManager.setGroupDrawSetting(groupName, set);
	}
	
	//-- Adjust Element Property  -----------------------------
	
	public boolean moveElement(String name, int x, int y) {
		Element e = getElement(name);
		if(e == null) {
			return false;
		}
		e.moveElement(x, y);
		if(clickList.contains(name)) {
			updateClickRegion(name);
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
	 * This method reassigns the regions of the screen that detect user input and
	 * trigger a clickEvent() with the corresponding generated code value.
	 * 
	 * It first removes all Detection regions from this ElementPanel, and then calls
	 * on all Clickable objects to generate their Detectable click regions to be added
	 * to this ElementPanel. 
	 * 
	 */
	
	private void updateClickRegions() {
		for(int i = 0; i < clickList.size(); i++) {
			updateClickRegion(clickList.get(i));
		}
	}
	
	private void updateClickRegions(String groupName) {
		for(int i = 0; i < clickList.size(); i++) {
			Clickable c = getClickableElement(clickList.get(i));
			if(c == null) {
				continue;
			}
			HashSet<String> group = groupInfoManager.getGroups(c.getIdentity());
			if(group.contains(groupName)) {
				if(canDrawElement(getElement(clickList.get(i)), group)) {
					updateClickRegion(clickList.get(i));
				}
				else {
					removeClickRegion(c.getIdentity());
				}
			}
		}
	}
	
	private void updateClickRegion(String name) {
		Clickable c = getClickableElement(name);
		if(c != null) {
			HashSet<String> group = groupInfoManager.getGroups(c.getIdentity());
			if(group == null) {
				addClickRegion(c.getIdentity(), c.getDetectionRegion(0, 0));
			}
			else {
				int offX = 0;
				int offY = 0;
				for(String s : group) {
					offX += groupInfoManager.getOffsetX(s);
					offY += groupInfoManager.getOffsetY(s);
				}
				addClickRegion(c.getIdentity(), c.getDetectionRegion(offX, offY));
			}
		}
	}
	
	private void updateFocusElement(int event) {
		if(getClickableElement(focusElement) != null) {
			getClickableElement(focusElement).unfocus();
		}
		for(String s : clickList) {
			Clickable c = getClickableElement(s);
			if(c != null) {
				if(c.getCode() == event) { //TODO: Change this to getting the identity instead of the code
					focusElement = s;
					c.focus();
				}
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
	
	@Override
	public void clickEvent(int event, int x, int y, int clickType){
		if(clickFired) {
			return;
		}
		clickFired = true;
		getParentFrame().dispenseAttention();
		setAttention(true);
		getEventReceiver().clickEvent(event, x, y, clickType);
	}

	@Override
	public void clickReleaseEvent(int event, int x, int y, int clickType) {
		double dist = Math.sqrt(Math.pow(x - dragClickX, 2) + Math.pow(y - dragClickY, 2));
		if(!clickFired && dist < dragClickSensitivity) {
			clickEvent(event, x, y, clickType);
		}
		getEventReceiver().clickReleaseEvent(event, x, y, clickType);
	}
	
	@Override
	public void clickPressEvent(int event, int x, int y, int clickType) {
		updateFocusElement(event);
		clickFired = false;
		
		dragClickX = x;
		dragClickY = y;
		getEventReceiver().clickPressEvent(event, x, y, clickType);
	}
	
	/**
	 * Certain UI Elements have behavior that requires a communication between higher-level
	 * objects, so that is handled before the dragBehaviour() method which the programmer
	 * can overwrite to define the interactive behavior of this Panel.
	 */
	
	@Override
	public void dragEvent(int event, int x, int y, int clickType) {
		if(focusElement != null) {
			Clickable c = getClickableElement(focusElement);
			if(!c.focusDragEvent(x, y, clickType)) {
				updateClickRegion(focusElement);
				return;
			}
			updateClickRegion(focusElement);
		}
		getEventReceiver().dragEvent(event, x, y, clickType);
	}
	
	public void mouseMoveEvent(int event, int x, int y) {
		getEventReceiver().mouseMoveEvent(event, x, y);
	}
	
	public void mouseWheelEvent(int rotation) {
		getEventReceiver().mouseWheelEvent(rotation);
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
			Clickable c = getClickableElement(focusElement);
			if(!c.focusKeyEvent(event)) {
				focusEventReaction(getFocusElementCode());
				updateClickRegion(focusElement);
				return;
			}
			updateClickRegion(focusElement);
		}
		getEventReceiver().keyEvent(event);
	}
	
	public void keyPressEvent(char event) {
		getEventReceiver().keyPressEvent(event);
	}
	
	public void keyReleaseEvent(char event) {
		getEventReceiver().keyReleaseEvent(event);
	}
	
	public void focusEventReaction(int code) {
		getEventReceiver().focusEventReaction(code);
	}

//---  Draw   ---------------------------------------------------------------------------------
	
	//-- Canvas  ----------------------------------------------
	
	public Canvas addCanvas(String name, int priority, String frame, int x, int y, int elemWidth, int elemHeight, int canWidth, int canHeight, int inCode) {
		Canvas c = new Canvas(canWidth, canHeight);
		clickList.add(name);
		handleAddElement(name, ElementFactory.generateCanvas(priority, x, y, elemWidth, elemHeight, c, inCode), frame);
		return c;
	}
	
	public void addCanvas(String name, int priority, String frame, int x, int y, int elemWidth, int elemHeight, Canvas inCanvas, int inCode) {
		clickList.add(name);
		handleAddElement(name, ElementFactory.generateCanvas(priority, x, y, elemWidth, elemHeight, inCanvas, inCode), frame);
	}
	
	//-- Scrollbar  -------------------------------------------
	
	public void addScrollbar(String name, int priority, String frame, int scrollX, int scrollY, int scrollWid, int scrollHei, int windowOrigin, int windowSize, String groupControl, boolean isVert) {
		clickList.add(name);
		setGroupWindow(groupControl, windowOrigin, windowSize, isVert);
		handleAddElement(name, ElementFactory.generateScrollbar(priority, scrollX, scrollY, scrollWid, scrollHei, windowOrigin, windowSize, SCROLLBAR_CODE_VALUE--, groupControl, isVert, this), frame);
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
		Image img = retrieveImage(path);
		handleAddElement(name, ElementFactory.generateImage(priority, x, y, center, img), frame);
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
		handleAddElement(name, ElementFactory.generateImage(priority, x, y, center, img), frame);
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
		Image img = retrieveImage(path);
		handleAddElement(name, ElementFactory.generateImage(priority, x, y, (int)(img.getWidth(null) * scale), (int)(img.getHeight(null) * scale), center, img, true), frame);
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
		handleAddElement(name, ElementFactory.generateImage(priority, x, y, (int)(img.getWidth(null) * scale), (int)(img.getHeight(null) * scale), center, img, true), frame);
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
		Image img = retrieveImage(path);
		handleAddElement(name, ElementFactory.generateImage(priority, x, y, width, height, center, img, proportion), frame);
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
		handleAddElement(name, ElementFactory.generateImage(priority, x, y, width, height, center, img, proportion), frame);
	}
	
	//-- Animations  ------------------------------------------
	
	public void addAnimation(String name, int priority, String frame, int x, int y, boolean center, int period, double scale, String[] images) {
		Image[] rec = new Image[images.length];
		for(int i = 0; i < rec.length; i++) {
			rec[i] = retrieveImage(images[i]);
		}
		int[] periods = new int[images.length];
		for(int i = 0; i < periods.length; i++) {
			periods[i] = period;
		}
		handleAddElement(name, ElementFactory.generateAnimation(priority, x, y, center, periods, scale, rec), frame);
	}
	
	public void addAnimation(String name, int priority, String frame, int x, int y, boolean center, int[] period, double scale, String[] images) {
		Image[] rec = new Image[images.length];
		for(int i = 0; i < rec.length; i++) {
			rec[i] = retrieveImage(images[i]);
		}
		handleAddElement(name, ElementFactory.generateAnimation(priority, x, y, center, period, scale, rec), frame);
	}
	
	public void addAnimation(String name, int priority, String frame, int x, int y, boolean center, int period, double scale, Image[] images) {
		int[] periods = new int[images.length];
		for(int i = 0; i < periods.length; i++) {
			periods[i] = period;
		}
		handleAddElement(name, ElementFactory.generateAnimation(priority, x, y, center, periods, scale, images), frame);
	}
	
	public void addAnimation(String name, int priority, String frame, int x, int y, boolean center, int[] period, double scale, Image[] images) {
		handleAddElement(name, ElementFactory.generateAnimation(priority, x, y, center, period, scale, images), frame);
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
		clickList.add(name);
		handleAddElement(name, ElementFactory.generateButton(priority, x, y, wid, hei, key, centered), frame);
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
		handleAddElement(name, ElementFactory.generateText(priority, x, y, width, height, phrase, font, centeredX, centeredY, centeredText), frame);
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
		clickList.add(name);
		handleAddElement(name, ElementFactory.generateTextEntry(priority, x, y, width, height, code, defaultText, font, centeredX, centeredY, centeredText), frame);
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
		handleAddElement(name, ElementFactory.generateRectangle(priority, x, y, width, height, center, col, col), frame);
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
		handleAddElement(name, ElementFactory.generateRectangle(priority, x, y, width, height, center, fillColor, borderColor), frame);
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
		handleAddElement(name, ElementFactory.generateLine(priority, x1, y1, x2, y2, thickness, choice), frame);
	}
	
//---  Remove Elements   ----------------------------------------------------------------------

//---  Setter Methods   -----------------------------------------------------------------------

	public void setElementStoredText(String elementName, String newText) {
		TextStorage sT = getStoredTextElement(elementName);
		if(sT == null) {
			return;
		}
		sT.setText(newText);
	}
	
	public void setDragClickSensitivity(int in) {
		dragClickSensitivity = in;
	}
	
	public void setOffsetX(String groupName, int newOffsetX) {
		groupInfoManager.setOffsetX(groupName, newOffsetX);
		updateClickRegions(groupName);
	}
	
	public void setOffsetY(String groupName, int newOffsetY) {
		groupInfoManager.setOffsetY(groupName, newOffsetY);
		updateClickRegions(groupName);
	}
		
//---  Getter Methods   -----------------------------------------------------------------------
	
	public int getOffsetX(String groupName) {
		return groupInfoManager.getOffsetX(groupName);
	}
	
	public int getOffsetY(String groupName) {
		return groupInfoManager.getOffsetY(groupName);
	}
	
	public int getTextWidth(String text, Font use) {
		Graphics g = this.getPanel().getGraphics();
		if(g == null) {
			return -1;
		}
		Font save = g.getFont();
		g.setFont(use);
		FontMetrics fM = g.getFontMetrics();
		int out = fM.stringWidth(text);
		g.setFont(save);
		return out;
	}
	
	public int getTextHeight(Font use) {
		Graphics g = this.getPanel().getGraphics();
		if(g == null) {
			return -1;
		}
		Font save = g.getFont();
		g.setFont(use);
		FontMetrics fM = g.getFontMetrics();
		int out = fM.getHeight();
		g.setFont(save);
		return out;
	}
	
	/**
	 * This method requests one of the Elements attributed to this ElementPanel to be returned,
	 * defined by the provided String object representing a name associated to that Element.
	 * 
	 * @param name - String object representing the name of the Element being requested.
	 * @return - Returns an Element object specified by a provided name
	 */
	
	private Element getElement(String name) {
		openLock();
		Element e = drawList.get(name);
		closeLock();
		return e;
	}
	
	public int getElementX(String name) {
		return getElement(name).getX();
	}
	
	public int getElementY(String name) {
		return getElement(name).getY();
	}
	
	/**
	 * This method retrieves an Element object associated to the provided String object that
	 * implements the Clickable interface, this method catching the potential exception if
	 * the requested Element does not implement the Clickable interface.
	 * 
	 * @param name - String object representing the Clickable object being retrieved
	 * @return - Returns a Clickable implementing object associated to the provided String object
	 */
	
	private Clickable getClickableElement(String name) {
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
	
	private Element getElementByCode(int code) {
		for(String s : clickList) {
			Clickable c = getClickableElement(s);
			if(c != null) {
				if(c.getCode() == code) {
					return getElement(s);
				}
			}
		}
		return null;
	}
	
	/**
	 * This method retrieves an Element object associated to the provided String object that
	 * implements the TextStorage interface, this method catching the potential exception if
	 * the requested Element does not implement the TextStorage interface.
	 * 
	 * @param name - String object representing the TextStorage object being retrieved
	 * @return - Returns a TextStorage implementing object associated to the provided String object
	 */
	
	private TextStorage getStoredTextElement(String name) {
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
	
	public String getFocusElement() {
		return focusElement;
	}
	
	public int getFocusElementCode() {
		return getClickableElement(getFocusElement()).getCode();
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
	
	public int getMinimumScreenX(String groupName) {
		Integer minX = null;
		openLock();
		ArrayList<Element> elements = new ArrayList<Element>(drawList.values());
		closeLock();
		for(int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if((groupName == null || groupInfoManager.getGroups(e.hashCode()).contains(groupName)) && (minX == null || e.getMinimumX() < minX)) {
				minX = e.getMinimumX();
			}
		}
		minX = minX == null ? 0 : minX;
		return minX;
	}
	
	public int getMaximumScreenX(String groupName) {
		Integer maxX = null;
		openLock();
		ArrayList<Element> elements = new ArrayList<Element>(drawList.values());
		closeLock();
		for(int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if((groupName == null || groupInfoManager.getGroups(e.hashCode()).contains(groupName)) && (maxX == null || e.getMaximumX() > maxX)) {
				maxX = e.getMaximumX();
			}
		}
		maxX = maxX == null ? 0 : maxX;
		return maxX;
	}
	
	public int getMinimumScreenY(String groupName) {
		Integer minY = null;
		openLock();
		ArrayList<Element> elements = new ArrayList<Element>(drawList.values());
		closeLock();
		for(int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if((groupName == null || groupInfoManager.getGroups(e.hashCode()).contains(groupName)) && (minY == null || e.getMinimumY() < minY)) {
				minY = e.getMinimumY();
			}
		}
		minY = minY == null ? 0 : minY;
		return minY;
	}
	
	public int getMaximumScreenY(String groupName) {
		Integer maxY = null;
		openLock();
		ArrayList<Element> elements = new ArrayList<Element>(drawList.values());
		closeLock();
		for(int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if((groupName == null || groupInfoManager.getGroups(e.hashCode()).contains(groupName)) && (maxY == null || e.getMaximumY() > maxY)) {
				maxY = e.getMaximumY();
			}
		}
		maxY = maxY == null ? 0 : maxY;
		return maxY;
	}
	
	public int getDragClickSensitivity() {
		return dragClickSensitivity;
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
	
	public boolean removeCachedImage(String pathIn) {
		String path = pathIn.replace("\\", "/");
		return null != images.remove(path);
	}
	
}
