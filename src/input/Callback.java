package input;

import java.util.HashMap;

public class Callback {

	private static HashMap<String, Callback> callback;
	
	public Callback() {};
	
	public static void callback(String name) {
		callback.get(name).callbackFunction();
	}
	
	public void callbackFunction() {
		
	}
	
	public static void setCallback(String name, Callback in) {
		if(callback == null) {
			callback = new HashMap<String, Callback>();
		}
		callback.put(name, in);
	}
	
}
