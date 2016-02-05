package org.yetiz.serv;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by yeti on 16/2/4.
 */
public class ChildInitializer extends ChannelInitializer<SocketChannel> {
    private final static ResourceOperationHandler resourceOperationHandler = new ResourceOperationHandler();
    private final static LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
            .addLast(loggingHandler)
            .addLast(new HttpServerCodec())
            .addLast(new HttpObjectAggregator(1024000))
            .addLast(resourceOperationHandler);
    }
}
