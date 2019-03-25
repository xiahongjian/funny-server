package tech.hongjian.funnyserver.server.handler;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.slf4j.Slf4j;
import tech.hongjian.funnyserver.route.Response;

/**
 * @author xiahongjian 
 * @time   2019-02-01 09:55:26
 *
 */
@Slf4j
public class HttpServerHandler extends ChannelInboundHandlerAdapter {
	private final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HttpRequest) {
			handleHttpRequest(ctx, (HttpRequest) msg);
		}
	}

	private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest req) {
		if (req.decoderResult().isFailure()) {
			ctx.write(createFullHttpResponse(Response.BAD_REQUEST)).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		if (HttpUtil.is100ContinueExpected(req)) {
			ctx.write(new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
			return;
		} 
		
		FullHttpResponse resp = createFullHttpResponse(Router.transfer(ctx, req));
		boolean keepAlive =  HttpUtil.isKeepAlive(req);
		
		if (!keepAlive) {
			ctx.write(resp).addListener(ChannelFutureListener.CLOSE);
		} else {
			resp.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
			ctx.write(resp);
		}
	}

	private FullHttpResponse createFullHttpResponse(Response resp) {
		final FullHttpResponse reponse = new DefaultFullHttpResponse(resp.version(), resp.status(), resp.content());
		Map<CharSequence, Set<Object>> headers = resp.headers();
		headers.forEach((name, values) -> {
			if (!values.isEmpty()) {
				reponse.headers().set(name, values);
			}
		}); 
		return reponse;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		logger.error(cause.getMessage(), cause);
	}

	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

}
