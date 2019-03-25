package tech.hongjian.funnyserver.route.handler.ws;

import java.util.UUID;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author xiahongjian 
 * @time   2019-03-22 15:58:50
 *
 */
@Getter
@Accessors(fluent = true)
public class WebSocketSession {
	private ChannelHandlerContext handlerCtx;
	private String uuid;
	
	public WebSocketSession(ChannelHandlerContext handlerCtx) {
		this.handlerCtx = handlerCtx;
		this.uuid = UUID.randomUUID().toString().replace("-", "");
	}
}
