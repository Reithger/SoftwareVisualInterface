package visual.composite;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;

public interface HandleElements {
	
//---  Operations   ---------------------------------------------------------------------------
	
	public abstract void handleLine(String nom, String group, int prior, int x, int y, int x2, int y2, int thck, Color fill);

	public abstract void handleRectangle(String nom, String group, int prior, int x, int y, int wid, int hei, Color fill, Color border);
	
	public abstract void handleThickRectangle(String nom, String group, int prior, int x, int y, int x2, int y2, Color border, int thick);

	public abstract void handleButton(String nom, String group, int prior, int x, int y, int wid, int hei, int code);

	public abstract void handleImage(String nom, String group, int prior, int x, int y, String path, double scale);

	public abstract void handleImage(String nom, String group, int prior, int x, int y, Image img, double scale);

	public abstract void handleImage(String nom, String group, int prior, int x, int y, int wid, int hei, boolean prop, String path);

	public abstract void handleImage(String nom, String group, int prior, int x, int y, int wid, int hei, boolean prop, Image path);
	
	public abstract void handleText(String nom, String group, int prior, int x, int y, int wid, int hei, Font font, String phr);
	
	public abstract void handleText(String nom, String group, int prior, int x, int y, int wid, int hei, ArrayList<String> phrases, ArrayList<Font> fonts, ArrayList<Color> colors);

	public abstract void handleTextEntry(String nom, String group, int prior, int x, int y, int wid, int hei, int cod, Font font, String phr);
	
	public abstract void handleTextButton(String nom, String group, int prior, int x, int y, int wid, int hei, Font font, String phr, int code, Color fill, Color border);

	public abstract void handleImageButton(String name, String group, int prior, int x, int y, int wid, int hei, String path, int code);
	
	public abstract void handleScrollbar(String name, String group, String controlledGroup, int prior, int scrollX, int scrollY, int scrollWid, int scrollHei, int windowAxisOrigin, int windowSize, boolean isBarVert);

	public abstract void handleCanvas(String name, String group, int prior, int x, int y, int wid, int hei, int canWid, int canHei, int code);
	
}