package test;

import input.NestedEventReceiver;
import visual.composite.ImageDisplay;
import visual.frame.WindowFrame;
import visual.panel.ElementPanel;

public class TestImageDisplay {

	public static void main(String[] args) {
		WindowFrame fra = new WindowFrame(500, 500);
		ElementPanel pa = new ElementPanel(0, 0, 500, 500);
		ImageDisplay iD = new ImageDisplay("src\\test\\assets\\ada.png", pa);
		pa.setEventReceiver(new NestedEventReceiver(iD.generateEventReceiver()));
		pa.setScrollBarHorizontal(false);
		pa.setScrollBarVertical(false);
		fra.reserveWindow("default");
		fra.showActiveWindow("default");
		fra.addPanelToWindow("default", "pan", pa);
		fra.setName("Test Image Display");
		iD.autofitImage();
		iD.refresh();
	}
	
}
