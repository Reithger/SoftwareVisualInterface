package input;

public class NestedEventReceiver implements EventReceiver{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static String IDENTITY_EMPTY = null;
	private final static String IDENTITY_UNNAMED = "";
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private EventReceiver nested;
	private NestedEventReceiver next;
	private String identifier;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public NestedEventReceiver(EventReceiver in) {
		nested = in;
		next = null;
		identifier = IDENTITY_UNNAMED;
	}
	
	public NestedEventReceiver(EventReceiver in, String identity) {
		nested = in;
		next = null;
		identifier = identity;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void addNested(NestedEventReceiver in) {
		if(next == null) {
			next = in;
		}
		else {
			next.addNested(in);
		}
	}
	
	public void addNested(EventReceiver in) {
		addNested(in, IDENTITY_UNNAMED);
	}
	
	public void addNested(EventReceiver in, String identity) {
		if(identifier == IDENTITY_EMPTY) {
			nested = in;
			identifier = identity;
		}
		else if(next != null) {
			next.addNested(in, identity);
		}
		else {
			next = new NestedEventReceiver(in, identity);
		}
	}
	
	public void removeNestedEventReceiver(String identity) {
		if(identifier != IDENTITY_EMPTY && identifier.equals(identity)) {
			removeCurrentNestedEventReceiver();
		}
		else if(next != null) {
			if(identifier == IDENTITY_EMPTY) {
				removeCurrentNestedEventReceiver();
				removeNestedEventReceiver(identity);
			}
			else {
			  next.removeNestedEventReceiver(identity);
			}
		}
	}
	
	private void removeCurrentNestedEventReceiver() {
		if(next != null) {
			nested = next.getEventReceiver();
			identifier = next.getIdentity();
			next = next.getNextNestedEventReceiver();
		}
		else {
			nested = new CustomEventReceiver();
			identifier = IDENTITY_EMPTY;
		}
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	private String getIdentity() {
		return identifier;
	}
	
	private NestedEventReceiver getNextNestedEventReceiver() {
		return next;
	}

	private EventReceiver getEventReceiver() {
		return nested;
	}
	
//---  Input Handling   -----------------------------------------------------------------------
	
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
