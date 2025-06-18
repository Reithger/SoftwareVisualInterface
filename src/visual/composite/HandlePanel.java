package visual.composite;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;

import visual.panel.ElementPanel;

public class HandlePanel extends ElementPanel implements HandleElements{
	
//---  Constants   ----------------------------------------------------------------------------
	
	protected final static Font DEFAULT_FONT = new Font("Serif", Font.BOLD, 16);
	protected final static Font ENTRY_FONT = new Font("Serif", Font.BOLD, 12);
	
//---  Constructors   -------------------------------------------------------------------------
	
	public HandlePanel(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void handleTextButton(String nom, String frame, int prior, int x, int y, int wid, int hei, Font font, String phr, int code, Color fill, Color border) {
		handleRectangle(nom + "_rect", frame, prior, x, y, wid, hei, fill, border);
		handleText(nom + "_text", frame, prior + 1, x, y, wid, hei, font == null ? DEFAULT_FONT : font, phr);
		handleButton(nom + "_butt", frame, prior,  x, y, wid, hei, code);
	}

	@Override
	public void handleText(String nom, String frame, int prior, int x, int y, int wid, int hei, Font font, String phr) {
		if(!moveElement(nom, x, y)){
			addText(nom, prior, frame, x, y, wid, hei, phr, font == null ? DEFAULT_FONT : font, true, true, true);
		}
	}

	@Override
	public void handleText(String nom, String frame, int prior, int x, int y, int wid, int hei, Font font, String phr, Color col) {
		if(!moveElement(nom, x, y)){
			addText(nom, prior, frame, x, y, wid, hei, phr, font == null ? DEFAULT_FONT : font, col == null ? Color.black : col, true, true, true);
		}
	}

	@Override
	public void handleText(String nom, String group, int prior, int x, int y, int wid, int hei, ArrayList<String> phrases, ArrayList<Font> fonts, ArrayList<Color> colors) {
		if(fonts == null) {
			fonts = new ArrayList<Font>();
			for(int i = 0; i < phrases.size(); i++) {
				fonts.add(DEFAULT_FONT);
			}
		}
		if(colors == null) {
			colors = new ArrayList<Color>();
			for(int i = 0; i < phrases.size(); i++) {
				colors.add(Color.black);
			}
		}
		if(!moveElement(nom, x, y)){
			addText(nom, prior, group, x, y, wid, hei, phrases, fonts, colors, true, true, true);
		}
	}
	
	@Override
	public void handleImage(String nom, String frame, int prior, int x, int y, String path, double scale) {
		if(!moveElement(nom, x, y)){
			addImage(nom, prior, frame, x, y, true, path, scale);
		}
	}
	
	@Override
	public void handleImage(String nom, String frame, int prior, int x, int y, Image img, double scale) {
		if(!moveElement(nom, x, y)){
			addImage(nom, prior, frame, x, y, true, img, scale);
		}
	}

	@Override
	public void handleImage(String nom, String frame, int prior, int x, int y, int wid, int hei, boolean prop, String imgPath) {
		if(!moveElement(nom, x, y)){
			addImage(nom, prior, frame, x, y, wid, hei, true, imgPath, prop);
		}
	}

	@Override
	public void handleImage(String nom, String frame, int prior, int x, int y, int wid, int hei, boolean prop, Image img) {
		if(!moveElement(nom, x, y)){
			addImage(nom, prior, frame, x, y, wid, hei, true, img, prop);
		}
	}

	@Override
	public void handleTextEntry(String nom, String frame, int prior, int x, int y, int wid, int hei, int cod, Font font, String phr) {
		if(!moveElement(nom, x, y)){
			addTextEntry(nom, prior, frame, x, y, wid, hei, cod, phr, font == null ? ENTRY_FONT : font, true, true, true);	//TODO: Smaller font for entry?
		}
		if(!getElementStoredText(nom).equals(phr)) {
			setElementStoredText(nom, phr);
		}
	}

	@Override
	public void handleButton(String nom, String frame, int prior, int x, int y, int wid, int hei, int code) {
		if(!moveElement(nom, x, y)) {
			addButton(nom, prior, frame, x, y, wid, hei, code, true);
		}
	}

	@Override
	public void handleLine(String nom, String frame, int prior, int x, int y, int x2, int y2, int thck, Color fill) {
		if(!moveElement(nom, x, y)) {
			addLine(nom, prior, frame, x, y, x2, y2, thck, fill);
		}
	}

	@Override
	public void handleRectangle(String nom, String frame, int prior, int x, int y, int wid, int hei, Color fill, Color border) {
		if(!moveElement(nom, x, y)) {
			addRectangle(nom, prior, frame, x, y, wid, hei, true, fill, border);
		}
	}

	@Override
	public void handleThickRectangle(String nom, String frame, int prior, int x, int y, int x2, int y2, Color border, int thick) {
		x += thick / 2;
		y += thick / 2;
		x2 -= thick / 2;
		y2 -= thick / 2;
		if(!moveElement(nom + "_line_1", x, y)) {
			addLine(nom + "_line_1", prior, frame, x, y, x2, y, thick, border);
		}
		if(!moveElement(nom + "_line_2", x, y)) {
			addLine(nom + "_line_2", prior, frame, x, y, x, y2, thick, border);
		}
		if(!moveElement(nom + "_line_3", x2, y2)) {
			addLine(nom + "_line_3", prior, frame, x2, y2, x2, y, thick, border);
		}
		if(!moveElement(nom + "_line_4", x2, y2)) {
			addLine(nom + "_line_4", prior, frame, x2, y2, x, y2, thick, border);
		}
	}

	@Override
	public void handleImageButton(String name, String frame, int prior, int x, int y, int wid, int hei, String path, int code) {
		String imageName = name + "_image";
		if(!moveElement(imageName, x, y)) {
			double imgWid = retrieveImage(path).getWidth(null);
			double zoom = 1.0;
			if(imgWid != wid) {
				zoom = wid / imgWid;
			}
			addImage(imageName, prior, frame, x, y, true, path, zoom);
		}
		String buttonName = name + "_button";
		if(!moveElement(buttonName, x, y)) {
			addButton(buttonName, prior, frame,  x, y, wid, hei, code, true);
		}
	}

	@Override
	public void handleScrollbar(String name, String group, String controlledGroup, int prior, int scrollX, int scrollY, int scrollWid, int scrollHei, int windowAxisOrigin, int windowSize, boolean isBarVert) {
		if(!moveElement(name, scrollX, scrollY)) {
			addScrollbar(name, prior, group, scrollX, scrollY, scrollWid, scrollHei, windowAxisOrigin, windowSize, controlledGroup, isBarVert);
		}
		
	}

	@Override
	public void handleCanvas(String name, String group, int prior, int x, int y, int wid, int hei, int canWid, int canHei, int code) {
		if(!moveElement(name, x, y)) {
			addCanvas(name, prior, group, x, y, wid, hei, canWid, canHei, code);
		}
		
	}

}
