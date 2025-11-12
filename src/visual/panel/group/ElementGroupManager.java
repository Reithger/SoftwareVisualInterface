package visual.panel.group;

import java.util.HashMap;
import java.util.HashSet;

/**
 * 
 * Element Groups are a way to tie the offset positions of many Elements together so that
 * they can easily be moved all together (such as someone scrolling a scrollbar and the main
 * elements of the page moving but not the header/footer).
 * 
 * This class manages these Groups and the elements associated to each group, as well as
 * the 'view' window for particular Groups. The user may desire to represent a scrolling
 * collection of elements that are limited to a finite region of the Panel, so the window
 * defines the viewport for which elements in a group will be drawn or not.
 * 
 */

public class ElementGroupManager implements OffsetManager{

//---  Instance Variables   -------------------------------------------------------------------
	
	private HashMap<Integer, HashSet<String>> groupMapping;

	private HashMap<String, OffsetValues> groupOffsets;
	
	private HashMap<String, WindowValues> groupWindows;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public ElementGroupManager() {
		groupMapping = new HashMap<Integer, HashSet<String>>();
		groupOffsets = new HashMap<String, OffsetValues>();
		groupWindows = new HashMap<String, WindowValues>();
	}
	
//---  Adder Methods   ------------------------------------------------------------------------
	
	public void addMapping(Integer hash, String groupName) {
		addGroup(groupName);
		HashSet<String> ref = groupMapping.get(hash);
		if(ref != null) {
			ref.add(groupName);
			groupMapping.put(hash, ref);
		}
		else {
			HashSet<String> use = new HashSet<String>();
			use.add(groupName);
			groupMapping.put(hash, use);
		}
	}
	
	public void addGroup(String groupName) {
		if(!groupOffsets.containsKey(groupName)) {
			groupOffsets.put(groupName, new OffsetValues());
			groupWindows.put(groupName, new WindowValues());
		}
	}
	
//---  Remover Methods   ----------------------------------------------------------------------
	
	public void removeMapping(Integer hash, String groupName) {
		HashSet<String> ref = groupMapping.get(hash);
		if(ref != null) {
			ref.remove(groupName);
			groupMapping.put(hash, ref);
		}
	}
	
	public void removeMapping(Integer hash) {
		groupMapping.remove(hash);
	}
	
	public void removeGroup(String groupName) {
		for(Integer i : groupMapping.keySet()) {
			HashSet<String> get = groupMapping.get(i);
			get.remove(groupName);
			groupMapping.put(i, get);
		}
		groupOffsets.remove(groupName);
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setWindow(String groupName, int origin, int breadth, boolean isVert) {
		if(!groupWindows.containsKey(groupName)) {
			groupWindows.put(groupName, new WindowValues(origin, breadth, isVert));
		}
		else {
			groupWindows.get(groupName).update(origin, breadth, isVert);
		}
	}

	@Override
	public void setOffsetX(String groupName, int newOffsetX) {
		addGroup(groupName);
		groupOffsets.get(groupName).setOffsetX(newOffsetX);
	}

	@Override
	public void setOffsetY(String groupName, int newOffsetY) {
		addGroup(groupName);
		groupOffsets.get(groupName).setOffsetY(newOffsetY);
	}
	
	public void setGroupDrawSetting(String groupName, boolean set) {
		addGroup(groupName);
		groupWindows.get(groupName).setGroupDraw(set);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public boolean getGroupDrawSetting(String groupName) {
		if(!hasWindow(groupName)) {
			return false;
		}
		return groupWindows.get(groupName).getGroupDraw();
	}
	
	public boolean isPositionInWindowBounds(String groupName, int orig, int end, boolean isVert) {
		int windOrig = getWindowOrigin(groupName, isVert);
		int winEnd = windOrig + getWindowBreadth(groupName, isVert);
		WindowValues win = groupWindows.get(groupName);
		boolean out = (isVert ? !win.hasAssignedY() : !win.hasAssignedX()) || (orig >= windOrig && orig <= winEnd) || (end >= windOrig && end <= winEnd) || (windOrig >= orig && windOrig <= end) || (winEnd >= orig && winEnd <= end);
		return out;
	}
	
	public boolean hasWindow(String groupName) {
		return groupWindows.containsKey(groupName);
	}
	
	/**
	 * 
	 * A Group is defined across a single axis (vertical or horizontal), so this
	 * function gets the x or y coordinate that is the origin/initial point for
	 * the span managed by this group.
	 * 
	 * Currently it seems that a Group can only move along one axis, so that something
	 * like a Scrollbar being scrolled would contribute that movement only to vertical
	 * or horizontal translation.
	 * 
	 * @param groupName
	 * @param isVert
	 * @return
	 */
	
	public int getWindowOrigin(String groupName, boolean isVert) {
		if(hasWindow(groupName)) {
			return groupWindows.get(groupName).getOrigin(isVert);
		}
		return 0;
	}
	
	public int getWindowBreadth(String groupName, boolean isVert) {
		if(hasWindow(groupName)) {
			return groupWindows.get(groupName).getBreadth(isVert);
		}
		return 0;
	}
	
	public HashSet<String> getGroupMembership(String name) {
		return groupMapping.get(name.hashCode());
	}
	
	public HashSet<String> getGroups(Integer hash){
		return groupMapping.get(hash);
	}
	
	public boolean hasGroup(String groupName) {
		return groupOffsets.containsKey(groupName);
	}
	
	@Override
	public int getOffsetX(String groupName) {
		addGroup(groupName);
		return groupOffsets.get(groupName).getOffsetX();
	}

	@Override
	public int getOffsetY(String groupName) {
		addGroup(groupName);
		return groupOffsets.get(groupName).getOffsetY();
	}

	
}
