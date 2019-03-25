package tech.hongjian.funnyserver.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import tech.hongjian.funnyserver.route.Request;
import tech.hongjian.funnyserver.route.Response;
import tech.hongjian.funnyserver.route.RoutesManager;

/**
 * @author xiahongjian 
 * @time   2019-02-01 10:58:17
 *
 */
public class Router {
	private static final Logger logger = LoggerFactory.getLogger(Router.class);
	
	public static Response transfer(ChannelHandlerContext ctx, HttpRequest req) {
		Request request = new Request(req);
		logger.info(String.format("%-6s | %s", request.method().toString(), request.uri()));
		return RoutesManager.request(request);
	}
}
