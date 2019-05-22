package tech.hongjian.funnyserver.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import tech.hongjian.funnyserver.server.handler.ServerInitializer;

/**
 * @author xiahongjian 
 * @time   2019-02-01 11:00:59
 *
 */
@Slf4j
public class FunnyServer {
	private int port;
	private boolean supportWebSorcket;
	
	public FunnyServer(int port, boolean supportWebSocket) {
		this.port = port;
		this.supportWebSorcket = supportWebSocket;
	}
	
	public static FunnyServer instance(int port, boolean supportWebSocket) {
		return new FunnyServer(port, supportWebSocket);
	}
	
	public void start() {
		EventLoopGroup boss = new NioEventLoopGroup(1);
    	EventLoopGroup worker = new NioEventLoopGroup();
        try {
        	ServerBootstrap b = new ServerBootstrap();
        	b.option(ChannelOption.SO_BACKLOG, 1024);
        	b.group(boss, worker)
        	.channel(NioServerSocketChannel.class)
//        	.handler(new LoggingHandler(LogLevel.INFO))
        	.childHandler(new ServerInitializer(supportWebSorcket));
			Channel ch = b.bind(port).sync().channel();
			log.info("FunnyServer starts on port {}, WebSocket supported: {}.", port, supportWebSorcket);
			ch.closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
	
}
