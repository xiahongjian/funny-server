package tech.hongjian.funnyserver.server;

import tech.hongjian.funnyserver.config.ServerConfig;
import tech.hongjian.funnyserver.server.NettyServer;

/**
 * Hello world!
 *
 */
public class TestServer {
    public static void main(String[] args) {
    	NettyServer.instance(ServerConfig.getPort(), true).start();
    }
}
