package com.github.softwarevisualinterface.visual.panel.group;

public class WindowValues {

//---  Instance Variables   -------------------------------------------------------------------
	
	private int originX;
	
	private int originY;
	
	private int breadthX;
	
	private int breadthY;
	
	private boolean groupDraw;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public WindowValues() {
		groupDraw = true;
		originX = -1;
		originY = -1;
		breadthY = -1;
		breadthX = -1;
	}
	
	public WindowValues(int orig, int bre, boolean isVert) {
		if(isVert) {
			originY = orig;
			breadthY = bre < 0 ? 0 : bre;
			originX = -1;
			breadthX = -1;
		}
		else {
			originX = orig;
			breadthX = bre < 0 ? 0 : bre;
			originY = -1;
			breadthY = -1;
		}
		groupDraw = true;
	}
	
	public WindowValues(int origX, int origY, int breX, int breY) {
		originX = origX;
		breadthX = breX < 0 ? 0 : breX;
		originY = origY;
		breadthY = breY < 0 ? 0 : breY;
		groupDraw = true;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public int getOrigin(boolean isVert) {
		return isVert ? originY : originX;
	}
	
	public int getBreadth(boolean isVert) {
		return isVert ? breadthY : breadthX;
	}
	
	public boolean getGroupDraw() {
		return groupDraw;
	}
	
	public boolean hasAssignedX() {
		return breadthX != -1;
	}
	
	public boolean hasAssignedY() {
		return breadthY != -1;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void update(int orig, int bre, boolean isVert) {
		setOrigin(orig, isVert);
		setBreadth(bre, isVert);
	}
	
	public void setOrigin(int in, boolean isVert) {
		if(isVert) {
			originY = in;
		}
		else {
			originX = in;
		}
	}
	
	public void setBreadth(int in, boolean isVert) {
		in = in < 0 ? 0 : in;
		if(isVert) {
			breadthY = in;
		}
		else {
			breadthX = in;
		}
	}
	
	public void setGroupDraw(boolean set) {
		groupDraw = set;
	}
	
}
