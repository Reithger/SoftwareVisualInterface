package com.github.softwarevisualinterface.filemeta;

import java.io.File;
import javax.swing.JFileChooser;

public class FileChooser {
	
//---  Operations   ---------------------------------------------------------------------------
	
	public static File promptSelectFile(String defaultPath, boolean explore, boolean files) {
		JFileChooser jf = new JFileChooser(defaultPath);
		jf.setFileSelectionMode(explore ? files ? JFileChooser.FILES_AND_DIRECTORIES : JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_ONLY);
		if(jf.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
			return jf.getSelectedFile();
		return null;
	}
	
	/**
	 * This function saves the provided file at the location specified by the User; it will move the file toSave to the
	 * new destination, removing it from its original location. Beware this side effect.
	 * 
	 * 
	 * @param defaultPath
	 * @param explore
	 * @param files
	 * @param toSave
	 */
	
	public static void promptSaveFile(String defaultPath, boolean explore, boolean files, File toSave) {
		JFileChooser jf = new JFileChooser(defaultPath);
		jf.setFileSelectionMode(explore ? files ? JFileChooser.FILES_AND_DIRECTORIES : JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_ONLY);
		if(jf.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			File saveLocale = jf.getSelectedFile();
			saveLocale = new File(saveLocale.getAbsolutePath() + File.separator + toSave.getName());
			toSave.renameTo(saveLocale);
		}
	}
}
