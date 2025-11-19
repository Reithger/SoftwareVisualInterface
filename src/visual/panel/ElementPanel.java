package visual.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;

import input.mouse.Detectable;
import visual.panel.element.Clickable;
import visual.panel.element.Element;
import visual.panel.element.TextStorage;
import visual.panel.group.ElementGroupManager;
import visual.panel.group.GroupBoundings;
import visual.panel.group.OffsetManager;


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
 * TODO: Some way to reduce number of threads once no longer necessary, increment/decrement the amount dynamically
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

public class ElementPanel extends Panel {

//---  Constant Values   ----------------------------------------------------------------------
	
	/** boolean value used to denote the meaning of the boolean value given to many methods*/
	public static final boolean CENTERED = true;
	/** boolean value used to denote the meaning of the boolean value given to many methods*/
	public static final boolean NON_CENTERED = false;
	
	private static final int DRAG_CLICK_SENSITIVITY = 25;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	//-- Element Storage/Management  --------------------------

	/** 
	 * 	Clickable object representing the most recently selected interactive Element by the User for directing Key Inputs towards.
	 * 
	 * 	Being a Focused Element allows specific behavior programmed into the specific class that implements Clickable to
	 * 	occur, typically absorbing other forms of input such as keyboard input into a TextEntry. This can sometimes stop
	 * 	that input from reaching behavioral functions such as clickBehavior or keyBehavior.
	 */
	private String focusElement;
	
	private ElementManager elements;
	
	private ElementGroupManager groupInfoManager;
	
	private ClickListManager clickList;
	
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
		groupInfoManager = new ElementGroupManager();
		elements = new ElementManager(groupInfoManager);
		clickList = new ClickListManager();
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
		elements.paint(gIn);
	}

 	//--  Get Sub-Managers  -------------------------------
 	
	public ElementLoader accessElementLoader() {
		return new ElementLoader(this);
	}
	
	public OffsetManager getOffsetManager() {
		return groupInfoManager;
	}
	
	protected GroupBoundings getBoundingAccess() {
		return elements;
	}
	
	//-- Element Adding/Removing  -----------------------------
	
	/**
	 * Support method to consolidate the actions necessary when adding an Element to this
	 * ElementPanel object.
	 * 
	 * @param n - String object representing the name of the Element to add to the screen
	 * @param e - Element object being added to the screen associated to the String n
	 */
	
	public void handleAddElement(String n, Element e, String frame) {
		if(n == null || e == null) {
			System.out.println("Error: Null added to drawList or frameList for pair <" + n + ", " + e.toString() + ">");
			return;
		}
		elements.addElement(n, e);
		if(frame != null) {
			groupInfoManager.addMapping(e.hashCode(), frame);
		}
		if(clickList.hasName(n))
			updateClickRegion(n);
	}
	
	public void handleAddClickable(String name) {
		clickList.addElement(name);
	}

	/**
	 * This method allows the removal of an Element object from this ElementPanel (both in
	 * being drawn and in being an interactive field) through the use of the String name
	 * associated to the Element at its construction.
	 * 
	 * @param name - String object representing the name of the Element to be removed.
	 */
	
	public void removeElement(String name) {
		if(clickList.hasName(name)) {
			Clickable c = getClickableElement(name);
			if(c != null && clickList.hasName(name)) {
				removeClickRegion(c.getIdentity());
				c.unfocus();
			}
		}
		if(focusElement != null && focusElement.equals(name)) {
			focusElement = null;
		}
		int hash = elements.removeElement(name);
		if(hash != -1)
			groupInfoManager.removeMapping(hash);
		clickList.removeElement(name);
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
		ArrayList<String> cs = elements.getElementNames();
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
		if(clickList.hasName(name)) {
			updateClickRegion(name);
		}
		return true;
	}
	
	public boolean shiftElement(String name, int changeX, int changeY) {
		Element e = getElement(name);
		if(e == null) {
			return false;
		}
		e.moveElement(e.getX() + changeX, e.getY() + changeY);
		if(clickList.hasName(name)) {
			updateClickRegion(name);
		}
		return true;
	}

	public int[] getRelativeClickPosition(String name, int x, int y) {
		String group = groupInfoManager.getGroupMembership(name).iterator().next();
		int out = x - getElement(name).getX() - getOffsetManager().getOffsetX(group);
		int out_y = y - getElement(name).getY() - getOffsetManager().getOffsetY(group);
		return new int[] {out, out_y};
	}

	@Override
	public void resetDetectionRegions() {
		super.resetDetectionRegions();
		updateClickRegions();
	}
	
	public void updateClickRegions() {
		for(String s : clickList.getClickableNames()) {
			updateClickRegion(s);
		}
	}
	
	public void printClickRegions() {
		for(String s : clickList.getClickableNames()) {
			Clickable c = getClickableElement(s);
			Detectable dr = c.getDetectionRegion(0, 0);
			System.out.println(c.getCode() + " " + c.getIdentity() + " " + dr.toString());
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
		for(String s : clickList.getClickableNames()) {
			Clickable c = getClickableElement(s);
			if(c != null) {
				if(c.getCode() == event) { //TODO: Change this to getting the identity instead of the code
					focusElement = s;
					c.focus();
					return;
				}
			}
		}
		focusElement = null;
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
		if(getParentFrame() != null) {
			getParentFrame().dispenseAttention();
		}
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
	
	@Override
	public void mouseMoveEvent(int event, int x, int y) {
		getEventReceiver().mouseMoveEvent(event, x, y);
	}

	@Override
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

	@Override
	public void keyEvent(char event){
		if(focusElement != null) {
			Clickable c = getClickableElement(focusElement);
			if(c != null && !c.focusKeyEvent(event)) {
				focusEventReaction(getFocusElementCode());
				updateClickRegion(focusElement);
				return;
			}
			updateClickRegion(focusElement);
		}
		getEventReceiver().keyEvent(event);
	}

	@Override
	public void keyPressEvent(char event) {
		getEventReceiver().keyPressEvent(event);
	}

	@Override
	public void keyReleaseEvent(char event) {
		getEventReceiver().keyReleaseEvent(event);
	}

	@Override
	public void focusEventReaction(int code) {
		getEventReceiver().focusEventReaction(code);
	}

//---  Setter Methods   -----------------------------------------------------------------------

	public void setElementStoredText(String elementName, String newText) throws Exception{
		TextStorage sT = getStoredTextElement(elementName);
		if(sT == null) {
			throw new Exception("Error: Failed attempt to set stored text from Element: " + elementName);
		}
		sT.setText(newText);
	}
	
	public void setDragClickSensitivity(int in) {
		dragClickSensitivity = in;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------

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
	
	public String getElementStoredText(String name) throws Exception{
		TextStorage sT = getStoredTextElement(name);
		if(sT == null) {
			throw new Exception("Error: Failed attempt to retrieve stored text from Element: " + name);
		}
		return sT.getText();
	}
	
	public boolean containsElement(String name) {
		return elements.hasElement(name);
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
		return elements.getElement(name);
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
	
	private int getFocusElementCode() {
		return getClickableElement(getFocusElement()).getCode();
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
