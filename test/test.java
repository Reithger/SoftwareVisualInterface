package test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

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
		
		WindowFrame fram = new WindowFrame(1200, 500);

		ElementPanel pan = new ElementPanel(0, 0, 300, 500) {
			public void keyBehaviour(char event) {
				
			}
			
			public void clickBehaviour(int event, int x, int y) {
				System.out.println(x + " " + y);
			} 
			
		};
		
		Font defaultFont = new Font("Serif", Font.BOLD, 18);
		
		pan.addRectangle("rect", 1, pan.getWidth() / 20, pan.getHeight() / 10, pan.getWidth() * 18/20, pan.getHeight() * 17/20, false, Color.blue);
		
		pan.addRectangle("rect2", 8, pan.getWidth() / 18,  pan.getHeight() / 18, pan.getWidth() * 16 / 18,  pan.getHeight() * 2 / 18, false, Color.white, Color.black);
		pan.addText("tex", 10, pan.getWidth() / 18, pan.getHeight() / 18, pan.getWidth() * 16 / 18, pan.getHeight() * 2 / 18, "This is a test phrase for a text\n object", defaultFont, false, true, true);
		
		pan.addRectangle("rect3", 8, pan.getWidth() / 2, pan.getHeight() / 2, pan.getWidth() * 12 / 18, pan.getHeight() / 12, true, Color.white, Color.black);
		pan.addText("tex1", 10, pan.getWidth() / 2, pan.getHeight() / 2, pan.getWidth() * 12 / 18, pan.getHeight() / 12, "T T T", defaultFont, true, true, true);
		pan.addText("tex2", 10, pan.getWidth() / 2, pan.getHeight() /  2, pan.getWidth() * 12 / 18, pan.getHeight() / 12, "F F F", defaultFont, false, false, false);
		pan.addText("tex3", 10, pan.getWidth() / 2, pan.getHeight() / 2, pan.getWidth() * 12 / 18, pan.getHeight() / 12, "T F T", defaultFont, true, false, true);
		
		
		ElementPanel pan2 = new ElementPanel(400, 0, 300, 500) {
			public void keyBehaviour(char event) {
				
			}
			
			public void clickBehaviour(int event, int x, int y) {
				System.out.println(event + " " + x + " " + y);
				System.out.println(getFocusElement());
				moveElement("line5", 40, 900);
			} 
			
		};
		
		pan.setScrollBarVertical(false);
		
		pan2.addRectangle("rect", 1, pan2.getWidth() /20, pan2.getHeight() / 20, pan2.getWidth() * 18/20, pan2.getHeight() * 18/20, false, Color.red);
		pan2.addRectangle("rect2", 8, pan2.getWidth() / 2,  pan2.getHeight() / 6, pan2.getWidth() * 16 / 18,  pan2.getHeight() * 2 / 18, true, Color.white, Color.black);
		pan2.addTextEntry("texEn", 10, pan2.getWidth() / 2, pan2.getHeight() / 6, pan2.getWidth() * 16 / 18, pan2.getHeight() * 2 / 18, 15, "This is a text entry area", defaultFont, true, true, true);
		
		pan2.addImage("sas", 15, pan2.getWidth() / 2, pan2.getHeight() * 2 / 3, true, imagePath, .5);
		
		pan2.addLine("line5", 30, 40, 10, 50, 750, 5, Color.black);
		pan2.addLine("line6", 30, 40, 10, 50, 50, 5, Color.black);
		pan2.setScrollBarHorizontal(false);
		
		CanvasPanel can = new CanvasPanel(800, 0, 300, 500, 1) {
			private boolean grid;
			
			@Override
			public void keyEvent(char key) {
				if(key == 'g') {
					grid = !grid;
				}
			}
			
			@Override
			public void commandOver(Graphics g) {
				if(grid) {
					int wid = this.getWidth();
					int hei = this.getHeight();
					int zm = this.getZoom();
					for(int i = 0; i < wid / zm; i++) {
						int x = zm * i;
						g.drawLine(x, 0, x, hei);
					}
					for(int i = 0; i < hei / zm; i++) {
						int y = zm * i;
						g.drawLine(0, y, wid, y);
					}
				}
			}
		};
		
		can.setPenColor(Color.blue);
		
		drawFrame(pan);
		drawFrame(pan2);
		fram.reserveWindow("window");
		fram.reservePanel("window", "panel1", pan);
		fram.reservePanel("window", "panel2", pan2);
		fram.reservePanel("window", "canvas", can);
	}

	private static void drawFrame(ElementPanel p) {
		p.addLine("line1", 5, 0, 0, p.getWidth(), 0, 3, Color.black);
		p.addLine("line2", 5, 0, 0, 0, p.getHeight(), 3, Color.black);
		p.addLine("line3", 5, p.getWidth(), p.getHeight(), p.getWidth(), 0, 3, Color.black);
		p.addLine("line4", 5, p.getWidth(), p.getHeight(), 0, p.getHeight(), 3, Color.black);
	}
	
}