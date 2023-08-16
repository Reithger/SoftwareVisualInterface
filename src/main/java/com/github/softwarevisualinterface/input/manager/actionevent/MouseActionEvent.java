package com.github.softwarevisualinterface.input.manager.actionevent;

import com.github.softwarevisualinterface.input.EventReceiver;

public class MouseActionEvent implements ActionEvent{

//---  Constants   ----------------------------------------------------------------------------
	
	public final static int EVENT_PRESS = 0;
	public final static int EVENT_RELEASE = 1;
	public final static int EVENT_CLICK = 2;
	public final static int EVENT_DRAG = 3;
	public final static int EVENT_MOVE = 4;
	
	public final static int CLICK_TYPE_LEFT = 1;
	public final static int CLICK_TYPE_MIDDLE = 2;
	public final static int CLICK_TYPE_RIGHT = 3;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private int type;
	private int x;
	private int y;
	private int code;
	private int button;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public MouseActionEvent(int inType, int inX, int inY, int inCode, int inButton) {
		type = inType;
		x = inX;
		y = inY;
		code = inCode;
		button = inButton;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void execute(EventReceiver reference) {
		switch(type) {
			case EVENT_PRESS:
				reference.clickPressEvent(code, x, y, button);
				break;
			case EVENT_RELEASE:
				reference.clickReleaseEvent(code, x, y, button);
				break;
			case EVENT_CLICK:
				reference.clickEvent(code, x, y, button);
				break;
			case EVENT_DRAG:
				reference.dragEvent(code, x, y, button);
				break;
			case EVENT_MOVE:
				reference.mouseMoveEvent(code, x, y);
				break;
		}
	}
	
}
