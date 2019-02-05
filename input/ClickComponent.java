package input;
import javax.swing.*;

import visual.panel.Panel;

import java.awt.event.*;
import java.util.*;

public class ClickComponent extends JComponent implements MouseListener{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** int value representing the coded value for which event-region is currently selected*/
	private int activeSelect;
	/** ArrayList<Integer[]> object containing the coordinates and codes for each event-region*/
	private ArrayList<Detectable> detectionRegions;
	/** Panel object representing the Panel to which this ClickComponent is attached (the Panel that is being clicked)*/
	private Panel containerFrame;

//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * Constructor for objects of the ClickComponent object; default selected value is -1,
	 * 
	 */
	
	public ClickComponent(Panel panel){
		resetSelected();
		detectionRegions = new ArrayList<Detectable>();
		containerFrame = panel;
		panel.addMouseListener(this);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * 
	 * @return
	 */
	
	public int getSelected(){
		int out = activeSelect;
		resetSelected();
		return out;
	}
	
//---  Operations   ---------------------------------------------------------------------------

	/**
	 * 
	 */
	
	public void resetSelected(){
		activeSelect = -1;
	}

	/**
	 * 
	 */
	
	public void resetDetectionRegions() {
		detectionRegions = new ArrayList<Detectable>();
	}

//---  Remover Methods   ----------------------------------------------------------------------
	
	/**
	 * 
	 */
	
	public boolean removeDetectionRegion(int code) {
		for(int i = 0; i < detectionRegions.size(); i++) {
			Detectable d = detectionRegions.get(i);
			if(d.getCode() == code) {
				detectionRegions.remove(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 */
	
	public boolean removeDetectionRegions(int x, int y) {
		boolean out = false;
		for(int i = 0; i < detectionRegions.size(); i++) {
			Detectable d = detectionRegions.get(i);
			if(d.wasClicked(x, y)) {
				detectionRegions.remove(i);
				out = true;
			}
		}
		return out;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------

	/**
	 * 
	 */
	
	public void setDetectionRegions(ArrayList<Detectable> updated){
		detectionRegions = updated;
	}

	/**
	 * 
	 */
	
	public void setParentFrame(Panel reference){
		containerFrame = reference;
	}
	
//---  Adder Methods   ------------------------------------------------------------------------
	
	public boolean addClickRegion(Detectable region){
		for(Detectable d : detectionRegions) {
			if(d.getCode() == region.getCode()) {
				return false;
			}
		}
		detectionRegions.add(region);
		return true;
	}
	
//---  Events   -------------------------------------------------------------------------------
	
	@Override
	public void mouseClicked(MouseEvent e){
		containerFrame.requestFocusInWindow();
		Integer x = e.getX();
		Integer y = e.getY();
		for(Detectable d : detectionRegions) {
			if(d.wasClicked(x, y))
				activeSelect = d.getCode();
		}
		containerFrame.clickEvent(getSelected());
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		//This space intentionally left blank, but doesn't need to be
	}
	
	@Override
	public void mouseEntered(MouseEvent e){
		//This space intentionally left blank, but doesn't need to be
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		//This space intentionally left blank, but doesn't need to be
	}
	
	@Override
	public void mouseExited(MouseEvent e){
		//This space intentionally left blank, but doesn't need to be
	}

}
