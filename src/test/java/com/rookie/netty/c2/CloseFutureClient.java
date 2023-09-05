package com.rookie.netty.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.net.InetSocketAddress;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yayee
 */
@Slf4j
public class CloseFutureClient {

    public static void main(String[] args) throws InterruptedException {
        // 创建EventLoopGroup，使用完毕后关闭
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
            .group(group)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new StringEncoder());
                }
            })
            .connect(new InetSocketAddress("127.0.0.1", 8080));
        channelFuture.sync();

        Channel channel = channelFuture.channel();
        log.debug("channel: {}", channel);
        Scanner scanner = new Scanner(System.in);

        new Thread(() -> {
            while (true) {
                String msg = scanner.nextLine();
                if ("q".equals(msg)) {
                    // close 异步操作 在 nio 线程中执行
                    channel.close();
                    //log.debug("处理关闭之后的操作"); // 不能在这里善后
                    break;
                }
                channel.writeAndFlush(msg);
            }
        }, "inputThread").start();

        // 获得 closeFuture 对象
        ChannelFuture closeFuture = channel.closeFuture();
        log.debug("waiting close...");

        // 同步等待 nio 线程执行完 close 操作
        /*closeFuture.sync();
        log.debug("处理关闭之后的操作");
        log.debug("class: {}", closeFuture.getClass());
        // 关闭 EventLoopGroup
        group.shutdownGracefully();*/

        closeFuture.addListener((ChannelFutureListener) future -> {
            // 等待 channel 关闭后才执行的操作
            log.debug("处理关闭之后的操作");
            log.debug("class: {}", closeFuture.getClass());
            // 关闭 EventLoopGroup
            group.shutdownGracefully();
        });
    }
}
