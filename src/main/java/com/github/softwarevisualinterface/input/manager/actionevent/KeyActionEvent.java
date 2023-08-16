package com.github.softwarevisualinterface.input.manager.actionevent;

import com.github.softwarevisualinterface.input.EventReceiver;

public class KeyActionEvent implements ActionEvent{

//---  Constants   ----------------------------------------------------------------------------
	
	public final static int EVENT_KEY = 0;
	public final static int EVENT_KEY_DOWN = 1;
	public final static int EVENT_KEY_UP = 2;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private int type;
	private char key;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public KeyActionEvent(int inType, char inKey) {
		key = inKey;
		type = inType;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void execute(EventReceiver reference) {
		switch(type) {
			case EVENT_KEY:
				reference.keyEvent(key);
				break;
			case EVENT_KEY_DOWN:
				reference.keyPressEvent(key);
				break;
			case EVENT_KEY_UP:
				reference.keyReleaseEvent(key);
				break;
		}
	}
	
}
