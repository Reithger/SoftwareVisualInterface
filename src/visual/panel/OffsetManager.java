package visual.panel;

public interface OffsetManager {

	public abstract int getOffsetX(String groupName);
	
	public abstract int getOffsetY(String groupName);
	
	public abstract void setOffsetX(String groupName, int offset);
	
	public abstract void setOffsetY(String groupName, int offset);
	
	public abstract int getMinimumScreenX(String groupName);
	
	public abstract int getMinimumScreenY(String groupName);
	
	public abstract int getMaximumScreenX(String groupName);
	
	public abstract int getMaximumScreenY(String groupName);
	
}
