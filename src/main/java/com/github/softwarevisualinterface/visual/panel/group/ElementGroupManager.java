package com.github.softwarevisualinterface.visual.panel.group;

import java.util.HashMap;
import java.util.HashSet;

public class ElementGroupManager {

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
	
	public void setOffsetX(String groupName, int newOffsetX) {
		addGroup(groupName);
		groupOffsets.get(groupName).setOffsetX(newOffsetX);
	}
	
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
	
	public HashSet<String> getGroups(Integer hash){
		return groupMapping.get(hash);
	}
	
	public boolean hasGroup(String groupName) {
		return groupOffsets.containsKey(groupName);
	}
	
	public int getOffsetX(String groupName) {
		addGroup(groupName);
		return groupOffsets.get(groupName).getOffsetX();
	}
	
	public int getOffsetY(String groupName) {
		addGroup(groupName);
		return groupOffsets.get(groupName).getOffsetY();
	}
	
}
