package tech.hongjian.funnyserver.route;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.ReferenceCountUtil;

/**
 * @author xiahongjian 
 * @time   2019-03-05 09:22:44
 *
 */
public class Response {
	private Map<CharSequence, Set<Object>> headers = new HashMap<>();
	private HttpVersion version;
	private HttpResponseStatus status;
	private ByteBuf content;
	
	public static final Response NOT_FOUND = new Response(HttpResponseStatus.NOT_FOUND, Unpooled.wrappedBuffer("Not Found".getBytes())); 
	public static final Response BAD_REQUEST = new Response(HttpResponseStatus.BAD_REQUEST, Unpooled.wrappedBuffer("Bad Request".getBytes()));
	
	public Response() {}
	
	public Response(HttpVersion version, HttpResponseStatus status, ByteBuf content) {
		this.version = version;
		this.status = status;
		this.content = content;
	}
	
	public Response(HttpResponseStatus status, ByteBuf content) {
		this(HttpVersion.HTTP_1_1, status, content);
	}
	
	public Response(HttpResponseStatus status) {
		this(HttpVersion.HTTP_1_1, status, Unpooled.EMPTY_BUFFER);
	}
	
	public void addHeader(CharSequence name, Object value) {
		Set<Object> values = headers.get(name);
		if (values == null) {
			values = new HashSet<>(1);
			headers.put(name, values);
		}
		values.add(value);
	}
	
	public void addHeader(CharSequence name, Iterable<Object> values) {
		Set<Object> vals = headers.get(name);
		if (vals == null) {
			vals = new HashSet<>();
			headers.put(name, vals);
		}
		for (Object v : values) {
			vals.add(v);
		}
	}
		
	public Set<Object> getHeader(CharSequence name) {
		return headers.containsKey(name) ? headers.get(name) : Collections.emptySet();
	}
	
	public Map<CharSequence, Set<Object>> headers() {
		return headers;
	}
	
	public HttpVersion version() {
		return version;
	}
	public HttpResponseStatus status() {
		return status;
	}
	public ByteBuf content() {
		ReferenceCountUtil.retain(content);
		return content;
	}
}
