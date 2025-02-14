package visual.composite.popout;

import java.awt.Color;

public class PopoutSelectList extends PopoutWindow{

	private static final int CODE_FILTER_ENTRY_ACCESS = -17;
	private static final int CODE_FILTER_ENTRY_SUBMIT = -16;
	private static final int CODE_CLOSE = -18;
	
	public static final String STATIC_ACCESS = "list";
	private static final String TEXT_ENTRY_FILTER_ACCESS = "filter";
	
	private String[] ref;
	private String[] used;
	private String searchTerm;
	private boolean filtered;
	private String output;
	private volatile boolean ready;
	
	public PopoutSelectList(int wid, int hei, String[] list, boolean filter) {
		super(wid, hei);
		ref = list;
		used = ref;
		filtered = filter;
		ready = false;
		searchTerm = "";
		drawPage();
	}
	
	public String getSelected() {
		while(!ready) {
		};
		return output;
	}
	
	private void drawPage() {
		int heiChng = getHeight() / 4;
		int size = heiChng / 4;
		if(filtered) {
			this.handleTextEntry(TEXT_ENTRY_FILTER_ACCESS, "no_move", 25, getWidth() / 3, heiChng / 4, getWidth() * 7 / 12, heiChng / 4, CODE_FILTER_ENTRY_ACCESS, null, "");
			this.handleRectangle("rect_filter_entry", "no_move", 5, getWidth() / 3, heiChng / 4, getWidth() * 7 / 12, heiChng / 4, Color.WHITE, Color.BLACK);
			
			this.handleRectangle("rect_submit", "no_move", 5, getWidth() * 3 /4, heiChng / 4, heiChng / 5, heiChng / 5, Color.GREEN, Color.BLACK);
			this.handleButton("butt_submit", "no_move", 5, getWidth() * 3 / 4, heiChng / 4, size, size, CODE_FILTER_ENTRY_SUBMIT);
		}
		removeElementPrefixed("han");
		for(int i = 0; i < used.length; i++) {
			handleTextButton("han_" + i, "move", 25, getWidth() / 2, (filtered ? heiChng : heiChng / 2) + i * heiChng, getWidth() * 2 / 4, getHeight() / 5, null, used[i], i, Color.gray, Color.black);
		}
		handleText("placeholder", "move", 25, getWidth() / 2, (filtered ? heiChng : heiChng / 2) + (used.length)* heiChng , 10, 10, null, "");
		
		int xPos = getWidth() * 35/40;
		int yPos = getHeight() * 1 / 15;
		
		this.handleTextButton("close", "no_move", 25, xPos,  yPos, size,  size, null, "", CODE_CLOSE, Color.red, Color.black);
		this.handleLine("close_line_1", "no_move", 15, xPos - size / 2, yPos - size / 2, xPos + size / 2, yPos + size / 2, 2, Color.black);
		this.handleLine("close_line_2", "no_move", 15, xPos - size / 2, yPos + size / 2, xPos + size / 2, yPos - size / 2, 2, Color.black);
	}
	
	private void filterList() {
		int newSize = 0;
		for(String s : ref) {
			if(s.matches(searchTerm + ".*")) {
				newSize++;
			}
		}
		used = new String[newSize];
		int posit = 0;
		for(int i = 0; i < ref.length; i++) {
			if(ref[i].matches(searchTerm + ".*")) {
				used[posit++] = ref[i];
			}
		}
	}
	
	@Override
	public void clickAction(int code, int x, int y) {
		if(code == CODE_FILTER_ENTRY_SUBMIT) {
			searchTerm = this.getStoredText(TEXT_ENTRY_FILTER_ACCESS);
			filterList();
			drawPage();
		}
		if(code == CODE_CLOSE) {
			dispose();
		}
		for(int i = 0; i < used.length; i++) {
			if(i == code) {
				output = used[i];
				ready = true;
			}
		}
		
	}
	
}
