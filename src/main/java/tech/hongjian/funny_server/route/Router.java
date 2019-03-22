package tech.hongjian.funny_server.route;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author xiahongjian 
 * @time   2019-03-21 11:04:54
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Router {
	private String path;
	private String method;
	
	public static Router of(String path, String method) {
		return new Router(path, method);
	}
	
	public String path() {
		return path;
	}
	public void path(String path) {
		this.path = path;
	}
	public String method() {
		return method;
	}
	public void method(String method) {
		this.method = method;
	}
}
