package tech.hongjian.funnyserver.controller.ws;

import lombok.extern.slf4j.Slf4j;
import tech.hongjian.funnyserver.annnotation.OnClose;
import tech.hongjian.funnyserver.annnotation.OnMessage;
import tech.hongjian.funnyserver.annnotation.OnOpen;
import tech.hongjian.funnyserver.annnotation.WSContorller;
import tech.hongjian.funnyserver.route.handler.ws.WebSocketContext;

/**
 * @author xiahongjian 
 * @time   2019-03-22 17:52:02
 *
 */
@Slf4j
@WSContorller("/ws")
public class DemoWsController {
	
	@OnOpen
	public void onOpen(WebSocketContext ctx) {
		ctx.send("Welcome!");
		log.info("ws connection is open, SessionID: {}", ctx.session().uuid());
	}
	
	@OnClose
	public void onClose(WebSocketContext ctx) {
		ctx.send("Bye!");	// 当发送过来的是CloseWebSocketFrame，我们将无法再向客户端发送数据
		log.info("ws connection is colse, SessionID: {}", ctx.session().uuid());
	}
	
	@OnMessage
	public void onMessage(WebSocketContext ctx) {
		ctx.broadcast(ctx.message());
		ctx.send("Got");
		log.info("Received message: {}, SessionID: {}", ctx.message(), ctx.session().uuid());
	}

}
