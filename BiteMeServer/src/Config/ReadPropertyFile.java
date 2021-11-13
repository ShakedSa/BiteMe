package Config;

import java.util.Properties;

public class ReadPropertyFile {

	public void main(String[] args) {
		// TODO Auto-generated method stub
		Properties prop=new Properties();
		try {
			prop.load(getClass().getResourceAsStream("/config.properties"));
		}catch (Exception e) {}

	}

}
