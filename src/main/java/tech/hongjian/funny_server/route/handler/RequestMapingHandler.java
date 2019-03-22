package tech.hongjian.funny_server.route.handler;

import tech.hongjian.funny_server.route.Request;
import tech.hongjian.funny_server.route.Response;

/**
 * @author xiahongjian 
 * @time   2019-03-04 16:35:37
 *
 */
public interface RequestMapingHandler {
	Response invok(Request request);
}
