package tech.hongjian.funnyserver.route.handler;

import tech.hongjian.funnyserver.route.Request;
import tech.hongjian.funnyserver.route.Response;

/**
 * @author xiahongjian 
 * @time   2019-03-04 16:35:37
 *
 */
public interface RequestMapingHandler {
	Response invok(Request request);
}
