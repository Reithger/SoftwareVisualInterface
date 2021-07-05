package input;

import java.awt.Component;

import input.keyboard.KeyComponent;
import input.manager.ActionEventManager;
import input.manager.actionevent.ActionEvent;
import input.mouse.ClickComponent;
import input.mouse.Detectable;

public class EventFielder {
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private ClickComponent mouseEvent;
	private KeyComponent keyPress;
	private ActionEventManager eventManager;
	private ComponentReceiver reference;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public EventFielder(ComponentReceiver parent) {
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
	
	public void addClickRegion(int identity, Detectable detect) {
		mouseEvent.addClickRegion(identity, detect);
	}
	
//---  Remover Methods   ----------------------------------------------------------------------
	
	public boolean removeDetectionRegion(int identity) {
		return mouseEvent.removeDetectionRegion(identity);
	}
	
	public boolean removeDetectionRegions(int x, int y) {
		return mouseEvent.removeDetectionRegions(x,  y);
	}
	
}
