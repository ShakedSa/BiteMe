package Config;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class ReadPropertyFile {
	
	private static Properties prop;
	private static ReadPropertyFile single_instance = null;
	
	public static ReadPropertyFile getInstance() 
	{
		if (single_instance == null) {
            single_instance = new ReadPropertyFile();
            
			try {
				prop=new Properties();
				InputStream is = new URL("https://github.com/ShakedSa/BiteMe/blob/master/BiteMeServer/src/Config/config.properties").openStream();
//				FileInputStream fin = new FileInputStream("src\\Config\\config.properties");
				prop.load(is);
			}catch (Exception e) {}
		}

 
        return single_instance;
		
	}
	public String getProp(String s) 
	{
		return prop.getProperty(s);
		
	}
}

