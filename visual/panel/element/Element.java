package visual.panel.element;

import java.awt.Graphics;

public abstract class Element implements Comparable<Element>{

	private int priority;
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * 
	 * @param g
	 */
	
	public abstract void drawToScreen(Graphics g);
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * 
	 * @return
	 */
	
	public int getDrawPriority() {
		return priority;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setDrawPriority(int in) {
		priority = in;
	}
	
	@Override
	public int compareTo(Element d) {
		int a = this.getDrawPriority();
		int b = d.getDrawPriority();
		if(a > b)
			return 1;
		if(a < b)
			return -1;
		return 0;
	}
}
