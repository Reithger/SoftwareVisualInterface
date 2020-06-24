package test;

import java.awt.Color;
import java.awt.Font;

import input.Communication;
import visual.frame.WindowFrame;
import visual.panel.CanvasPanel;
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
				System.out.println("Char: " + event);
				System.out.println(getFocusElement() == null ? "Null focus" : "Code: " + getFocusElement().getCode());
				removeElement("rec");
				removeElementPrefixed("lin");
				Communication.set("A", event+"");
				System.out.println(event);
			}
			
			public void clickBehaviour(int event, int x, int y) {
				System.out.println("Event: " + event);

				System.out.println(Communication.get("A"));
			} 
			
		};
		
		CanvasPanel can = new CanvasPanel(600, 0, 400, 400, 15);
		can.setColor(new Color(122, 122, 122));
		pan.addLine("line", 150, 50, 50, 50, 100, 10, Color.BLUE);
		pan.addLine("line2", 1, 50, 50, 100, 50, 10, Color.BLUE);
		pan.addLine("line3", 10, 50, 50, 170, 170, 10, Color.BLUE);
		pan.addLine("line4", 10, 50, 170, 170, 50, 10, Color.BLUE);
		pan.addLine("line4", 10, 50, 50, 170, 100, 10, Color.BLUE);
		pan.addRectangle("rec", 8, 150, 150, 120, 120, true, new Color(180, 0, 180));
		pan.addLine("line5", 9, 90, 150, 210, 150, 2, Color.BLUE);
		//pan.addRectangle("rec_1", 3, 300, 150, 250, 100, true, new Color(80, 180, 80));
		pan.addTextEntry("tex", 10, 150, 150, 120, 120, 3, "Welcome\nWelcome", new Font("Arial Bold", Font.BOLD, 18), true, true, true);
		//pan.addRectangle("rec", 0, 0, 0, pan.getWidth(), pan.getHeight(), false,  new Color(190, 190, 190));
		//pan.addRectangle("rec2", 0, 10, 10, pan.getWidth() - 20, pan.getHeight() - 20, false,  new Color(55, 55, 55));
//		designReactiveButton(pan, "cont", new Color(200, 30, 30), new Font("Arial Bold", Font.BOLD, 14), "Start", pan.getWidth() / 2, pan.getHeight() / 2, 200, 80, 5, 6, true, true);
//		pan.addTextEntry("ent", 5, 15, 15, 500, 250, 1, new Font("Arial Bold", Font.BOLD, 18), "words", true);
		//pan.addRectangle("rec", 0, 300, 150, 100, 250, false, new Color(255,255,255), new Color(0,0,0));
		
		ElementPanel pan2 = new ElementPanel(600, 0, 400, 400) {
			public void clickBehaviour(int event, int x, int y) {
				System.out.println(event);
				Communication.set("A", "Second");
				//pan.removeElementPrefixed("rec");
			}
			
			public void keyBehaviour(char event) {
				System.out.println(event);
			}
		};
		//pan2.addImage("sas", 100, pan2.getWidth() / 2, pan2.getHeight() / 2, true, imagePath, 2);
		pan2.addRectangle("rec", 102, 350, 150, 120, 120, true, new Color(180, 0, 180));
		pan2.addTextEntry("tex2", 105, 350, 150, 120, 120, 4, "Welcome to\n this long ph\nrase\n I will write", new Font("Arial Bold", Font.BOLD, 18), true, true, true);

		fram.reserveWindow("main");
		fram.reservePanel("main", "fir", pan);
		fram.reservePanel("main", "sas", can);
		//fram.reservePanel("main", "sec", pan2);
		}
	
	private static void designReactiveButton(ElementPanel pan, String name, Color col, Font font, String message, int x, int y, int wid, int hei, int priority, int code, boolean centered, boolean centeredText) {
		pan.addRectangle(name + "_rect", priority * 10, x, y, wid, hei, centered, col);
		pan.addButton(name + "_but",     priority * 10 + 1, x, y, wid, hei, code, centered);
		pan.addText(name + "_text_but",  priority * 10 - 2, x, y,  wid, hei, message, font, centered, centered, centeredText);
	}
	
}