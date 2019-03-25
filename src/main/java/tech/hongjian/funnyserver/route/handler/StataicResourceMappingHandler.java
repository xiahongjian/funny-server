package tech.hongjian.funnyserver.route.handler;

import java.io.File;

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
 * @time   2019-03-04 16:45:17
 *
 */
public class StataicResourceMappingHandler implements RequestMapingHandler {
	private File file;
	private String contentType;
	
	public static final String CSS_CONTENT_TYPE = "text/css; charset=utf-8";
	public static final String JS_CONTENT_TYPE = "text/javascript; charset=utf-8";
	public static final String HTML_CONTENT_TYPE = "text/html; charset=utf-8";
	public static final String TEXT_CONTENT_TYPE = "text/plain; charset=utf-8";
	
	
	public StataicResourceMappingHandler(File file) {
		this.file = file;
		String name = file.getName();
		contentType = determainContentType(name);
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
		Response resp = new Response(HttpResponseStatus.OK, Unpooled.wrappedBuffer(IOUtil.toString(file).getBytes()));
		if (contentType != null) {
			resp.addHeader(HttpHeaderNames.CONTENT_TYPE, new AsciiString(contentType));
		}
		resp.addHeader(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
		return resp;
	}
	
	public static StataicResourceMappingHandler of(File file) {
		return new StataicResourceMappingHandler(file);
	}
}
