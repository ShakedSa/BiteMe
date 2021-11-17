package Config;

import java.io.FileInputStream;
import java.util.Properties;

public class ReadPropertyFile {

	private static Properties prop;
	private static ReadPropertyFile single_instance = null;
	private static FileInputStream fin = null;

	public static ReadPropertyFile getInstance() {
		if (single_instance == null) {
			single_instance = new ReadPropertyFile();
			try {
				prop = new Properties();
				fin = new FileInputStream("src/Config/config.properties");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return single_instance;

	}

	public String getProp(String s) {
		try {
		prop.load(fin);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return prop.getProperty(s);

	}
}
