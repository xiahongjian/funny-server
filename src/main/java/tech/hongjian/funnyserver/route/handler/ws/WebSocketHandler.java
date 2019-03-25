package tech.hongjian.funnyserver.route.handler.ws;

/**
 * @author xiahongjian 
 * @time   2019-03-22 15:12:46
 *
 */
public interface WebSocketHandler {
	
	void onConnect(WebSocketContext ctx);
	
	void onText(WebSocketContext ctx);
	
	void onDisconnect(WebSocketContext ctx);
}
