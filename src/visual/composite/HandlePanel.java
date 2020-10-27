package visual.composite;

import java.awt.Color;
import java.awt.Font;

import visual.panel.ElementPanel;

public class HandlePanel extends ElementPanel implements HandleElements{
	
	protected final static Font DEFAULT_FONT = new Font("Serif", Font.BOLD, 16);
	protected final static Font ENTRY_FONT = new Font("Serif", Font.BOLD, 12);
	
	public HandlePanel(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void handleTextButton(String nom, boolean frame, int x, int y, int wid, int hei, Font font, String phr, int code, Color fill, Color border) {
		handleRectangle(nom + "_rect", frame, 5, x, y, wid, hei, fill, border);
		handleText(nom + "_text", frame, x, y, wid, hei, font == null ? DEFAULT_FONT : font, phr);
		handleButton(nom + "_butt", frame, x, y, wid, hei, code);
	}

	@Override
	public void handleText(String nom, boolean frame, int x, int y, int wid, int hei, Font font, String phr) {
		if(!moveElement(nom, x, y)){
			addText(nom, 15, false, x, y, wid, hei, phr, font == null ? DEFAULT_FONT : font, true, true, true);
		}
	}

	@Override
	public void handleImage(String nom, boolean frame, int x, int y, String path, double scale) {
		if(!moveElement(nom, x, y)){
			addImage(nom, 15, frame, x, y, true, path, scale);
		}
	}

	@Override
	public void handleTextEntry(String nom, boolean frame, int x, int y, int wid, int hei, int cod, Font font, String phr) {
		if(!moveElement(nom, x, y)){
			addTextEntry(nom, 15, false, x, y, wid, hei, cod, phr, font == null ? ENTRY_FONT : font, true, true, true);	//TODO: Smaller font for entry?
		}
	}

	@Override
	public void handleButton(String nom, boolean frame, int x, int y, int wid, int hei, int code) {
		if(!moveElement(nom, x, y)) {
			addButton(nom, 10, false, x, y, wid, hei, code, true);
		}
	}

	@Override
	public void handleLine(String nom, boolean frame, int prior, int x, int y, int x2, int y2, int thck, Color fill) {
		if(!moveElement(nom, x, y)) {
			addLine(nom, prior, false, x, y, x2, y2, thck, fill);
		}
	}

	@Override
	public void handleRectangle(String nom, boolean frame, int prior, int x, int y, int wid, int hei, Color fill, Color border) {
		if(!moveElement(nom, x, y)) {
			addRectangle(nom, prior, false, x, y, wid, hei, true, fill, border);
		}
	}

	@Override
	public void handleThickRectangle(String nom, boolean frame, int x, int y, int x2, int y2, Color border, int thick) {
		x += thick / 2;
		y += thick / 2;
		x2 -= thick / 2;
		y2 -= thick / 2;
		if(!moveElement(nom + "_line_1", x, y)) {
			addLine(nom + "_line_1", 5, frame, x, y, x2, y, thick, border);
		}
		if(!moveElement(nom + "_line_2", x, y)) {
			addLine(nom + "_line_2", 5, frame, x, y, x, y2, thick, border);
		}
		if(!moveElement(nom + "_line_3", x2, y2)) {
			addLine(nom + "_line_3", 5, frame, x2, y2, x2, y, thick, border);
		}
		if(!moveElement(nom + "_line_4", x2, y2)) {
			addLine(nom + "_line_4", 5, frame, x2, y2, x, y2, thick, border);
		}
	}

	@Override
	public void handleImageButton(String name, boolean frame, int x, int y, int wid, int hei, String path, int code) {
		String imageName = name + "_image";
		if(!moveElement(imageName, x, y)) {
			double imgWid = retrieveImage(path).getWidth(null);
			double zoom = 1.0;
			if(imgWid != wid) {
				zoom = wid / imgWid;
			}
			addImage(imageName,15, frame, x, y, true, path, zoom);
		}
		String buttonName = name + "_button";
		if(!moveElement(buttonName, x, y)) {
			addButton(buttonName, 15, frame,  x, y, wid, hei, code, true);
		}
	}
	
}
