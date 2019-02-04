package visual.panel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.io.File;
import java.awt.*;
import javax.imageio.ImageIO;
import input.Detectable;
import visual.panel.element.*;

/**
 * This class is the central point around which many simplifications allow for a
 * JUnit-based windowing system to be used and images/objects be drawn there.
 * 
 * @author Mac Clevinger
 *
 */

public class ElementPanel extends Panel{

//---  Constants   ----------------------------------------------------------------------------

//---  Instance Variables   -------------------------------------------------------------------
	
	/** HashMap that saves the image associated to the provided file path and allows re-access via the file path*/
	private HashMap<String, Image> images;
	/** HashMap that assigns a name to objects that can be drawn to the screen; each repaint uses this list to draw to the screen*/
	private HashMap<String, Element> drawList;
	/** */
	private HashMap<String, Detectable> clickList;
	/** */
	private Clickable focusElement;

//---  Constructors   -------------------------------------------------------------------------
	
	public ElementPanel(int x, int y, int width, int height){
		super();
		setDoubleBuffered(true);
		initiate(x, y, width, height);
		images = new HashMap<String, Image>();
		drawList = new HashMap<String, Element>();
		clickList = new HashMap<String, Detectable>();
		setVisible(true);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
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
	
	public void clickBehaviour(int event) {
		System.out.println("Overwrite this method");
	}
	
	public void keyEvent(char event){
		if(focusElement != null) {
			if(!focusElement.focusEvent(event)) {
				return;
			}
		}
		keyBehaviour(event);
	}
	
	public void keyBehaviour(char event) {
		System.out.println("Overwrite this method");
	}
	
	public void paintComponent(Graphics g) {
		ArrayList<Element> elements = new ArrayList<Element>(drawList.values());
		Collections.sort(elements);
		for(Element d : elements) {
			d.drawToScreen(g);
		}
	}
	
	public void updateClickRegions() {
		resetDetectionRegions();
		for(Detectable d : clickList.values()) {
			if(!addClickRegion(d)) {
				removeClickRegion(d.getCode());
				addClickRegion(d);
			}
		}
	}

	public Element getElement(String name) {
		return drawList.get(name);
	}
	
	public String getElementStoredText(String name) {
		try {
			return ((TextStorage)drawList.get(name)).getText();
		}
		catch(Exception e) {
			System.out.println("Illegal Cast Exception; Element object was not of type TextStorage");
			return "";
		}
	}
	
//---  Mechanics   ----------------------------------------------------------------------------
	
	public Image retrieveImage(String pathIn) {
		String path = pathIn.replace("\\", "/");
		try {
			if(images.get(path) == null) {
				images.put(path, ImageIO.read(ElementPanel.class.getResource(path.substring(path.indexOf("/")))));
			}
			return images.get(path);
		}
		catch(Exception e) {
			try {
				if(images.get(path) == null) {
					images.put(path, ImageIO.read(new File(path)));
				}
				return images.get(path);
			}
			catch(Exception e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}
	
//---  Draw   ---------------------------------------------------------------------------------
	
	//-- Image  -----------------------------------------------
	
	public void addImage(String name, int priority, int x, int y, String path){
		drawList.put(name, new DrawnImage(x, y, retrieveImage(path)));
	}
	
	public void addImage(String name, int priority, int x, int y, String path, int scale){
		drawList.put(name, new DrawnImage(x, y, priority, retrieveImage(path), scale));
	}
	
	//-- Button  ----------------------------------------------
	
	public void addButton(String name, int priority, int x, int y, int wid, int hei, Color col, int key){
		DrawnButton drawn = new DrawnButton(x, y, priority, wid, hei, key, col);
		drawList.put(name, drawn);
		clickList.put(name, drawn.getDetectionRegion());
		updateClickRegions();
	}
	
	public void addImageButton(String name, int priority, int x, int y, String path, int key){
		DrawnImageButton but = new DrawnImageButton(x, y, priority, retrieveImage(path), key);
		drawList.put(name, but);
		clickList.put(name, but.getDetectionRegion());
		updateClickRegions();
	}
	
	public void addImageButton(String name, int priority, int x, int y, String path, int key, int scale){
		DrawnImageButton but = new DrawnImageButton(x, y, priority, retrieveImage(path), key, scale);
		drawList.put(name, but);
		clickList.put(name, but.getDetectionRegion());
		updateClickRegions();
	}
	
	//-- Text  ------------------------------------------------
	
	public void addText(String name, int priority, int x, int y, String phrase, Font font){
		DrawnText text = new DrawnText(x, y, priority, phrase, font);
		drawList.put(name, text);
	}

	public void addTextEntry(String name, int priority, int x, int y, int width, int height, int code, Font font) {
		DrawnTextArea dTA = new DrawnTextArea(x, y, x + width, y + height, priority, code, font);
		drawList.put(name, dTA);
		clickList.put(name,  dTA.getDetectionRegion());
		updateClickRegions();
	}
	
	//-- Shapes  ----------------------------------------------
	
	public void addRectangle(String name, int priority, int x, int y, int width, int height, Color col) {
		DrawnRectangle rect = new DrawnRectangle(x, y, x + width, y + height, priority, col);
		drawList.put(name, rect);
	}
	
	public void addRectangle(String name, int priority, int x, int y, int width, int height, Color fillColor, Color borderColor) {
		DrawnRectangle rect = new DrawnRectangle(x, y, x + width, y + height, priority, fillColor, borderColor);
		drawList.put(name, rect);
	}

//---  Remove Elements   ----------------------------------------------------------------------
	
	public void removeElement(String name) {
		//More
		updateClickRegions();
	}
	
}
