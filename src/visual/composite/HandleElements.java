package visual.composite;

import java.awt.Color;
import java.awt.Font;

public interface HandleElements {
	
	public abstract void handleLine(String nom, boolean frame, int prior, int x, int y, int x2, int y2, int thck, Color fill);

	public abstract void handleRectangle(String nom, boolean frame, int prior, int x, int y, int wid, int hei, Color fill, Color border);
	
	public abstract void handleThickRectangle(String nom, boolean frame, int x, int y, int x2, int y2, Color border, int thick);

	public abstract void handleButton(String nom, boolean frame, int x, int y, int wid, int hei, int code);

	public abstract void handleImage(String nom, boolean frame, int x, int y, String path, double scale);

	public abstract void handleText(String nom, boolean frame, int x, int y, int wid, int hei, Font font, String phr);

	public abstract void handleTextEntry(String nom, boolean frame, int x, int y, int wid, int hei, int cod, Font font, String phr);
	
	public abstract void handleTextButton(String nom, boolean frame, int x, int y, int wid, int hei, Font font, String phr, int code, Color fill, Color border);

	public abstract void handleImageButton(String name, boolean frame, int x, int y, int wid, int hei, String path, int code);
	
}