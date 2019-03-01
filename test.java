import java.awt.Color;
import java.awt.Font;

import input.Communication;
import visual.frame.WindowFrame;
import visual.panel.ElementPanel;

public class test {
	
	/*
	 * TODO: Scroll wheel type thing for increasing screen space availability; Frame origin x, y values
	 * that can change, Panel's placed accordingly to the x, y bias.
	 * 
	 * 
	 * 
	 *
	 */

	public static void main(String[] args) {
		String imagePath = "src\\Saskia_Portrait.jpg";
		WindowFrame fram = new WindowFrame(1200, 500);
		ElementPanel pan = new ElementPanel(0, 0, 550, 290) {
			public void keyBehaviour(char event) {
				removeElement("rec");
				Communication.set("A", event+"");
			}
			
			public void clickBehaviour(int event) {
				if(event == -1) {
					System.out.println(this.getElementStoredText("ent"));
				}
				System.out.println(Communication.get("A"));
			} 
			
		};
		pan.addText("tex", 10, 300, 250, 500, 250, "Wel", new Font("Arial Bold", Font.BOLD, 18), true);
		pan.addTextEntry("ent", 5, 15, 15, 500, 250, 1, new Font("Arial Bold", Font.BOLD, 18), false);
		pan.addRectangle("rec", 0, 15, 15, 500, 250, new Color(255,255,255), new Color(0,0,0), false);
		ElementPanel pan2 = new ElementPanel(600, 0, 400, 400) {
			public void clickBehaviour(int event) {
				Communication.set("A", "Second");
			}
		};
		pan2.addImage("sas", 100, 200, 200, imagePath);
		fram.addPanel("fir", pan);
		fram.addPanel("sas", pan2);
		}
	
}