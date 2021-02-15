package input;

public class NestedEventReceiver implements EventReceiver{

	private EventReceiver nested;
	private NestedEventReceiver next;
	
	public NestedEventReceiver(EventReceiver in) {
		nested = in;
		next = null;
	}
	
	public void addNested(EventReceiver in) {
		next = new NestedEventReceiver(in);
	}

	@Override
	public void clickEvent(int event, int x, int y, int clickType) {
		nested.clickEvent(event, x, y, clickType);
		if(next != null)
			next.clickEvent(event, x, y, clickType);
	}

	@Override
	public void clickReleaseEvent(int event, int x, int y, int clickType) {
		nested.clickReleaseEvent(event, x, y, clickType);
		if(next != null)
			next.clickReleaseEvent(event, x, y, clickType);
	}

	@Override
	public void clickPressEvent(int event, int x, int y, int clickType) {
		nested.clickPressEvent(event, x, y, clickType);
		if(next != null)
			next.clickPressEvent(event, x, y, clickType);
	}

	@Override
	public void dragEvent(int event, int x, int y, int clickType) {
		nested.dragEvent(event, x, y, clickType);
		if(next != null)
			next.dragEvent(event, x, y, clickType);
	}

	@Override
	public void mouseMoveEvent(int event, int x, int y) {
		nested.mouseMoveEvent(event, x, y);
		if(next != null)
			next.mouseMoveEvent(event, x, y);
	}

	@Override
	public void mouseWheelEvent(int rotation) {
		nested.mouseWheelEvent(rotation);
		if(next != null)
			next.mouseWheelEvent(rotation);
	}

	@Override
	public void keyEvent(char event) {
		nested.keyEvent(event);
		if(next != null)
			next.keyEvent(event);
	}

	@Override
	public void keyPressEvent(char event) {
		nested.keyPressEvent(event);
		if(next != null)
			next.keyPressEvent(event);
	}

	@Override
	public void keyReleaseEvent(char event) {
		nested.keyReleaseEvent(event);
		if(next != null)
			next.keyReleaseEvent(event);
	}

	@Override
	public void focusEventReaction(int code) {
		nested.focusEventReaction(code);
		if(next != null)
			next.focusEventReaction(code);
	}

	
}
