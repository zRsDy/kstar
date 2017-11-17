package org.xsnake.web.utils;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class PropertiesUtil {
	
	private static ConcurrentMap<String, String> configMap = new ConcurrentHashMap<String, String>();
	private static final String CONFIG_FILE = "/service_client.properties";
	private static Properties prop = null;

	public static String getStringByKey(String key) {
		if(null == prop){
			loadProp();
		}
		
		key = key.trim();
		if (!configMap.containsKey(key)) {
			if (prop.getProperty(key) != null) {
				configMap.put(key, prop.getProperty(key));
			}
		}
		return configMap.get(key);
	}

	private static void loadProp() {
		ResourceLoader loader = new DefaultResourceLoader();
		Resource resource = loader.getResource(CONFIG_FILE);
		try {
			prop = new Properties();
			prop.load(resource.getInputStream());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
