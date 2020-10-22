package filemeta.config;

import java.io.File;

public interface ValidateFiles {

	/**
	 * User directed implementation of the process to validate the config files for their
	 * particular project.
	 * 
	 * Returns an int value representing the state of this function's termination; if successful, return
	 * the class constant Config.CONFIG_VERIFY_SUCCESS to denote such. Otherwise, return any other value
	 * for interpretation by the function calling the verifyConfig() function (which calls this method).
	 * 
	 * @param c
	 * @param f
	 * @return
	 */
	
	public abstract int validateFile(Config c, File f);
	
}
