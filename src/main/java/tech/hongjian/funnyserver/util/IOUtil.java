package tech.hongjian.funnyserver.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiahongjian 
 * @time   2019-03-04 16:53:34
 *
 */
public class IOUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(IOUtil.class);
	
	public static String toString(String path) {
		if (path == null)
			return null;
		StringBuilder buffer = new StringBuilder();;
		try {
			BufferedReader reader = Files.newBufferedReader(Paths.get(path));
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
				buffer.append("\n");
			}
			return buffer.toString();
		} catch (IOException e) {
			LOGGER.error("Failed to read file: {}", path, e);
		}
		return null;
	}
	
	public static String toString(File file) {
		if (file == null || !file.exists() || file.isDirectory())
			return null;
		return toString(file.getPath());
	}
}
