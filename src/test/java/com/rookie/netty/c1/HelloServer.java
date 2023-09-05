package com.rookie.netty.c1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author yayee
 */
public class HelloServer {

    public static void main(String[] args) {
        // 1. 启动器 负责装配netty组件 启动服务器
        new ServerBootstrap()
            // 2. 创建 NioEventLoopGroup 可以简单理解为 线程池 + Selector
            .group(new NioEventLoopGroup())
            // 3. 选择 服务器的 ServerSocketChannel 实现
            .channel(NioServerSocketChannel.class)
            // 4. child 负责处理读写 该方法决定了 child 执行哪些操作
            // ChannelInitializer 处理器（仅执行一次）
            // 它的作用是待客户端 SocketChannel 建立连接后 执行 initChannel 以便添加更多的处理器
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    // 5. SocketChannel 的处理器 使用 StringDecoder 解码 ByteBuf -> String
                    ch.pipeline().addLast(new StringDecoder());
                    // 6. SocketChannel 的业务处理 使用上一个处理器的处理结果
                    ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                            System.out.println(msg);
                        }
                    });
                }
                // 7. ServerSocketChannel 绑定 8080 端口
            }).bind(8080);
    }
}
