package input.manager.actionevent;

public class ActionEventGenerator {

//---  Constants   ----------------------------------------------------------------------------
	
	public final static int KEY_DOWN = KeyActionEvent.EVENT_KEY_DOWN;
	public final static int KEY_UP = KeyActionEvent.EVENT_KEY_UP;
	public final static int KEY_PRESS = KeyActionEvent.EVENT_KEY;
	
	public final static int MOUSE_PRESS = MouseActionEvent.EVENT_PRESS;
	public final static int MOUSE_CLICK = MouseActionEvent.EVENT_CLICK;
	public final static int MOUSE_RELEASE = MouseActionEvent.EVENT_RELEASE;
	public final static int MOUSE_DRAG = MouseActionEvent.EVENT_DRAG;
	public final static int MOUSE_MOVE = MouseActionEvent.EVENT_MOVE;
	
//---  Operations   ---------------------------------------------------------------------------
	
	public static ActionEvent generateKeyActionEvent(int type, char key) {
		return new KeyActionEvent(type, key);
	}
	
	public static ActionEvent generateMouseWheelActionEvent(int scroll) {
		return new MouseWheelActionEvent(scroll);
	}
	
	public static ActionEvent generateMouseActionEvent(int type, int code, int x, int y, int button) {
		return new MouseActionEvent(type, x, y, code, button);
	}
	
}
