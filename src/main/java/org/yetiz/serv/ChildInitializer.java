package org.yetiz.serv;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.internal.chmv8.ConcurrentHashMapV8;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by yeti on 16/2/4.
 */
public class ChildInitializer extends ChannelInitializer<SocketChannel> {
    public static ConcurrentHashMapV8<String, byte[]> mapV8 = new ConcurrentHashMapV8(1024);

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
            .addLast(new LoggingHandler(LogLevel.DEBUG))
            .addLast(new HttpServerCodec())
            .addLast(new HttpObjectAggregator(81920))
            .addLast(new ChannelDuplexHandler() {
                @Override
                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                    FullHttpRequest request = (FullHttpRequest) msg;
                    String key = request.getUri().substring(1).replace('/', '.');
                    if (key == null) {
                        respondNotFound(ctx);
                        return;
                    }

                    if (request.getMethod().equals(HttpMethod.GET)) {
                        byte[] data = ChildInitializer.mapV8.get(key);
                        if (data == null) {
                            respondNotFound(ctx);
                            return;
                        } else {
                            respondOkWithData(ctx, data);
                            return;
                        }

                    } else if (request.getMethod().equals(HttpMethod.POST)) {
                        byte[] body = new byte[request.content().capacity()];
                        request.content().readBytes(body);
                        mapV8.put(key, body);
                        respondOk(ctx);

                    } else if (request.getMethod().equals(HttpMethod.DELETE)) {
                        if (mapV8.remove(key) == null) {
                            respondNotFound(ctx);
                            return;
                        }

                        respondOk(ctx);
                    }
                }

                private void respondNotFound(ChannelHandlerContext ctx) {
                    final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus
                        .NOT_FOUND);
                    HttpHeaders.setKeepAlive(response, false);
                    HttpHeaders.setHeader(response, "Content-Type", "text/html; charset=utf-8");
                    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                }

                private void respondOkWithData(ChannelHandlerContext ctx, byte[] data) {
                    final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK,
                        Unpooled.wrappedBuffer(data));
                    HttpHeaders.setKeepAlive(response, false);
                    HttpHeaders.setHeader(response, "Content-Type", "text/html; charset=utf-8");
                    HttpHeaders.setContentLength(response, data.length);
                    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                }

                private void respondOk(ChannelHandlerContext ctx) {
                    final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK,
                        Unpooled.wrappedBuffer(new byte[]{}));
                    HttpHeaders.setKeepAlive(response, false);
                    HttpHeaders.setHeader(response, "Content-Type", "text/html; charset=utf-8");
                    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                }
            });
    }
}
