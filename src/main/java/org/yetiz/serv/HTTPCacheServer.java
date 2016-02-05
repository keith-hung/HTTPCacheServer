package org.yetiz.serv;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Optional;


/**
 * Created by yeti on 16/2/4.
 */
public class HTTPCacheServer {
    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private Channel channel;

    public void start(int port) {
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            channel = bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 20480)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_RCVBUF, 40960)
                .option(ChannelOption.SO_SNDBUF, 40960)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ChildInitializer())
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .bind(port)
                .sync()
                .channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    public void stop() {
        Optional.ofNullable(channel).ifPresent(channel -> channel.close());
        Optional.ofNullable(boss).ifPresent(elg -> elg.shutdownGracefully());
        Optional.ofNullable(worker).ifPresent(elg -> elg.shutdownGracefully());
    }
}
