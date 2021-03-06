package tech.hongjian.funnyserver.server.handler;

import java.util.concurrent.CompletableFuture;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.ReferenceCountUtil;
import tech.hongjian.funnyserver.route.RoutesManager;
import tech.hongjian.funnyserver.route.handler.ws.WebSocketContext;
import tech.hongjian.funnyserver.route.handler.ws.WebSocketHandlerWrapper;
import tech.hongjian.funnyserver.route.handler.ws.WebSocketSession;

/**
 * @author xiahongjian
 * @time 2019-03-22 15:02:09
 *
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<Object> {
	private WebSocketServerHandshaker handshaker;
	private WebSocketSession session;
	private tech.hongjian.funnyserver.route.handler.ws.WebSocketHandler handler;
	private String uri;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HttpRequest) {
			handleHttpRequest(ctx, (HttpRequest) msg);
		} else if (msg instanceof WebSocketFrame) {
			initHandlerWrapper();
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
		} else {
			ReferenceCountUtil.retain(msg);
			ctx.fireChannelRead(msg);
		}

	}

	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame msg) {
		if (msg instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) msg.retain());
			initHandlerWrapper();
			CompletableFuture.completedFuture(new WebSocketContext(session, handler))
					.thenAcceptAsync(handler::onDisconnect, ctx.executor());
			return;
		}

		if (msg instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(msg.content().retain()));
			return;
		}

		if (!(msg instanceof TextWebSocketFrame)) {
			throw new UnsupportedOperationException("Unsupport fram type: " + msg.getClass().getName());
		}

		CompletableFuture.completedFuture(new WebSocketContext(session, handler, ((TextWebSocketFrame) msg).text()))
				.thenAcceptAsync(handler::onText, ctx.executor());
	}

	private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest req) {
		if (isWebSocketRequest(req)) {
			WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(req.uri(), null, true);
			handshaker = wsFactory.newHandshaker(req);
			if (handshaker == null) {
				WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
			} else {
				handshaker.handshake(ctx.channel(), req);
				session = new WebSocketSession(ctx);
				uri = req.uri();
				initHandlerWrapper();
				// TODO 用户身份认证
				// 获取请求参数中的token并验证
				CompletableFuture.completedFuture(new WebSocketContext(session, handler, uri))
						.thenAcceptAsync(handler::onConnect, ctx.executor());
			}
		} else {
			ReferenceCountUtil.retain(req);
			ctx.fireChannelRead(req);
		}
	}

	private boolean isWebSocketRequest(HttpRequest req) {
		return req != null && ((handler = RoutesManager.getWebSocket(req.uri())) != null) && req.decoderResult().isSuccess()
				&& "websocket".equals(req.headers().get("Upgrade"));
	}

	private void initHandlerWrapper() {
		if (this.handler != null && this.handler instanceof WebSocketHandlerWrapper) {
			((WebSocketHandlerWrapper) this.handler).setPath(this.uri);
		}
	}
}
