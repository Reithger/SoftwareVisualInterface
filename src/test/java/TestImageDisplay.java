import java.awt.Image;
import java.io.IOException;
import java.io.UncheckedIOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.*;

import com.github.softwarevisualinterface.input.CustomEventReceiver;
import com.github.softwarevisualinterface.visual.composite.ImageDisplay;
import com.github.softwarevisualinterface.visual.frame.WindowFrame;
import com.github.softwarevisualinterface.visual.panel.ElementPanel;

public class TestImageDisplay {

	public static void main(String[] args) throws IOException {
		WindowFrame fra = new WindowFrame(500, 500);
		ElementPanel pa = new ElementPanel(0, 0, 500, 500);
		Image img = ImageIO.read(IOUtils.resourceToURL("ada.png", TestImageDisplay.class.getClassLoader()));
		ImageDisplay iD = new ImageDisplay(img, pa);
		pa.addEventReceiver("label1", iD.generateEventReceiver());
		pa.addEventReceiver(new CustomEventReceiver() {
			
			@Override
			public void clickEvent(int code, int x, int y, int mouseType) {
				try {
					System.out.println("Here");
					iD.setImage(ImageIO.read(IOUtils.resourceToURL("Saskia_Portrait.jpg", TestImageDisplay.class.getClassLoader())));
					iD.refresh();
					if(x < y) {
						pa.removeEventReceiver("label1");
				}
				} catch (IOException ioe) {
					throw new UncheckedIOException(ioe);
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
