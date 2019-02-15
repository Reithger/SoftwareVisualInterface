import java.awt.Color;
import java.awt.Font;

import visual.frame.WindowFrame;
import visual.panel.ElementPanel;

public class test {

	public static void main(String[] args) {
		String imagePath = "src\\Saskia_Portrait.jpg";
		WindowFrame fram = new WindowFrame(1200, 500);
		ElementPanel pan = new ElementPanel(0, 0, 550, 290) {
			public void keyBehaviour(char event) {
				removeElement("rec");
				union.set(event+"");
			}
			
			public void clickBehaviour(int event) {
				if(event == -1) {
					System.out.println(this.getElementStoredText("ent"));
				}
				System.out.println(union.get());
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
class union{
	
	private static union un;
	private static String str;
	
	protected union(String sup) {
		str = sup;
	}
	
	public static union getUnion() {
		if(un == null)
			un = new union(" ");
		return un;
	}
	
	public static void set(String in) {
		getUnion().setIn(in);
	}
	
	private void setIn(String in) {
		str = in;
	}
	
	public static String get() {
		return getUnion().getIn();
	}
	
	private String getIn() {
		return str;
	}
	
}