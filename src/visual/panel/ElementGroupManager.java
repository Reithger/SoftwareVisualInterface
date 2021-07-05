package visual.panel;

import java.util.HashMap;
import java.util.HashSet;

public class ElementGroupManager {

	private HashMap<Integer, HashSet<String>> groupMapping;
	
	public ElementGroupManager() {
		groupMapping = new HashMap<Integer, HashSet<String>>();
	}
	
	public void addMapping(Integer hash, String groupName) {
		HashSet<String> ref = groupMapping.get(hash);
		if(ref != null) {
			ref.add(groupName);
			groupMapping.put(hash, ref);
		}
		else {
			HashSet<String> use = new HashSet<String>();
			use.add(groupName);
			groupMapping.put(hash, use);
		}
	}
	
	public void removeMapping(Integer hash, String groupName) {
		HashSet<String> ref = groupMapping.get(hash);
		if(ref != null) {
			ref.remove(groupName);
			groupMapping.put(hash, ref);
		}
	}
	
	public void removeMapping(Integer hash) {
		groupMapping.remove(hash);
	}
	
	public void removeGroup(String groupName) {
		for(Integer i : groupMapping.keySet()) {
			HashSet<String> get = groupMapping.get(i);
			get.remove(groupName);
			groupMapping.put(i, get);
		}
	}
	
	public HashSet<String> getGroups(Integer hash){
		return groupMapping.get(hash);
	}
	
}
