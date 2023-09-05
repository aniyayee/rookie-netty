package com.rookie.netty.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import java.io.IOException;
import java.net.InetSocketAddress;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yayee
 */
@Slf4j
public class MyClient {

    public static void main(String[] args) throws InterruptedException, IOException {
        // 2. 带有 Future Promise 的类型都是和异步方法配套使用 用来处理结果
        ChannelFuture channelFuture = new Bootstrap()
            .group(new NioEventLoopGroup())
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new StringEncoder());
                }
            })
            // 1. 连接到服务器
            // 该方法为异步非阻塞方法 主线程调用后不会被阻塞 真正去执行连接操作的是 nio 线程（NioEventLoop 中的线程）
            .connect(new InetSocketAddress("127.0.0.1", 8080));

        // 2.1 使用 sync 方法同步处理结果
        channelFuture.sync(); // 阻塞住当前线程 直到 nio 线程连接建立完毕
        Channel channel = channelFuture.channel();  // 获取客户端-服务器之间的 Channel 对象
        log.debug("{}", channel);
        channel.writeAndFlush("hello, world");

        // 2.2 使用 addListener(回调对象) 方法异步处理结果
        // 当 connect 方法执行完毕后 也就是连接真正建立后 会在 nio 线程中调用 operationComplete 方法
        /*channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel = future.channel();
                log.info("{}", channel);
                channel.writeAndFlush("hey~");
            }
        });*/

        System.out.println("debug");
    }
}
