package tech.hongjian.funnyserver.route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MixedAttribute;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiahongjian 
 * @time   2019-03-04 16:51:21
 *
 */
@Slf4j
public class Request {
	private HttpRequest request;
	private Map<String, List<String>> params;
	
	public Request(HttpRequest req) {
		this.request = req;
		HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(req);
		List<InterfaceHttpData> postList = decoder.getBodyHttpDatas();
		params = new HashMap<>();
		for (InterfaceHttpData data : postList) {
			MixedAttribute value = (MixedAttribute) data;
			value.setCharset(CharsetUtil.UTF_8);
			String val;
			try {
				val = value.getValue();
				if (params.containsKey(data.getName())) {
					params.get(data.getName()).add(val);
				} else {
					List<String> values = new ArrayList<>();
					values.add(val);
					params.put(data.getName(), values);
				}
			} catch (IOException e) {
				log.warn("Failed to read request params.", e);
			}
		}
	}
	
	public HttpMethod method() {
		return request.method();
	}
	
	public HttpHeaders headerds() {
		return request.headers();
	}
	
	
	
	public Map<String, List<String>> params() {
		return params;
	}
	
	public String uri() {
		return request.uri();
	}
}
