package Config;

import java.io.FileInputStream;
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
//				InputStream is = new URL("https://github.com/ShakedSa/BiteMe/blob/master/BiteMeServer/src/Config/config.properties").openStream();
				FileInputStream fin = new FileInputStream("BitmeMeClient/src/Config/config.properties");
				prop.load(fin);
			}catch (Exception e) {}
		}

 
        return single_instance;
		
	}
	public String getProp(String s) 
	{
		return prop.getProperty(s);
		
	}
}

