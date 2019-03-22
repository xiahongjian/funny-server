package tech.hongjian.http_server.server;

import tech.hongjian.funny_server.config.ServerConfig;
import tech.hongjian.funny_server.server.NettyServer;

/**
 * Hello world!
 *
 */
public class TestServer {
    public static void main(String[] args) {
    	NettyServer.instance(ServerConfig.getPort(), true).start();
    }
}
