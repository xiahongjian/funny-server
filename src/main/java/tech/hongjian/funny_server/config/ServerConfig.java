package tech.hongjian.funny_server.config;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiahongjian 
 * @time   2019-03-04 15:38:54
 *
 */
public class ServerConfig {
	public static final String CONFIG_FILE = "server.properties";
	
	public static final String KEY_SERVER_PORT = "server.port";
	public static final String KEY_STATIC_PATH = "server.resource.location";
	public static final String KEY_STATIC_URL_PREFIX = "server.resource.url-prefix";
	
	public static final String CONROLLER_PACKAGE = "web.controller-package";
	public static final String WEBSOCKET_PACKAGE = "web.websocket-package";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerConfig.class);
	private static Properties prop = new Properties();
	
	
	static {
		try {
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE));
		} catch (IOException e) {
			LOGGER.error("Failed to load config.", e);
		};
	}
	
	public static int getPort() {
		return getInt(KEY_SERVER_PORT, 8080);
	}
	
	public static int getInt(String key, int defaultVal) {
		String val = prop.getProperty(key);
		return key == null ?  defaultVal : Integer.valueOf(val);
	}
	
	public static String getString(String key) {
		return prop.getProperty(key);
	}
	
	public static String getString(String key, String defaultVal) {
		return prop.getProperty(key, defaultVal);
	}
}
