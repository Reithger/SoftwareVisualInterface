package filemeta.config.blueprint;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Generic definition of a Folder in a File System to map out a blueprint of a config system for reproduction
 * 
 * @author Ada Clevinger
 *
 */

public class Folder {

//---  Instance Variables   -------------------------------------------------------------------
	
	private String name;
	private ArrayList<Folder> children;
	private ArrayList<ConfigFile> files;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Folder(String nom) {
		name = nom;
		children = new ArrayList<Folder>();
		files = new ArrayList<ConfigFile>();
	}
	
//---  Operations   ---------------------------------------------------------------------------

	public void write(String path, boolean erase) throws IOException{
		path += name + "/";
		File gen = new File(path);
		gen.mkdir();
		for(ConfigFile s : files) {
			s.write(path, erase);
		}
		for(Folder f : children) {
			f.write(path, erase);
		}
	}

	public void erase(String path) {
		path += name + "/";
		for(Folder f : children) {
			f.erase(path);
		}
		for(ConfigFile s : files) {
			s.erase(path);
		}
	}
	
//---  Adder Methods   ------------------------------------------------------------------------
	
	public String addFilePath(String[] path) {
		if(path == null || path.length == 0) {
			return name;
		}
		Folder next = getNext(path[0]);
		if(next == null) {
			next = new Folder(path[0]);
			children.add(next);
		}
		return next.addFilePath(tearArray(path));
	}
	
	public void addFile(String[] path, String fileName, String comments) {
		if(path == null || path.length == 0) {
			files.add(new ConfigFile(fileName, comments));
			return;
		}
		Folder next = getNext(path[0]);
		if(next == null) {
			addFilePath(path);
			next = getNext(path[0]);
		}
		next.addFile(tearArray(path), fileName, comments);
	}
	
	public void addFileEntry(String[] path, String fileName, String entryName, String entryComment, String entryValue) {
		if(path == null || path.length == 0) {
			ConfigFile cf = getConfigFile(fileName);
			if(cf != null)
				cf.addFileEntry(entryName, entryComment, entryValue);
			return;
		}
		Folder next = getNext(path[0]);
		if(next == null) {
			addFile(path, fileName, "");
			next = getNext(path[0]);
		}
		next.addFileEntry(tearArray(path), fileName, entryName, entryComment, entryValue);
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	private Folder getNext(String nom) {
		for(Folder f : children) {
			if(f.getName().equals(nom)) {
				return f;
			}
		}
		return null;
	}
	
	private ConfigFile getConfigFile(String nom){
		for(ConfigFile cf : files) {
			if(cf.getName().equals(nom)) {
				return cf;
			}
		}
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<File> getFiles(String path){
		ArrayList<File> out = new ArrayList<File>();
		for(ConfigFile cf : files) {
			File f = new File(path + "/" + name + "/" + cf.getName());
			out.add(f);
		}
		return out;
	}
	
	public ArrayList<File> getAllFiles(String path){
		ArrayList<File> out = getFiles(path);
		for(Folder f : children) {
			out.addAll(f.getAllFiles(path + "/" + name));
		}
		return out;
	}

//---  Mechanics   ----------------------------------------------------------------------------
	
	private String[] tearArray(String[] in) {
		if(in.length == 0) {
			return new String[] {};
		}
		return Arrays.copyOfRange(in, 1, in.length);
	}
	
}
