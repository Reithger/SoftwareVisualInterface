package visual.composite.popout;

import java.awt.Color;

import input.Callback;

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
	private String callbackReference;
	private String output;
	
	public PopoutSelectList(int wid, int hei, String[] list, boolean filter, String callbackIn) {
		super(wid, hei);
		allowScrollbars(true);
		ref = list;
		used = ref;
		filtered = filter;
		searchTerm = "";
		callbackReference = callbackIn;
		drawPage();
	}
	
	public String getSelected() {
		return output;
	}
	
	private void drawPage() {
		int heiChng = getHeight() / 4;
		int size = heiChng / 4;
		if(filtered) {
			this.handleTextEntry(TEXT_ENTRY_FILTER_ACCESS, true, getWidth() / 3, heiChng / 4, getWidth() * 7 / 12, heiChng / 4, CODE_FILTER_ENTRY_ACCESS, null, "");
			this.handleRectangle("rect_filter_entry", true, 5, getWidth() / 3, heiChng / 4, getWidth() * 7 / 12, heiChng / 4, Color.WHITE, Color.BLACK);
			
			this.handleRectangle("rect_submit", true, 5, getWidth() * 3 /4, heiChng / 4, heiChng / 5, heiChng / 5, Color.GREEN, Color.BLACK);
			this.handleButton("butt_submit", true, getWidth() * 3 / 4, heiChng / 4, size, size, CODE_FILTER_ENTRY_SUBMIT);
		}
		removeElementPrefixed("han");
		for(int i = 0; i < used.length; i++) {
			handleTextButton("han_" + i, false, getWidth() / 2, (filtered ? heiChng : heiChng / 2) + i * heiChng, getWidth() * 2 / 4, getHeight() / 5, null, used[i], i, Color.gray, Color.black);
		}
		handleText("placeholder", false, getWidth() / 2, (filtered ? heiChng : heiChng / 2) + (used.length)* heiChng , 10, 10, null, "");
		
		int xPos = getWidth() * 35/40;
		int yPos = getHeight() * 1 / 15;
		
		this.handleTextButton("close", true, xPos,  yPos, size,  size, null, "", CODE_CLOSE, Color.red, Color.black);
		this.handleLine("close_line_1", true, 15, xPos - size / 2, yPos - size / 2, xPos + size / 2, yPos + size / 2, 2, Color.black);
		this.handleLine("close_line_2", true, 15, xPos - size / 2, yPos + size / 2, xPos + size / 2, yPos - size / 2, 2, Color.black);
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
				Callback.callback(callbackReference);
			}
		}
		
	}

	@Override
	public void keyAction(char code) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void scrollAction(int scroll) {
		
	}

	@Override
	public void clickPressAction(int code, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickReleaseAction(int code, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragAction(int code, int x, int y) {
		// TODO Auto-generated method stub
		
	}
	
}
