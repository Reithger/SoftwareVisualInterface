package input;

import java.awt.Component;

public interface ComponentReceiver extends EventReceiver{
	
//---  Operations   ---------------------------------------------------------------------------
	
	public abstract void requestFocusInWindow();
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public abstract Component getListenerRecipient();
	
}
