package test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;


import filemeta.FileChooser;
import filemeta.config.Config;
import misc.Canvas;
import visual.composite.popout.PopoutSelectList;
import visual.frame.WindowFrame;
import visual.panel.ElementPanel;
import visual.panel.element.DrawnCanvas;

public class test {
	
	/*
	 * 
	 * 
	 * 
	 *
	 */

	public static void main(String[] args) {
		/*Callback.setCallback("C", new Callback() {
			@Override
			public void callbackFunction() {
				System.out.println("Yo");
			}
		});
		Callback.callback("C");*/
		//testComposites();
		
		//testConfig();
		drawTest1();
	}
	
	private static void testComposites() {
		//PopoutAlert pa = new PopoutAlert(450, 250, "yo get scared");
		//pa.dispose();
		PopoutSelectList psl = new PopoutSelectList(250, 150, new String[] {"var1A", "var2B", "var2C", "var1D"}, true);
		psl.setTitle("Test");
		System.out.println(psl.getSelected());
		psl.dispose();
		//PopoutImageDisplay pid = new PopoutImageDisplay(200, 200, "src\\test\\assets\\ada.png");
	}
	
	private static void testConfig() {
		Config c = new Config("", new TestValidateFile());
		c.addFilePath("test");
		c.addFilePath("test/settings");
		c.addFilePath("test/settings/underling");
		c.addFile("test/settings", "test.txt", "#Howdy");
		c.addFileEntry("test/settings", "test.txt", "test", "More howdy", "expected");
		System.out.println(c.verifyConfig());
		System.out.println(c.initializeDefaultConfig());
		System.out.println(c.verifyConfig());
		c.setConfigFileEntry("test/settings/test.txt", "test", "unexpected");
		System.out.println(c.getConfigFileEntry("test/settings/test.txt", "test"));
	}
	
	private static void testFileChooser() {
		File f = FileChooser.promptSelectFile("C:/Users/Borinor/Pictures/", true, true);
		FileChooser.promptSaveFile(null, true, false, f);
	}
	
	private static Canvas can;
	
	private static void drawTest1() {
		String imagePath = "src\\test\\assets\\Saskia_Portrait.jpg";
		String imagePath2 = "src\\test\\assets\\ada.png";
		String[] imagesPaths = new String[] {"src\\test\\assets\\burner5.png","src\\test\\assets\\burner6.png","src\\test\\assets\\burner7.png"};
		
		WindowFrame fram = new WindowFrame(1200, 500);
		fram.setName("Test");
		ElementPanel pan = new ElementPanel(0, 0, 300, 500) {
			public void keyEvent(char event) {
				super.keyEvent(event);
				System.out.println(event);
				//PopoutSelectList psL = new PopoutSelectList(300, 500, new String[] {"A", "B"}, false);
				//System.out.println(psL.getSelected());
			}
			
			public void clickEvent(int event, int x, int y, int clickType) {
				super.clickEvent(event, x, y, clickType);
				System.out.println(x + " " + y);
				
			} 
			
		};
		
		Font defaultFont = new Font("Serif", Font.BOLD, 18);
		
		pan.addRectangle("rect", 1, "move", pan.getWidth() / 20, pan.getHeight() / 10, pan.getWidth() * 18/20, pan.getHeight() * 17/20, false, Color.blue);
		
		pan.addRectangle("rect2", 8, "move",  pan.getWidth() / 18,  pan.getHeight() / 18, pan.getWidth() * 16 / 18,  pan.getHeight() * 2 / 18, false, Color.white, Color.black);
		pan.addText("tex", 10, "move",  pan.getWidth() / 18, pan.getHeight() / 18, pan.getWidth() * 16 / 18, pan.getHeight() * 2 / 18, "This is a test phrase for a text\n object", defaultFont, true, true, false);
		
		pan.addRectangle("rect3", 8, "move",  pan.getWidth() / 2, pan.getHeight() / 2, pan.getWidth() * 12 / 18, pan.getHeight() / 12, true, Color.white, Color.black);
		pan.addText("tex1", 10, "move",  pan.getWidth() / 2, pan.getHeight() / 2, pan.getWidth() * 12 / 18, pan.getHeight() / 12, "T T T", defaultFont, true, true, false);
		pan.addText("tex2", 10, "move",  pan.getWidth() / 2, pan.getHeight() /  2, pan.getWidth() * 12 / 18, pan.getHeight() / 12, "F F F", defaultFont, true, false, false);
		pan.addText("tex3", 10, "move",  pan.getWidth() / 2, pan.getHeight() / 2, pan.getWidth() * 12 / 18, pan.getHeight() / 12, "T F T", defaultFont, true, false, true);
		
		pan.addAnimation("anim", 23, "move",  pan.getWidth() / 2, pan.getHeight() * 3 / 4, true, new int[] {13, 7, 12}, 5, imagesPaths);
		
		pan.addImage("ada", 15, "move", pan.getWidth() / 4, pan.getHeight() / 3, 125, 75, true, imagePath2, true);
		pan.addImage("ada2", 15, "move", pan.getWidth() * 2 / 3, pan.getHeight() / 3, 125, 75, true, imagePath2, false);

		pan.addRectangle("rectout", 25, "no_move", pan.getWidth() / 3 - 5, 40, 5, pan.getHeight() - 80, false, Color.gray, Color.black);
		pan.addRectangle("rectout2", 25, "no_move", 2 * pan.getWidth() / 3, 40, 5, pan.getHeight() - 80, false, Color.gray, Color.black);
		
		pan.addRectangle("rectout3", 25, "move", -150, 40, 5, pan.getHeight() - 80, false, Color.gray, Color.black);
		
		
		pan.setGroupDrawOutsideWindow("move", false);
		
		pan.addScrollbar("scrollbar", 30, "no_move", 0, pan.getHeight() - 20, pan.getWidth() / 2, 20, pan.getWidth() / 3, pan.getWidth() / 3, "move", false);
		
		ElementPanel pan2 = new ElementPanel(400, 0, 300, 500) {
			
			private boolean dragging;
			private int lastX;
			private int lastY;
			
			@Override
			public void keyEvent(char event) {
				super.keyEvent(event);
				//if(event == 't') {
				//	fram.hideActiveWindow("window");
				//	fram.showActiveWindow("other");
				//}
			}
			
			@Override
			public void clickPressEvent(int code, int x, int y, int clickType) {
				super.clickPressEvent(code, x, y, clickType);
				dragging = true;
				lastX = x;
				lastY = y;
			}
			
			@Override
			public void dragEvent(int code, int x, int y, int clickType) {
				super.dragEvent(code, x, y, clickType);
				if(dragging) {
					resize(getWidth() + (x - lastX), getHeight() + (y - lastY));
					lastX = x;
					lastY = y;
					drawPan2(this);
				}
			}
			
			@Override
			public void clickReleaseEvent(int code, int x, int y, int clickType) {
				super.clickReleaseEvent(code, x, y, clickType);
				dragging = false;
			}
			
			@Override
			public void clickEvent(int event, int x, int y, int clickType) {
				super.clickEvent(event, x, y, clickType);
				System.out.println(event + " " + x + " " + y);
				System.out.println(getFocusElement());
				moveElement("line5", 40, 900);
			} 
			
		};
		

		drawPan2(pan2);
		
		String CANVAS_NAME = "canvas";
		
		can = new Canvas(200, 400) {

			boolean grid;
			
			@Override
			public void input(int code) {
				if(code == 1) {
					grid = !grid;
				}
			}
			
			@Override
			public void commandOver(Graphics g) {
				if(grid) {
					int wid = this.getCanvasZoomWidth();
					int hei = this.getCanvasZoomHeight();
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
		
		ElementPanel pan3 = new ElementPanel(800, 0, 300, 500) {
			
			@Override
			public void clickEvent(int code, int x, int y, int clickType) {
				super.clickEvent(code, x, y, clickType);
				can.setPixelColor(x - getElementX(CANVAS_NAME) - getOffsetX("move"), y - getElementY(CANVAS_NAME) - getOffsetY("move"), Color.blue);
			}
			
			@Override
			public void dragEvent(int code, int x, int y, int clickType) {
				super.dragEvent(code, x, y, clickType);
				clickEvent(code, x, y, clickType);
			}
			
			@Override
			public void keyEvent(char key) {
				super.keyEvent(key);
				if(key == 'g') {
					can.input(1);
				}

				if(key == 'z') {
					can.setZoom(can.getZoom() + 1);
				}
				if(key == 'x') {
					can.setZoom(can.getZoom() - 1);
				}
				if(key == 'a') {
					moveElement("canvas", getElementX(CANVAS_NAME) + 25, getElementY(CANVAS_NAME));
				}
				if(key == 's') {
					moveElement("canvas", getElementX(CANVAS_NAME), getElementY(CANVAS_NAME) + 25);
				}
			}
			
			
		};
		
		pan3.addCanvas(CANVAS_NAME, 15, "move", 0, 0, 300, 500, can, 5);
		
		ElementPanel stlth = new ElementPanel(300, 0, 100, 100) {
			@Override
			public void clickEvent(int code, int x, int y, int clickType) {
				fram.showActiveWindow("window");
				fram.hideActiveWindow("other");
			}
		};
		
		ElementPanel hide = new ElementPanel(150, 150, 250, 250) {
			
		};
		
		hide.addAnimation("anim", 23, "move",  hide.getWidth() / 2, hide.getHeight() * 3 / 4, true,	new int[] {13, 7, 12}, 5, imagesPaths);
		
		fram.addPanel("stlth", stlth);
		fram.reserveWindow("other");
		
		fram.addPanelToWindow("other", "hide", hide);
		
		drawFrame(pan);
		drawFrame(pan2);
		fram.reserveWindow("window");
		fram.showActiveWindow("window");
		fram.addPanelToWindow("window", "panel1", pan);
		fram.addPanelToWindow("window", "panel2", pan2);
		fram.addPanelToWindow("window", "canvas", pan3);
	}
	
	private static void drawPan2(ElementPanel pan2) {
		String imagePath = "src\\test\\assets\\Saskia_Portrait.jpg";
		pan2.removeElementPrefixed("");
		pan2.addRectangle("rect", 1, "move",  pan2.getWidth() /20, pan2.getHeight() / 20, pan2.getWidth() * 18/20, pan2.getHeight() * 18/20, false, Color.red);
		pan2.addRectangle("rect2", 8, "move",  pan2.getWidth() / 2,  pan2.getHeight() / 6, pan2.getWidth() * 16 / 18,  pan2.getHeight() * 2 / 18, true, Color.white, Color.black);
		pan2.addTextEntry("texEn", 10, "move",  pan2.getWidth() / 2, pan2.getHeight() / 6, pan2.getWidth() * 16 / 18, pan2.getHeight() * 2 / 18, 15, "This is a text entry area", new Font("Serif", Font.BOLD, 12), true, true, true);
		
		pan2.addImage("sas", 15, "move",  pan2.getWidth() / 2, pan2.getHeight() * 2 / 3, true, imagePath, .5);

		pan2.addLine("line5", 30, "move",  40, -70, 50, 750, 5, Color.black);
		pan2.addLine("line6", 30, "move",  50, 50, 150, 50, 5, Color.black);
		
		pan2.addScrollbar("scrollbar", 30, "no_move", pan2.getWidth() - 30, 150, 30, pan2.getHeight() - 150, 0, pan2.getHeight(), "move", true);
		
	}

	private static void drawTest2() {
		String[] imagesPaths = new String[] {"src\\test\\assets\\burner5.png","src\\test\\assets\\burner6.png","src\\test\\assets\\burner7.png"};
		int wid = 400;	//400
		int hei = 250;	//250
		WindowFrame fra = new WindowFrame(wid, hei) {
			@Override
			public void reactToResize() {
				System.out.println("H");
				if(this.getPanel("pan") != null) {
					this.getPanel("pan").resize(this.getWidth(), this.getHeight());
				}
			}
		};

		fra.reserveWindow("window");
		ElementPanel pan = new ElementPanel(0, 0, wid, hei) {
			
			public void clickEvent(int code, int x, int y, int clickType) {
				super.clickEvent(code, x, y, clickType);
				System.out.println(y);
				//fra.resize(300, 400);
				//resize(300, 400);
			}
			
		};
		fra.addPanelToWindow("window", "pan", pan);

		pan.resize(150, 400);
		fra.resize(150, 400);	//17 pixel offset, too small
		
		fra.setResizable(true);
		
		pan.addAnimation("anim", 23, "move",  pan.getWidth() / 2, pan.getHeight() * 6 / 4, true,	new int[] {13, 7, 12}, 5, imagesPaths);
	}
	
	private static void drawFrame(ElementPanel p) {
		p.addLine("line1", 5, "move",  0, 0, p.getWidth(), 0, 3, Color.black);
		p.addLine("line2", 5, "move",  0, 0, 0, p.getHeight(), 3, Color.black);
		p.addLine("line3", 5, "move",  p.getWidth(), p.getHeight(), p.getWidth(), 0, 3, Color.black);
		p.addLine("line4", 5, "move",  p.getWidth(), p.getHeight(), 0, p.getHeight(), 3, Color.black);
	}
	
}