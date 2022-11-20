package filemeta.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import filemeta.config.blueprint.ConfigBlueprint;

//TODO: Closing function that stops all this data from being active

public class Config {

//---  Constants   ----------------------------------------------------------------------------
	
	public final static int CONFIG_VERIFY_SUCCESS = 958273;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private ConfigBlueprint bp;
	private ValidateFiles vf;
	private int errorCode;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Config(String rootPath, ValidateFiles validationRules) {
		bp = new ConfigBlueprint(rootPath);
		vf = validationRules;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * Calls on provided realization of the ValidateFiles interface to verify the config files managed
	 * by this Config object.
	 * 
	 * Returns a boolean representing the condition of the verification; expectation is that
	 * success in validation returns the class constant CONFIG_VERIFY_SUCCESS, otherwise custom code
	 * values can be used by the programmer for identifying faults, accessed by calling getErrorCode()
	 * after the verifyConfig() function has terminated.
	 * 
	 * @return - Returns a boolean representing whether the validation succeeded; if it did not, the generated error code can be accessed via getErrorCode().
	 */
	
	public boolean verifyConfig() {
		for(File f : bp.getAllFiles()) {
			int code = vf.validateFile(this, f);
			if(code != CONFIG_VERIFY_SUCCESS) {
				setErrorCode(code);
				return false;
			}
		}
		return true;
	}
	
	public boolean softWriteConfig() {
		return bp.writeBlueprint(false);
	}
	
	public boolean initializeDefaultConfig() {
		return bp.writeBlueprint(true);
	}
	
	public void eraseConfig() {
		bp.eraseBlueprint();
	}
	
//---  Adder Methods   ------------------------------------------------------------------------
	
	public String addFilePath(String path) {
		return bp.addFilePath(path);
	}
	
	public void addFile(String path, String file, String comment) {
		bp.addFile(path, file, comment);
	}
	
	public void addFileEntry(String path, String file, String entry, String comment, String value) {
		bp.addFileEntry(path, file, entry, comment, value);
	}
	
//---  Setter Methods   -----------------------------------------------------------------------

	public static boolean setConfigFileEntry(String fullPath, String entryName, String newValue) {
		try {
			return ConfigFileParser.setContents(new File(fullPath), entryName, newValue);
		}
		catch(IOException e) {
			e.printStackTrace();
			System.err.println("Failure to assign data: \"" + newValue + "\" to entry: \"" + entryName + "\" in file: \"" + fullPath + "\".");
			return false;
		}
	}

	public static boolean setConfigFileEntry(File fil, String entryName, String newValue) {
		try {
			return ConfigFileParser.setContents(fil, entryName, newValue);
		}
		catch(IOException e) {
			e.printStackTrace();
			System.err.println("Failure to assign data: \"" + newValue + "\" to entry: \"" + entryName + "\" in file: \"" + fil.getAbsolutePath() + "\".");
			return false;
		}
	}
	
	private void setErrorCode(int in) {
		errorCode = in;
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	public static String getConfigFileEntry(File fil, String entryName) {
		try {
			return ConfigFileParser.getContents(fil, entryName);
		}
		catch(IOException e) {
			e.printStackTrace();
			System.err.println("Failure to retrieve data in file: \"" + fil.getAbsolutePath() + "\" with entry name: " + entryName);
			return null;
		}
	}
	
	public static String getConfigFileEntry(String fullPath, String entryName) {
		try {
			return ConfigFileParser.getContents(new File(fullPath), entryName);
		}
		catch(IOException e) {
			e.printStackTrace();
			System.err.println("Failure to retrieve data in file: \"" + fullPath + "\" with entry name: " + entryName);
			return null;
		}
	}
	
	public int getErrorCode() {
		return errorCode;
	}

//---  Support   ------------------------------------------------------------------------------
	
	public static BufferedReader retrieveFileReader(String pathIn) {
		String path = pathIn.replace("\\", "/");
		InputStream is = Config.class.getResourceAsStream(path.substring(path.indexOf("/"))); 
		if(is == null) {
			try {
				is = new FileInputStream(new File(path));
			}
			catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return new BufferedReader(new InputStreamReader(is));
	}
	
}
