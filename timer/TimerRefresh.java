package timer;

import java.util.TimerTask;

import visual.frame.Frame;
import visual.panel.Panel;

public class TimerRefresh extends TimerTask{

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private Frame parent;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 * @param par
	 */
	
	public TimerRefresh(Frame par){
		parent = par;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * 
	 */
	
	public void run(){
		parent.repaint();
	}
}
