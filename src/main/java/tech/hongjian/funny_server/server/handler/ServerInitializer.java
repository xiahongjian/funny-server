package tech.hongjian.funny_server.server.handler;

import java.util.Set;
import java.util.stream.Collectors;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import tech.hongjian.funny_server.annnotation.WSContorller;
import tech.hongjian.funny_server.config.ServerConfig;
import tech.hongjian.funny_server.util.scanner.BeanContainer;
import tech.hongjian.funny_server.util.scanner.reader.DynamicContext;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

	public ServerInitializer(boolean supportWebSocket) {
		if (supportWebSocket) {
			Set<Class<?>> classes = DynamicContext
					.recursionFindClasses(ServerConfig.getString(ServerConfig.WEBSOCKET_PACKAGE, ""), null,
							WSContorller.class)
					.map(classInfo -> classInfo.getClazz()).collect(Collectors.toSet());
			BeanContainer.add(classes);
		}
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();

		p.addLast(new HttpServerCodec());

		p.addLast(new HttpContentCompressor());

		p.addLast(new HttpObjectAggregator(1048576));

		p.addLast(new ChunkedWriteHandler());

		p.addLast(new WebSocketHandler());

		p.addLast(new HttpServerHandler());
	}

}
