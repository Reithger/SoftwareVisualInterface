package com.github.softwarevisualinterface.config.blueprint;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ConfigBlueprint {

//---  Instance Variables   -------------------------------------------------------------------
	
	private Folder root;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public ConfigBlueprint(String src) {
		if(src.equals("")) {
			src = (new File("")).getAbsolutePath();
		}
		root = new Folder(src);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public boolean writeBlueprint(boolean erase) {
		try {
			root.write("", erase);
			return true;
		}
		catch(IOException e) {
			e.printStackTrace();
			System.out.println("Error during Configuration System write");
			return false;
		}
	}

	public void eraseBlueprint() {
		root.erase("");
	}
	
	private String[] processPath(String path) {
		path = path.replaceAll("\\\\", "/").replaceAll("//", "/");
		String[] use = path.split("/");
		return use;
	}
	
//---  Adder Methods   ------------------------------------------------------------------------
	
	public String addFilePath(String path) {
		return root.addFilePath(processPath(path));
	}
	
	public void addFile(String path, String fileName, String fileComments) {
		root.addFile(processPath(path), fileName, fileComments);
	}
	
	public void addFileEntry(String path, String fileName, String entryName, String entryComment, String entryValue) {
		root.addFileEntry(processPath(path), fileName, entryName, entryComment, entryValue);
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	public ArrayList<File> getAllFiles(){
		return root.getAllFiles("");
	}

	public Folder getRoot() {
		return root;
	}
	
}
