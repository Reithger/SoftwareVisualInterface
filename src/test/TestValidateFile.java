package test;

import java.io.File;

import visual.filemeta.config.Config;
import visual.filemeta.config.ValidateFiles;

public class TestValidateFile implements ValidateFiles{

	@Override
	public int validateFile(Config c, File f) {
		switch(f.getName()) {
			case "test.txt":
				String tes = c.getConfigFileEntry(f.getAbsolutePath(), "test");
				if(!"expected".equals(tes)) {
					return -1;
				}
				break;
			default:
				break;
		}
		return Config.CONFIG_VERIFY_SUCCESS;
	}

}
