package tech.hongjian.funnyserver.route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MixedAttribute;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiahongjian
 * @time 2019-03-04 16:51:21
 *
 */
@Slf4j
public class Request {
	private HttpRequest request;

	private Map<String, List<String>> params = new HashMap<>();

	private HttpHeaders httpHeaders;
	private Map<String, String> headers;

	private String url;
	private String uri;
	private String method;

	public Request(HttpRequest req) {
		init(req);
	}

	private void init(HttpRequest req) {
		this.request = req;
		this.httpHeaders = req.headers();

		this.url = request.uri();
		int pathEndIdx = url.indexOf('?');
		this.uri = pathEndIdx < 0 ? url : url.substring(0, pathEndIdx);
		this.method = req.method().name();

		handleQueryParams(req);
	}

	private void handleQueryParams(HttpRequest req) {
		if (url.contains("?")) {
			Map<String, List<String>> params = new QueryStringDecoder(url, CharsetUtil.UTF_8).parameters();
			if (params != null && !params.isEmpty()) {
				this.params.putAll(params);
			}
		}
		if ("GET".equals(method)) {
			return;
		}
		try {
			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(req);
			List<InterfaceHttpData> postList = decoder.getBodyHttpDatas();
			for (InterfaceHttpData data : postList) {
				MixedAttribute value = (MixedAttribute) data;
				value.setCharset(CharsetUtil.UTF_8);
				String val = value.getValue();
				if (params.containsKey(data.getName())) {
					params.get(data.getName()).add(val);
				} else {
					List<String> values = new ArrayList<>();
					values.add(val);
					params.put(data.getName(), values);
				}
			}
		} catch (IOException e) {
			log.warn("Failed to read request params from request body.", e);
		}
	}

	public String method() {
		return method;
	}

	public Map<String, String> headerds() {
		if (null == headers) {
			headers = new HashMap<>(httpHeaders.size());
			Iterator<Map.Entry<String, String>> entryIterator = httpHeaders.iteratorAsString();
			while (entryIterator.hasNext()) {
				Map.Entry<String, String> next = entryIterator.next();
				headers.put(next.getKey(), next.getValue());
			}
		}
		return headers;
	}

	public Map<String, List<String>> params() {
		return params;
	}

	public String uri() {
		return uri;
	}

	public String url() {
		return url;
	}

	public String queryString() {
		if (url == null || !url.contains("?")) {
			return "";
		}
		return url.substring(url.indexOf("?") + 1);
	}
}
