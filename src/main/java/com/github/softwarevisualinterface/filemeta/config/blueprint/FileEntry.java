package com.github.softwarevisualinterface.filemeta.config.blueprint;

public class FileEntry {

//---  Instance Variables   -------------------------------------------------------------------
	
	private String name;
	private String comments;
	private String value;
	
//---  Constructors   -------------------------------------------------------------------------
	
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
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public String getName() {
		return name;
	}
	
	public String getComments() {
		return comments;
	}
	
	public String getValue() {
		return value;
	}
	
//---  Mechanics   ----------------------------------------------------------------------------
	
	@Override
	public int hashCode() {
		return (name + comments).hashCode();
	}
	
}
