package tech.hongjian.http_server.controller.ws;

import lombok.extern.slf4j.Slf4j;
import tech.hongjian.funny_server.annnotation.OnClose;
import tech.hongjian.funny_server.annnotation.OnMessage;
import tech.hongjian.funny_server.annnotation.OnOpen;
import tech.hongjian.funny_server.annnotation.WSContorller;
import tech.hongjian.funny_server.route.handler.ws.WebSocketContext;

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
		log.info("ws connection is opent");
	}
	
	@OnClose
	public void onClose(WebSocketContext ctx) {
		log.info("ws connection is colse");
	}
	
	@OnMessage
	public void onMessage(WebSocketContext ctx) {
		log.info(ctx.message());
	}

}
