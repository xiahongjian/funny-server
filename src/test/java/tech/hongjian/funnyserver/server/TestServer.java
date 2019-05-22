package tech.hongjian.funnyserver.server;

import tech.hongjian.funnyserver.config.ServerConfig;
import tech.hongjian.funnyserver.server.FunnyServer;

/**
 * Hello world!
 *
 */
public class TestServer {
    public static void main(String[] args) {
    	FunnyServer.instance(ServerConfig.getPort(), true).start();
    }
}
