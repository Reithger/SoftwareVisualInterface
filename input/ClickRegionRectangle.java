package input;

public class ClickRegionRectangle implements Detectable{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private int xHigh;
	/** */
	private int xLow;
	/** */
	private int yHigh;
	/** */
	private int yLow;
	/** */
	private int code;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 * @param in
	 */
	
	public ClickRegionRectangle(int[] in) {
		xHigh = in[0];
		yHigh = in[1];
		xLow = in[2];
		yLow = in[3];
		code = in[4];
	}
	
	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param inCode
	 */
	
	public ClickRegionRectangle(int lowX, int lowY, int highX, int highY, int inCode) {
		xLow = lowX;
		yLow = lowY;
		xHigh = highX;
		yHigh = highY;
		code = inCode;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	@Override
	public boolean wasClicked(int xPos, int yPos) {
		return xPos <= xHigh && xPos >= xLow && yPos <= yHigh && yPos >= yLow;
	}
	
	@Override
	public int getCode() {
		return code;
	}
	
}
