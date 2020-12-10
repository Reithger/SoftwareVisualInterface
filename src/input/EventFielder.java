package input;

import java.awt.Component;

import input.keyboard.KeyComponent;
import input.manager.ActionEvent;
import input.manager.ActionEventManager;
import input.mouse.ClickComponent;
import input.mouse.Detectable;

public class EventFielder {
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private ClickComponent mouseEvent;
	private KeyComponent keyPress;
	private ActionEventManager eventManager;
	private EventReceiver reference;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public EventFielder(EventReceiver parent) {
		mouseEvent = new ClickComponent(this);
		keyPress = new KeyComponent(this);
		reference = parent;
		Component reception = parent.getListenerRecipient();
		
		reception.addMouseListener(mouseEvent);
		reception.addMouseMotionListener(mouseEvent);
		reception.addMouseWheelListener(mouseEvent);
		reception.addKeyListener(keyPress);
		startQueueThread();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	private void startQueueThread() {
		if(eventManager == null) {
			eventManager = new ActionEventManager(reference);
			eventManager.start();
		}
		else if(!eventManager.isAlive()) {
			eventManager.run();
		}
	}
	
	public void receiveActionEvent(ActionEvent in) {
		eventManager.add(in);
		startQueueThread();
	}
	
	public void requestFocusInWindow() {
		reference.requestFocusInWindow();
	}
	
	public void resetDetectionRegions() {
		mouseEvent.resetDetectionRegions();
	}
	
//---  Adder Methods   ------------------------------------------------------------------------
	
	public void addClickRegion(Detectable detect) {
		mouseEvent.addClickRegion(detect);
	}
	
//---  Remover Methods   ----------------------------------------------------------------------
	
	public boolean removeDetectionRegion(int code) {
		return mouseEvent.removeDetectionRegion(code);
	}
	
	public boolean removeDetectionRegions(int x, int y) {
		return mouseEvent.removeDetectionRegions(x,  y);
	}
	
}
