package input.manager.actionevent;

import input.EventReceiver;

public interface ActionEvent {

//---  Operations   ---------------------------------------------------------------------------
	
	public abstract void execute(EventReceiver reference);
	
}