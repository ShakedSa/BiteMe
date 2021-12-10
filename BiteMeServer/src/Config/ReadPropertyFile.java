package Config;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * A singleton for reading default properties in the project.
 * 
 * @author Eden Ben Abu
 * @version November 2021 (1.0)
 * */
public class ReadPropertyFile {

	/** Property reader. */
	private static Properties prop;

	/** Instance of the singleton. */
	private static ReadPropertyFile single_instance = null;

	/** Location of input stream. */
	private static FileInputStream fin = null;

	/**
	 * Getting singleton instance.
	 * 
	 * @return single_instance
	 */
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

	/**
	 * Reading a single item property from the input stream.
	 * 
	 * @param property
	 */
	public String getProp(String property) {
		try {
			prop.load(fin);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return prop.getProperty(property);
	}
}