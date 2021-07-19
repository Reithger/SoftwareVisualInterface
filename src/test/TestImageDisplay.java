package test;

import input.CustomEventReceiver;
import input.NestedEventReceiver;
import visual.composite.ImageDisplay;
import visual.frame.WindowFrame;
import visual.panel.ElementPanel;

public class TestImageDisplay {

	public static void main(String[] args) {
		WindowFrame fra = new WindowFrame(500, 500);
		ElementPanel pa = new ElementPanel(0, 0, 500, 500);
		ImageDisplay iD = new ImageDisplay("src\\test\\assets\\ada.png", pa);
		pa.addEventReceiver("label1", iD.generateEventReceiver());
		pa.addEventReceiver(new CustomEventReceiver() {
			
			@Override
			public void clickEvent(int code, int x, int y, int mouseType) {
				System.out.println("Here");
				iD.setImage("src\\test\\assets\\Saskia_Portrait.jpg");
				iD.refresh();
				if(x < y) {
					pa.removeEventReceiver("label1");
				}
			}
			
		});
		fra.reserveWindow("default");
		fra.showActiveWindow("default");
		fra.addPanelToWindow("default", "pan", pa);
		fra.setName("Test Image Display");
		iD.autofitImage();
		iD.refresh();
	}
	
}
