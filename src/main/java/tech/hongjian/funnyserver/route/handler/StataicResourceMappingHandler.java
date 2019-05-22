package tech.hongjian.funnyserver.route.handler;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.AsciiString;
import tech.hongjian.funnyserver.route.Request;
import tech.hongjian.funnyserver.route.Response;
import tech.hongjian.funnyserver.util.IOUtil;

/**
 * @author xiahongjian
 * @time 2019-03-04 16:45:17
 *
 */
public class StataicResourceMappingHandler implements RequestMapingHandler {
	public static final String CSS_CONTENT_TYPE = "text/css; charset=utf-8";
	public static final String JS_CONTENT_TYPE = "text/javascript; charset=utf-8";
	public static final String HTML_CONTENT_TYPE = "text/html; charset=utf-8";
	public static final String TEXT_CONTENT_TYPE = "text/plain; charset=utf-8";

	private static final Map<String, ContentWrapper> RESOURCES = new ConcurrentHashMap<>();

	private static final StataicResourceMappingHandler INSTANCE = new StataicResourceMappingHandler();

	public static StataicResourceMappingHandler instance() {
		return INSTANCE;
	}

	public StataicResourceMappingHandler add(String uri, File file) {
		RESOURCES.put(uri, new ContentWrapper(determainContentType(file.getName()), file));
		return this;
	}

	private String determainContentType(String name) {
		if (name.endsWith(".css")) {
			return CSS_CONTENT_TYPE;
		} else if (name.endsWith(".js")) {
			return JS_CONTENT_TYPE;
		} else if (name.endsWith(".html") || name.endsWith(".htm") || name.endsWith(".shtml")) {
			return HTML_CONTENT_TYPE;
		} else if (name.endsWith(".txt")) {
			return TEXT_CONTENT_TYPE;
		} else {
			return TEXT_CONTENT_TYPE;
		}
	}

	@Override
	public Response invok(Request request) {
		ContentWrapper wrapper = RESOURCES.get(request.uri());
		Response resp = new Response(HttpResponseStatus.OK, Unpooled.wrappedBuffer(IOUtil.toString(wrapper.file).getBytes()));
		resp.addHeader(HttpHeaderNames.CONTENT_TYPE, new AsciiString(wrapper.contentType));
		resp.addHeader(HttpHeaderNames.TRANSFER_ENCODING, new AsciiString(HttpHeaderValues.CHUNKED));
		return resp;
	}

	private static class ContentWrapper {
		String contentType;
		File file;

		public ContentWrapper(String contentType, File file) {
			this.contentType = contentType;
			this.file = file;
		}
	}
}
