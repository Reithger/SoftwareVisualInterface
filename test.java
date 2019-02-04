import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import visual.frame.WindowFrame;
import visual.panel.ElementPanel;

public class test {

	public static void main(String[] args) {
		String imagePath = "src\\Saskia_Portrait.jpg";
		WindowFrame fram = new WindowFrame(1200, 500);
		ElementPanel pan = new ElementPanel(0, 0, 550, 290) {
			public void keyBehaviour(char event) {
				
			}
			
			public void clickBehaviour(int event) {
				if(event == -1) {
					System.out.println(this.getElementStoredText("ent"));
				}
			} 
			
		};
		pan.addTextEntry("ent", 5, 15, 15, 500, 250, 1, new Font("Arial Bold", Font.BOLD, 18));
		pan.addRectangle("rec", 0, 15, 15, 500, 250, new Color(255,255,255), new Color(0,0,0));
		ElementPanel pan2 = new ElementPanel(600, 0, 400, 400) {
			
		};
		pan2.addImage("sas", 100, 200, 200, imagePath);
		fram.addPanel("fir", pan);
		fram.addPanel("sas", pan2);
		}
}