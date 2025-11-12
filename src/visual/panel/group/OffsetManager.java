package visual.panel.group;

public interface OffsetManager {

//---  Getter Methods   -----------------------------------------------------------------------
	
	public abstract int getOffsetX(String groupName);
	
	public abstract int getOffsetY(String groupName);

//---  Setter Methods   -----------------------------------------------------------------------
	
	public abstract void setOffsetX(String groupName, int offset);
	
	public abstract void setOffsetY(String groupName, int offset);
	
}
