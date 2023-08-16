package com.github.softwarevisualinterface.visual.frame.timer;

import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.softwarevisualinterface.visual.frame.Frame;

/**
 * This class extends the TimerTask class to define behaviors for managing the
 * repainting of a visual Frame object; used to define a repeated task in tandem
 * with the Timer class, specifically for repainting the provided Frame object.
 * 
 * Used in the background of the Frame abstract class.
 * 
 * @author Ada Clevinger
 *
 */

public class TimerRefresh extends TimerTask{

	private static Logger logger = LogManager.getLogger();

//---  Instance Variables   -------------------------------------------------------------------
	
	/** Frame object designating the object that this TimerRefresh is associated to; will repaint() this object*/
	private Frame parent;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * Constructor for objects of the TimerRefresh type that assigns the provided
	 * Frame object as the object to be repaint()ed.
	 * 
	 * @param par - Frame object that this TimerRefresh object will repaint().
	 */
	
	public TimerRefresh(Frame par){
		super();
		parent = par;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void run(){
		try {
			parent.repaint();
		}
		catch(Exception e) {
			logger.catching(e);
		}
	}

}
