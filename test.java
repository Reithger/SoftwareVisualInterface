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
		System.out.println("Here");
		WindowFrame fram = new WindowFrame(1200, 500);

		System.out.println("Here");
		
		ElementPanel pan = new ElementPanel(0, 0, 550, 500) {
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
		pan.addRectangle("rec", 0, 300, 150, 100, 250, true, new Color(80, 80, 80));
		pan.addText("tex", 10, 300, 150, 100, 250, "Welcome to this long phrase I will write", new Font("Arial Bold", Font.BOLD, 18), true);
		
		designReactiveButton(pan, "cont", new Color(255, 255, 255), new Font("Arial Bold", Font.BOLD, 14), "Start", pan.getWidth() / 2, pan.getHeight() / 2, pan.getWidth() / 8, pan.getHeight() / 10, 5, 6, true);
//		pan.addTextEntry("ent", 5, 15, 15, 500, 250, 1, new Font("Arial Bold", Font.BOLD, 18), "words", true);
		//pan.addRectangle("rec", 0, 300, 150, 100, 250, false, new Color(255,255,255), new Color(0,0,0));
		ElementPanel pan2 = new ElementPanel(600, 0, 400, 400) {
			public void clickBehaviour(int event) {
				Communication.set("A", "Second");
			}
		};
		pan2.addImage("sas", 100, pan2.getWidth() / 2, pan2.getHeight() / 2, true, imagePath);
		fram.reservePanel("fir", pan);
		fram.reservePanel("sas", pan2);
		}
	
	private static void designReactiveButton(ElementPanel pan, String name, Color col, Font font, String message, int x, int y, int wid, int hei, int priority, int code, boolean centered) {
		pan.addRectangle(name + "_rect", priority * 10, x, y, wid, hei, centered, col);
		pan.addButton(name + "_but",     priority * 10 + 1, x, y, wid, hei, code, centered);
		pan.addText(name + "_text_but",  priority * 10 + 2, x, y,  wid, hei, message, font, centered);
	}
	
}