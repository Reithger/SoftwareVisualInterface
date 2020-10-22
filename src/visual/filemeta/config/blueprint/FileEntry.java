package visual.filemeta.config.blueprint;

public class FileEntry {

	private String name;
	private String comments;
	private String value;
	
	public FileEntry(String nom, String nomment) {
		name = nom;
		comments = nomment;
		value = "";
	}
	
	public FileEntry(String nom, String nomment, String entryData) {
		name = nom;
		comments = nomment;
		value = entryData;
	}
	
	public String getName() {
		return name;
	}
	
	public String getComments() {
		return comments;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public int hashCode() {
		return (name + comments).hashCode();
	}
	
}
