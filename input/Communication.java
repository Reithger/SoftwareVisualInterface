package input;

/**
 * This static class permits String information to be shared between disjoint classes
 * without having explicit relationships between one another; intention is for usage
 * when defining the nature of a Panel object's interactivity to the broader project.
 * 
 * In a project using the SWI library, the broader project can have information communicated
 * to it by getting the information stored in this class after the Panel object has had
 * its predefined behavior store information here.
 * 
 * (Static means all references to this class are the same, so changing it during a run-time
 * changes it for all future uses; call methods by Communication.set() or Communication.get(),
 * you never need to generate a copy of this class manually.)
 * 
 * So just Communication.set(String information) within the Panel's defined method, and use
 * Communication.get() in the project to get that information (presumably user-submitted info
 * that exists locally to the Panel.)
 * 
 * @author Mac Clevinger
 *
 */

public class Communication {

//---  Instance Variables   -------------------------------------------------------------------
	
	/** Static String object representing the data stored by one class scope for usage by another class' scope*/
	private static String data;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * Constructor for objects of the Communication type; protected to disallow explicit creation of
	 * Communication objects; all usage is by static methods. ('Communication.method_name()' style)
	 * 
	 */
	
	protected Communication() {
		data = "";
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * Getter method to access whatever data is stored internally by the static Communication object.
	 * 
	 * @return - Returns a String object representing the information stored by the static Communication object.
	 */
	
	public static String get() {
		return data;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	/**
	 * Setter method to assign a new String object as the data stored internally by the static Communication object.
	 * 
	 * @param in - String object representing the new data to be stored by the static Communication object.
	 */
	
	public static void set(String in) {
		data = in;
	}
	
}