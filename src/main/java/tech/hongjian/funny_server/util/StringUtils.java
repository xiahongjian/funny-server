package tech.hongjian.funny_server.util;

import java.util.Arrays;

/**
 * @author xiahongjian 
 * @time   2019-03-21 11:39:22
 *
 */
public class StringUtils {
	public static String join(String separator, String...strings) {
		if (strings == null || strings.length == 0)
			return "";
		return join(separator, Arrays.asList(strings));
	}
	
	public static String join(String separator, Iterable<String> strs) {
		if (strs == null)
			return "";
		boolean isFirst = true;
		StringBuilder buf = new StringBuilder();
		for (String s : strs) {
			if (isFirst) {
				isFirst = false;
			} else {
				buf.append(separator);
			}
			buf.append(s);
		}
		return buf.toString();
	}
	
	public static boolean isBlank(String str) {
		return str == null || "".equals(str);
	}
	
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}
}
