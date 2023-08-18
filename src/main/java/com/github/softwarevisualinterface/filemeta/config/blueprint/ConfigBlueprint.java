package com.github.softwarevisualinterface.filemeta.config.blueprint;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigBlueprint {

	private static Logger logger = LogManager.getLogger();

//---  Instance Variables   -------------------------------------------------------------------
	
	private Folder root;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public ConfigBlueprint(String src) {
		if(src.isEmpty()) {
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
		return FilenameUtils.normalize(path).split(File.separator);
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
	
	public List<File> getAllFiles(){
		return root.getAllFiles("");
	}

	public Folder getRoot() {
		return root;
	}
	
}
