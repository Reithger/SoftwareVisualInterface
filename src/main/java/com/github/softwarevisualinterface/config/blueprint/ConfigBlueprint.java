package com.github.softwarevisualinterface.config.blueprint;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigBlueprint {

	private static Logger logger = LogManager.getLogger();

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
			logger.error("Error during Configuration System write", e);
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
