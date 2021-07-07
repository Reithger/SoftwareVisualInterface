package visual.panel.group;

public class OffsetValues {
	
	private int offsetX;
	private int offsetY;
	
	public OffsetValues() {
		offsetX = 0;
		offsetY = 0;
	}
	
	public OffsetValues(int x, int y) {
		offsetX = x;
		offsetY = y;
	}
	
	public int getOffsetX() {
		return offsetX;
	}
	
	public int getOffsetY() {
		return offsetY;
	}
	
	public void setOffsetX(int in) {
		offsetX = in;
	}
	
	public void setOffsetY(int in) {
		offsetY = in;
	}
	
}
