package input.manager;

import input.EventReceiver;

public class ActionEvent {

//---  Constants   ----------------------------------------------------------------------------
	
	public final static char EVENT_PRESS = 'P';
	public final static char EVENT_RELEASE = 'R';
	public final static char EVENT_CLICK = 'C';
	public final static char EVENT_DRAG = 'D';
	public final static char EVENT_MOUSE_MOVE = 'M';
	public final static char EVENT_WHEEL = 'W';
	public final static char EVENT_KEY = 'K';
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private int x;
	private int y;
	private int code;
	private char type;
	
	private int wheel;
	
	private char keyInput;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public ActionEvent(char in, int xIn, int yIn, int inCode) {
		x = xIn;
		y = yIn;
		code = inCode;
		type = in;
	}
	
	public ActionEvent(char in, int inWheel) {
		type = in;
		wheel = inWheel;
	}
	
	public ActionEvent(char in, char key) {
		type = in;
		keyInput = key;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void execute(EventReceiver reference) {
		if(reference == null) {
			return;
		}
		switch(type) {
			case EVENT_KEY:
				reference.keyEvent(keyInput);
				break;
			case EVENT_PRESS:
				reference.clickPressEvent(code, x, y);
				break;
			case EVENT_RELEASE:
				reference.clickReleaseEvent(code, x, y);
				break;
			case EVENT_CLICK:
				reference.clickEvent(code, x, y);
				break;
			case EVENT_DRAG:
				reference.dragEvent(code, x, y);
				break;
			case EVENT_MOUSE_MOVE:
				reference.mouseMoveEvent(x, y);
				break;
			case EVENT_WHEEL:
				reference.mouseWheelEvent(wheel);
				break;
		}
	}
	
}