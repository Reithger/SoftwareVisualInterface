package visual.panel;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import visual.panel.element.Element;
import visual.panel.group.ElementGroupManager;
import visual.panel.group.GroupBoundings;

/**
 * 
 * This class manages the Elements that are associated to a particular ElementPanel.
 * 
 * It primarily serves as a way to isolate complicated logic and control access to
 * the drawList data structure so we can ensure mutex safe access, and to be a smaller
 * class that implements an interface to provide access to some functions that Scrollbar needs.
 * 
 */

public class ElementManager implements GroupBoundings {
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** HashMap that assigns a name to objects that can be drawn to the screen; each repaint uses this list to draw to the screen*/
	private volatile HashMap<String, Element> drawList;	//TODO: Abstract these out
	
	private volatile ElementGroupManager groupInfoManager;
	
	private volatile boolean mutex;

//---  Constructors   -------------------------------------------------------------------------
	
	public ElementManager(ElementGroupManager managerOfElementGroups) {
		drawList = new HashMap<String, Element>();
		groupInfoManager = managerOfElementGroups;
	}

//---  Operations   ---------------------------------------------------------------------------
	
	public void paint(Graphics gIn) {
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
				boolean cullIfOutOfWindow = false;
				int origX = -1;
				int origY = -1;
				int maxX = -1;
				int maxY = -1;
				for(String s : group) {
					offX += groupInfoManager.getOffsetX(s);
					offY += groupInfoManager.getOffsetY(s);
					// This mess is so we know the most rigid window viewport available
					// for cutting off a part of how an Element is drawn to remain in
					// that viewport.
					int yOrig = groupInfoManager.getWindowOrigin(s, true);
					int xOrig = groupInfoManager.getWindowOrigin(s, false);
					int yBreadth = groupInfoManager.getWindowBreadth(s, true);
					int xBreadth = groupInfoManager.getWindowBreadth(s, false);
					if((yOrig < origY || origY == -1) && yOrig != -1) {
						origY = yOrig;
					}
					if((xOrig < origX || origX == -1) && xOrig != -1) {
						origX = xOrig;
					}
					if((yOrig + yBreadth < maxY || maxY == -1) && (yOrig + yBreadth >= 0)) {
						maxY = yOrig + yBreadth;
					}
					if((xOrig + xBreadth < maxX || maxX == -1) && (xOrig + xBreadth >= 0)) {
						maxX = xOrig + xBreadth;
					}
					if(!groupInfoManager.getGroupDrawSetting(s)) {
						cullIfOutOfWindow = true;
					}
				}
				if(cullIfOutOfWindow) {
					if(canDrawElement(e, group)) {
						//TODO: detect if partial draw (cut off by viewport) so can call 'drawPartialToScreen' instead
						e.drawPartialToScreen(g, offX, offY, origX, origY, maxX, maxY);
					}
				}
				else {
					e.drawToScreen(g, offX, offY);
				}

			}
		}
	}
	
	/**
	 * 
	 * Function that checks whether a particular element, given the groups it belongs to and their relating window viewports,
	 * should be drawn or not. This uses the element's calculated position based on group offsets along with the view context
	 * of each group's window viewport to see if the element appears in any of the legal viewing spaces (assuming that the
	 * viewports are set to cull elements if they are not within the alloted space for each viewport).
	 * 
	 * @param e
	 * @param group
	 * @return
	 */
	
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

	public void addElement(String n, Element e) {
		openLock();
		e.setHash(n);
		drawList.put(n, e);
		closeLock();
	}

	public int removeElement(String n) {
		openLock();
		if(!drawList.containsKey(n)) {
			closeLock();
			return -1;
		}
		int out = drawList.get(n).hashCode();
		drawList.remove(n);
		closeLock();
		return out;
	}
	
	public boolean hasElement(String name) {
		return drawList.containsKey(name);
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	public Element getElement(String name) {
		openLock();
		Element e = drawList.get(name);
		closeLock();
		return e;
	}	
	
	public ArrayList<String> getElementNames(){
		openLock();
		ArrayList<String> out = new ArrayList<String>(drawList.keySet());
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

//---  Mechanics   ----------------------------------------------------------------------------
	
	protected void openLock() {
		while(mutex) {}
		mutex = true;
	}
	
	protected void closeLock() {
		mutex = false;
	}
	
}
