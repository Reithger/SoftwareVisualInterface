package test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import visual.frame.WindowFrame;
import visual.panel.ElementPanel;
import visual.panel.element.DrawnCanvas;

public class Tutorial {
	
	/*
	 * Introductory file that explains how the library can be used to generate your program's UI.
	 * 
	 * Please reference the TutorialExample.jar runnable file to see what program this code generates.
	 * 
	 * Comments in some classes may be lacking, so this should serve as a starting location for basic
	 * usage of the library; if more advanced usage is desired, feel free to explore the Classes to
	 * see how they operate, or contact the library writer, Ada Clevinger (aka Reithger), at her github
	 * for help, suggestions, or a demand to make a more convenient wiki.
	 * 
	 */

	public static void main(String[] args) {
		drawTest1();
	}

	private static void drawTest1() {
		//Image paths should be contextual to your project architecture, no worries about accessing within a .jar if you package your program, I fixed that
		String imagePath = "src\\test\\assets\\Saskia_Portrait.jpg";
		String imagePath2 = "src\\test\\assets\\ada.png";
		String[] imagesPaths = new String[] {"src\\test\\assets\\burner5.png","src\\test\\assets\\burner6.png","src\\test\\assets\\burner7.png"};
		
		//Just make a WindowFrame with (width, height)
		WindowFrame fram = new WindowFrame(1200, 500);

		//A WindowFrame specifically functions to allow sets of Panels to be added, allowing for group addition/removal of Panels
		fram.reserveWindow("window");
		
		/*
		 * Creates a .jar with an (x, y) position in its parent Frame and a width, height
		 * 
		 * ALSO, this overrides the functions keyBehaviour and clickBehaviour as a part of instantiation, which
		 * is a neat trick you can just do that is used here to specify how the ElementPanel should respond to
		 * the input it receives.
		 * 
		 * Unfortunately this ElementPanel doesn't do anything at the moment beside some brief print-outs.
		 * 
		 */
		ElementPanel pan = new ElementPanel(0, 0, 300, 500) {
			public void keyBehaviour(char event) {
				
			}
			
			public void clickBehaviour(int event, int x, int y) {
				System.out.println(x + " " + y);
				
			} 
			
		};
		
		Font defaultFont = new Font("Serif", Font.BOLD, 18);
		
		//Here is a whole suite of functions that add Elements of varying types to the ElementPanel
		
		pan.addRectangle("rect", 1, false, pan.getWidth() / 20, pan.getHeight() / 10, pan.getWidth() * 18/20, pan.getHeight() * 17/20, false, Color.blue);
		
		pan.addRectangle("rect2", 8, false,  pan.getWidth() / 18,  pan.getHeight() / 18, pan.getWidth() * 16 / 18,  pan.getHeight() * 2 / 18, false, Color.white, Color.black);
		pan.addText("tex", 10, false,  pan.getWidth() / 18, pan.getHeight() / 18, pan.getWidth() * 16 / 18, pan.getHeight() * 2 / 18, "This is a test phrase for a text\n object", defaultFont, false, true, true);
		
		pan.addRectangle("rect3", 8, false,  pan.getWidth() / 2, pan.getHeight() / 2, pan.getWidth() * 12 / 18, pan.getHeight() / 12, true, Color.white, Color.black);
		pan.addText("tex1", 10, false,  pan.getWidth() / 2, pan.getHeight() / 2, pan.getWidth() * 12 / 18, pan.getHeight() / 12, "T T T", defaultFont, true, true, true);
		pan.addText("tex2", 10, false,  pan.getWidth() / 2, pan.getHeight() /  2, pan.getWidth() * 12 / 18, pan.getHeight() / 12, "F F F", defaultFont, false, false, false);
		pan.addText("tex3", 10, false,  pan.getWidth() / 2, pan.getHeight() / 2, pan.getWidth() * 12 / 18, pan.getHeight() / 12, "T F T", defaultFont, true, false, true);
		
		pan.addAnimation("anim", 23, false,  pan.getWidth() / 2, pan.getHeight() * 3 / 4, true,	new int[] {13, 7, 12}, 5, imagesPaths);
		
		pan.addImage("ada", 15, false, pan.getWidth() / 4, pan.getHeight() / 3, 125, 75, true, imagePath2, true);
		pan.addImage("ada2", 15, false, pan.getWidth() * 2 / 3, pan.getHeight() / 3, 125, 75, true, imagePath2, false);
		
		
		//Another ElementPanel, this time when clicked it moves an Element
		ElementPanel pan2 = new ElementPanel(400, 0, 300, 500) {
			@Override
			public void keyBehaviour(char event) {
			}
			
			@Override
			public void clickBehaviour(int event, int x, int y) {
				System.out.println(event + " " + x + " " + y);
				System.out.println(getFocusElement());
				moveElement("line5", 40, 900);
			} 
			
		};
		
		//Scrollbars are a functionality that allow a Panel to be navigated if its Elements extend beyond the size of the Panel; they can be disabled
		pan.setScrollBarVertical(true);
		
		pan2.addRectangle("rect", 1, false,  pan2.getWidth() /20, pan2.getHeight() / 20, pan2.getWidth() * 18/20, pan2.getHeight() * 18/20, false, Color.red);
		pan2.addRectangle("rect2", 8, false,  pan2.getWidth() / 2,  pan2.getHeight() / 6, pan2.getWidth() * 16 / 18,  pan2.getHeight() * 2 / 18, true, Color.white, Color.black);
		pan2.addTextEntry("texEn", 10, false,  pan2.getWidth() / 2, pan2.getHeight() / 6, pan2.getWidth() * 16 / 18, pan2.getHeight() * 2 / 18, 15, "This is a text entry area", defaultFont, true, true, true);
		
		pan2.addImage("sas", 15, false,  pan2.getWidth() / 2, pan2.getHeight() * 2 / 3, true, imagePath, .5);
		
		pan2.addLine("line5", 30, false,  40, -70, 50, 750, 5, Color.black);
		pan2.addLine("line6", 30, false,  50, 50, 150, 50, 5, Color.black);
		pan2.setScrollBarVertical(true);
		
		DrawnCanvas can = new DrawnCanvas(800, 0, 300, 500, 5, 300, 500, 1) {
			private boolean grid;
			
			
			/*//CanvasPanel has some specific functions to it for overriding, this one draws a grid over the canvas if enabled
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
			}*/
		};
		
		//CanvasPanel can have its pen change color, it's a virtual canvas

		//Calls on a helper method to draw some lines around these Panels to add borders to them
		drawFrame(pan);
		drawFrame(pan2);
		//Instructs the WindowFrame to show the window "window"
		fram.showActiveWindow("window");
		//And now we add each Panel object to the Window with unique names and a housing Window. Done!
		fram.addPanelToWindow("window", "panel1", pan);
		//We can also use a simpler function call of addPanel(panelName, panel) if we won't need to control Windows and can just have one pool for all Panels
		//This uses a window named "default" that the WindowFrame prepares during its construction
		fram.addPanel("panel2", pan2);
	}

	private static void drawFrame(ElementPanel p) {
		p.addLine("line1", 5, false,  0, 0, p.getWidth(), 0, 3, Color.black);
		p.addLine("line2", 5, false,  0, 0, 0, p.getHeight(), 3, Color.black);
		p.addLine("line3", 5, false,  p.getWidth(), p.getHeight(), p.getWidth(), 0, 3, Color.black);
		p.addLine("line4", 5, false,  p.getWidth(), p.getHeight(), 0, p.getHeight(), 3, Color.black);
	}
	
}