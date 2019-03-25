package tech.hongjian.funnyserver.route.handler.ws;

import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author xiahongjian 
 * @time   2019-03-22 15:57:29
 *
 */
@Accessors(fluent = true)
public class WebSocketContext {
	@Getter
	private WebSocketSession session;
	@Getter
	private String message;
	private WebSocketHandler handler;
	
	public WebSocketContext(WebSocketSession session, WebSocketHandler handler) {
		this.session = session;
		this.handler = handler;
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
		handler.onDisconnect(this);
	}
}
