import java.io.File;

import com.github.softwarevisualinterface.config.Config;
import com.github.softwarevisualinterface.config.ValidateFiles;

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
