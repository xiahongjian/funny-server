package tech.hongjian.funnyserver.route.handler.ws;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatchers;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

/**
 * @author xiahongjian 
 * @time   2019-03-22 15:57:29
 *
 */
@Accessors(fluent = true)
public class WebSocketContext {
	public static Map<String, ChannelGroup> groups = new ConcurrentHashMap<>();
	public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	@Getter
	private WebSocketSession session;
	@Getter
	private String message;
	@Getter
	private String uri;
	private WebSocketHandler handler;
	private ReentrantLock look = new ReentrantLock();
	
	public WebSocketContext(WebSocketSession session, WebSocketHandler handler) {
		this.session = session;
		this.handler = handler;
		this.uri = handler.getPath();
		ChannelGroup group = groups.get(handler.getPath());
		if (group == null) {
			group = createNewGroup(uri);
		}
		group.add(session.handlerCtx().channel());
	}
	
	
	public WebSocketContext(WebSocketSession session, WebSocketHandler handler, String message) {
		this(session, handler);
		this.message = message;
	}
	
	public void message(String value) {
		session.handlerCtx().writeAndFlush(new TextWebSocketFrame(value));
	}
	
	public void disconnect() {
		session.handlerCtx().disconnect().addListener(ChannelFutureListener.CLOSE);
	}
	
	/**
	 * 同一个频道里广播消息
	 * @param msg 发送的消息
	 */
	public void broadcast(String msg) {
		groups.get(uri).writeAndFlush(wrap(msg), ChannelMatchers.isNot(session.handlerCtx().channel()));
	}
	
	/**
	 * 向当前客户端发送消息
	 * @param msg 发送的消息
	 */
	public void send(String msg) {
		session.handlerCtx().channel().writeAndFlush(wrap(msg));
	}
	
	private TextWebSocketFrame wrap(String msg) {
		return new TextWebSocketFrame(msg);
	}
	
	private ChannelGroup createNewGroup(String uri) {
		look.lock();
		ChannelGroup grp;
		try {
			grp = groups.get(uri);
			if (grp == null) {
				grp = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
				groups.put(uri, grp);
			}
		} finally {
			look.unlock();
		}
		return grp; 
	}
}
