package visual.frame;

import java.util.HashMap;
import visual.panel.Panel;

/**
 * This class implements the Frame abstract class to store a collection of named Panel objects
 * that can be hidden and shown by referencing their names.
 * 
 * @author Mac Clevinger
 *
 */

public class WindowFrame extends Frame{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** HashMap<<r>String, Panel> object containing the Panel objects contained by this WindowFrame and their names*/
	private HashMap<String, Panel> windows;

	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * Constructor for objects of the WindowFrame type; assigns provided size for the WindowFrame, calls
	 * the initiate() function from abstract class Frame, and initializes the HashMap<<r>String, Panel>
	 * for the Panel objects held by this WindowFrame.
	 * 
	 * @param width - int value representing the width of this WindowFrame
	 * @param height - int value representing the height of this WindowFrame
	 */
	
	public WindowFrame(int width, int height) {
		super();
		initiate(width, height);
		windows = new HashMap<String, Panel>();
	}

//---  Adder Methods   ------------------------------------------------------------------------
	
	/**
	 * This method adds a Panel object to this WindowFrame object, creating an entry
	 * in the HashMap<<r>String, Panel> for manipulation.
	 * 
	 * By default added Panels are visible; location of the Panel is handled by the
	 * Panel (call setLocation(int, int)).
	 * 
	 */
	
	public void addPanel(String title, Panel panel) {
		windows.put(title, panel);
		this.add(panel);
		panel.setParentFrame(this);
	}
	
//---  Remover Methods   ----------------------------------------------------------------------
	
	/**
	 * This method removes a Panel object specified by a name represented as a String object from
	 * this WindowFrame, removing it both from immediate view and the stored HashMap<<r>String, Panel>
	 * that contains all Panels associated to this WindowFrame object.
	 * 
	 * @param name - String object representing the name of the Panel to remove from this WindowFrame.
	 */
	
	public void removePanel(String name) {
		try {
			remove(windows.get(name));
			windows.get(name).setParentFrame(null);
			windows.remove(name);
		}
		catch(Exception e) {
			System.out.println("Error: Attempt to remove non-existant Panel object");
		}
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * This method removes a designated Panel object from the Frame, but keeps
	 * it in the stored HashMap<<r>String, Panel> for re-inclusion at a later time.
	 * 
	 * @param name - String object representing the name associated to a Panel object stored by this WindowFrame.
	 */
	
	public void hidePanel(String name) {
		try {
			remove(windows.get(name));
		}
		catch(Exception e) {
			System.out.println("Error: Attempt to hide non-existant Panel object");
		}
	}
	
	/**
	 * This method adds a designated Panel object to the Frame, accessing the stored
	 * HashMap<<r>String, Panel> via the provided name to find the Panel to add.
	 * 
	 * @param name - String object representing the name associated to a Panel object stored by this WindowFrame.
	 */
	
	public void showPanel(String name) {
		try {
			add(windows.get(name));
		}
		catch(Exception e) {
			System.out.println("Error: Attempt to show non-existant Panel object");
		}
	}
	
}
