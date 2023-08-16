package com.github.softwarevisualinterface.config.blueprint;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;

import com.github.softwarevisualinterface.config.ConfigFileParser;

public class ConfigFile {
	
//---  Constants   ----------------------------------------------------------------------------

	public final static String COMMENT_SYMBOL = "#####";

//---  Instance Variables   -------------------------------------------------------------------
	
	private String name;
	private String fileComment;
	private HashSet<FileEntry> fileEntries;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public ConfigFile(String nom, String comment) {
		name = nom;
		fileComment = comment;
		fileEntries = new HashSet<FileEntry>();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void write(String path, boolean erase) throws IOException{
		File f = new File(path + name);
		if(f.exists() && !erase) {
			return;
		}
		f.delete();
		f.createNewFile();
		RandomAccessFile raf = new RandomAccessFile(f, "rw");
		if(fileComment.length() > 0)
			raf.writeBytes(COMMENT_SYMBOL + fileComment + "\n");
		for(FileEntry s : getFileEntries()) {
			raf.writeBytes(COMMENT_SYMBOL + s.getComments() + "\n");
			raf.writeBytes(s.getName() + ConfigFileParser.ENTRY_EQUAL_SYMBOL + s.getValue() + ConfigFileParser.ENTRY_VALUE_END_SYMBOL + "\n");
		}
		raf.close();
	}
	
	public void erase(String path) {
		File f = new File(path + name);
		f.delete();
	}
	
//---  Adder Methods   ------------------------------------------------------------------------
	
	public void addFileEntry(String nom, String comment, String value) {
		fileEntries.add(new FileEntry(nom, comment, value));
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	public String getName() {
		return name;
	}
	
	public HashSet<FileEntry> getFileEntries(){
		return fileEntries;
	}
	
}
