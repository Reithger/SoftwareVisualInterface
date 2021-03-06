package input.manager.actionevent;

import input.EventReceiver;

public class MouseWheelActionEvent implements ActionEvent{

//---  Instance Variables   -------------------------------------------------------------------
	
	private int scroll;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public MouseWheelActionEvent(int inScroll) {
		scroll = inScroll;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void execute(EventReceiver reference) {
		reference.mouseWheelEvent(scroll);
	}
	
}
