package com.github.softwarevisualinterface.input.manager.actionevent;

import com.github.softwarevisualinterface.input.EventReceiver;

public interface ActionEvent {

//---  Operations   ---------------------------------------------------------------------------
	
	public abstract void execute(EventReceiver reference);
	
}