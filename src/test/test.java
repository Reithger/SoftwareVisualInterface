package test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;

import filemeta.FileChooser;
import filemeta.config.Config;
import misc.Canvas;
import visual.composite.popout.PopoutSelectList;
import visual.frame.WindowFrame;
import visual.panel.ElementLoader;
import visual.panel.ElementPanel;
import visual.panel.element.drawn.DrawnCanvas;

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
				if(event == 'k') {
					fram.hidePanel("window", "panel2");
					fram.showPanel("window", "panel22");
				}
				if(event == 'l') {
					fram.hidePanel("window", "panel22");
					fram.showPanel("window", "panel2");
				}
			}
			
			private boolean flip;
			
			@Override
			public void clickPressEvent(int event, int x, int y, int clickType) {
				super.clickReleaseEvent(event, x, y, clickType);
				System.out.println("Down");
			}
			
			public void clickEvent(int event, int x, int y, int clickType) {
				super.clickEvent(event, x, y, clickType);
				System.out.println(x + " " + y + " " + flip);
				if(flip) {
					fram.hidePanel("window", "panel2");
					fram.showPanel("window", "panel22");
					
				}
				else {
					fram.hidePanel("window", "panel22");
					fram.showPanel("window", "panel2");
					
				}
				flip = !flip;
			} 
			
			@Override
			public void clickReleaseEvent(int event, int x, int y, int clickType) {
				super.clickReleaseEvent(event, x, y, clickType);
				System.out.println("Release");
			}
			
		};
		
		//fram.setIconImage(pan.retrieveImage(imagePath2));
		
		Font defaultFont = new Font("Serif", Font.BOLD, 18);
		
		ElementLoader el = new ElementLoader(pan);
		
		el.addRectangle("rect555", 0, "move", 0, 0, pan.getWidth(), pan.getHeight(), false, Color.blue);
		
		el.addRectangle("rect", 1, "move", pan.getWidth() / 20, pan.getHeight() / 10, pan.getWidth() * 18/20, pan.getHeight() * 17/20, false, Color.blue);
		
		el.addRectangle("rect2", 8, "move",  pan.getWidth() / 18,  pan.getHeight() / 18, pan.getWidth() * 16 / 18,  pan.getHeight() * 2 / 18, false, Color.white, Color.black);
		el.addText("tex", 10, "move",  pan.getWidth() / 18, pan.getHeight() / 18, pan.getWidth() * 16 / 18, pan.getHeight() * 2 / 18, "This is a test phrase for a text\n object", defaultFont, true, true, false);
		
		ArrayList<String> strings = new ArrayList<String>();
		ArrayList<Font> fonts = new ArrayList<Font>();
		ArrayList<Color> colors = new ArrayList<Color>();

		el.addRectangle("rect3", 8, "move",  pan.getWidth() / 2, pan.getHeight() / 2, pan.getWidth() * 12 / 18, pan.getHeight() / 12, true, Color.white, Color.black);
		el.addText("tex1", 10, "move",  pan.getWidth() / 2, pan.getHeight() / 2, pan.getWidth() * 12 / 18, pan.getHeight() / 12, "T T T", defaultFont, true, true, true);
		el.addText("tex2", 10, "move",  pan.getWidth() / 2, pan.getHeight() /  2, pan.getWidth() * 12 / 18, pan.getHeight() / 12, "F F F", defaultFont, false, false, false);
		el.addText("tex3", 10, "move",  pan.getWidth() / 2, pan.getHeight() / 2, pan.getWidth() * 12 / 18, pan.getHeight() / 12, "T F T", defaultFont, true, false, true);
		el.addText("tex4", 10, "move",  pan.getWidth() / 2, pan.getHeight() / 2, pan.getWidth() * 12 / 18, pan.getHeight() / 12, "T T F", defaultFont, true, true, false);
		
		el.addAnimation("anim", 23, "move",  pan.getWidth() / 2, pan.getHeight() * 3 / 4, true, new int[] {13, 7, 12}, 5, imagesPaths);
		
		el.addImage("ada", 15, "move", pan.getWidth() / 4, pan.getHeight() / 3, 125, 75, true, imagePath2, true);
		el.addImage("ada2", 15, "move", pan.getWidth() * 2 / 3, pan.getHeight() / 3, 125, 75, true, imagePath2, false);

		el.addRectangle("rectout", 25, "no_move", pan.getWidth() / 3 - 5, 40, 5, pan.getHeight() - 80, false, Color.gray, Color.black);
		el.addRectangle("rectout2", 25, "no_move", 2 * pan.getWidth() / 3, 40, 5, pan.getHeight() - 80, false, Color.gray, Color.black);
		
		el.addRectangle("rectout3", 25, "move", -150, 40, 5, pan.getHeight() - 80, false, Color.gray, Color.black);
		
		
		pan.setGroupDrawOutsideWindow("move", false);
		
		el.addScrollbar("scrollbar", 30, "no_move", 0, pan.getHeight() - 20, pan.getWidth() / 2, 20, pan.getWidth() / 3, pan.getWidth() / 3, "move", false);
		
		ElementPanel pan2 = new ElementPanel(400, 0, 300, 500) {
			
			private boolean dragging;
			private int lastX;
			private int lastY;
			
			@Override
			public void keyEvent(char event) {
				super.keyEvent(event);
				if(event == 't') {
					try {
						this.setElementStoredText("texEn", "display");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
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
				moveElement("line5", 40, 900);
			} 
			
		};
		
		ElementPanel pan22 = new ElementPanel(400, 0, 300, 500) {
			
			private boolean dragging;
			private int lastX;
			private int lastY;
			
			@Override
			public void keyEvent(char event) {
				super.keyEvent(event);
				if(event == 't') {
					try {
						this.setElementStoredText("texEn", "display");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
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
				moveElement("line5", 40, 900);
			} 
			
		};
		

		drawPan2(pan2);
		drawPan2(pan22);
		
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
		
		strings.add("wod 1");
		strings.add("wod of length 2");
		strings.add("and much much more");
		
		fonts.add(new Font("Sans Serif", Font.BOLD, 12));
		fonts.add(new Font("Times New Roman", Font.ITALIC, 18));
		fonts.add(new Font("Sans Serif", Font.PLAIN, 24));
		
		colors.add(Color.black);
		colors.add(Color.red);
		colors.add(Color.blue);

		el = new ElementLoader(pan2);
		
		el.addText("test 5", 25, "move", pan.getWidth() / 2, pan.getHeight() * 3 / 4, pan.getWidth() * 12 / 18, pan.getHeight() / 4, strings, fonts, colors, false, false, true);
		
		ElementPanel pan3 = new ElementPanel(800, 0, 300, 500) {
			
			@Override
			public void clickEvent(int code, int x, int y, int clickType) {
				super.clickEvent(code, x, y, clickType);
				int[] posit = this.getRelativeClickPosition(CANVAS_NAME, x, y);
				can.setPixelColor(posit[0], posit[1], Color.blue);
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
					shiftElement("canvas", 25, 0);
				}
				if(key == 's') {
					shiftElement("canvas", 0, 25);
				}
			}
			
			
		};
		el = new ElementLoader(pan3);
		
		el.addCanvas(CANVAS_NAME, 15, "move", 0, 0, 300, 500, can, 5);
		
		ElementPanel stlth = new ElementPanel(300, 0, 100, 100) {
			@Override
			public void clickEvent(int code, int x, int y, int clickType) {
				fram.showActiveWindow("window");
				fram.hideActiveWindow("other");
			}
		};
		
		ElementPanel hide = new ElementPanel(150, 150, 250, 250) {
			
		};

		el = new ElementLoader(hide);
		el.addAnimation("anim", 23, "move",  hide.getWidth() / 2, hide.getHeight() * 3 / 4, true,	new int[] {13, 7, 12}, 5, imagesPaths);
		
		fram.addPanel("stlth", stlth);
		fram.reserveWindow("other");
		
		fram.addPanelToWindow("other", "hide", hide);
		
		drawFrame(pan);
		drawFrame(pan2);
		fram.reserveWindow("window");
		fram.showActiveWindow("window");
		fram.addPanelToWindow("window", "panel1", pan);
		fram.addPanelToWindow("window", "panel2", pan2);
		fram.addPanelToWindow("window", "panel22", pan22);
		fram.hidePanel("window", "panel22");
		fram.addPanelToWindow("window", "canvas", pan3);
	}
	
	private static void drawPan2(ElementPanel pan2) {
		String imagePath = "src\\test\\assets\\Saskia_Portrait.jpg";
		ElementLoader el = new ElementLoader(pan2);
		pan2.removeElementPrefixed("");
		el.addRectangle("rect", 1, "move",  pan2.getWidth() /20, pan2.getHeight() / 20, pan2.getWidth() * 18/20, pan2.getHeight() * 18/20, false, Color.red);
		el.addRectangle("rect2", 8, "move",  pan2.getWidth() / 2,  pan2.getHeight() / 6, pan2.getWidth() * 16 / 18,  pan2.getHeight() * 2 / 18, true, Color.white, Color.black);
		el.addTextEntry("texEn", 10, "move",  pan2.getWidth() / 2, pan2.getHeight() / 6, pan2.getWidth() * 16 / 18, pan2.getHeight() * 2 / 18, 15, "This is a text entry area", new Font("Serif", Font.BOLD, 12), true, true, true);
		
		el.addImage("sas", 15, "move",  pan2.getWidth() / 2, pan2.getHeight() * 2 / 3, true, imagePath, .5);

		el.addLine("line5", 30, "move",  40, -70, 50, 750, 5, Color.black);
		el.addLine("line6", 30, "move",  50, 50, 150, 50, 5, Color.black);
		
		el.addScrollbar("scrollbar", 30, "no_move", pan2.getWidth() - 30, 150, 30, pan2.getHeight() - 150, 0, pan2.getHeight(), "move", true);
		
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
		ElementLoader el = new ElementLoader(pan);
		
		el.addAnimation("anim", 23, "move",  pan.getWidth() / 2, pan.getHeight() * 6 / 4, true,	new int[] {13, 7, 12}, 5, imagesPaths);
	}
	
	private static void drawFrame(ElementPanel p) {
		ElementLoader el = new ElementLoader(p);
		el.addLine("line1", 5, "move",  0, 0, p.getWidth(), 0, 3, Color.black);
		el.addLine("line2", 5, "move",  0, 0, 0, p.getHeight(), 3, Color.black);
		el.addLine("line3", 5, "move",  p.getWidth(), p.getHeight(), p.getWidth(), 0, 3, Color.black);
		el.addLine("line4", 5, "move",  p.getWidth(), p.getHeight(), 0, p.getHeight(), 3, Color.black);
	}
	
}