package com.rookie.netty.c2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yayee
 */
@Slf4j
public class PipeLineServer {

    public static void main(String[] args) {
        new ServerBootstrap()
            .group(new NioEventLoopGroup())
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    /*
                     在 SocketChannel 的 pipeline 中添加 handler
                     pipeline 中 handler 是带有 head 与 tail 节点的双向链表 它实际结构为 head <-> handler1 <-> ... <-> handler4 <->tail
                     Inbound 主要处理入站操作 一般为读操作 发生入站操作时会触发 Inbound 方法
                     入站时 handler 是从 head 向后调用的
                     */
                    // 1. 通过 channel 拿到 pipeline
                    ChannelPipeline pipeline = ch.pipeline();
                    // 2. 添加处理器 head <->  h1 <-> h2 <->  h5 <-> h3 <-> h4 <-> h6 <-> tail
                    pipeline.addLast("h1", new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf buf = (ByteBuf) msg;
                            log.debug("1: {}", buf.toString(Charset.defaultCharset()));
                            // 将数据传递给下个 handler 如果不调用 调用链会断开 或者调用 ctx.fireChannelRead(msg);
                            super.channelRead(ctx, msg);
                        }
                    });
                    pipeline.addLast("h2", new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf buf = (ByteBuf) msg;
                            log.debug("2: {}", buf.toString(Charset.defaultCharset()));
                            super.channelRead(ctx, msg);
                        }
                    });
                    pipeline.addLast("h5", new ChannelOutboundHandlerAdapter() {
                        @Override
                        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
                            throws Exception {
                            ByteBuf buf = (ByteBuf) msg;
                            log.debug("5: {}", buf.toString(Charset.defaultCharset()));
                            super.write(ctx, msg, promise);
                        }
                    });
                    pipeline.addLast("h3", new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf buf = (ByteBuf) msg;
                            log.debug("3: {}", buf.toString(Charset.defaultCharset()));
                            // 执行 write 操作 使得 Outbound 方法能够得到调用
                            // ctx.writeAndFlush 从当前 handler 向前寻找OutboundHandler
                            //ctx.writeAndFlush(ctx.alloc().buffer().writeBytes("server...".getBytes()));

                            // ch.writeAndFlush 从 tail 向前寻找 OutboundHandler
                            ch.writeAndFlush(ctx.alloc().buffer().writeBytes("server...".getBytes()));
                        }
                    });

                    /*
                     Outbound 主要处理出站操作 一般为写操作 发生出站操作时会触发 Outbound 方法
                     出站时 handler的调用是从tail向前调用的
                     */
                    pipeline.addLast("h4", new ChannelOutboundHandlerAdapter() {
                        @Override
                        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
                            throws Exception {
                            ByteBuf buf = (ByteBuf) msg;
                            log.debug("4: {}", buf.toString(Charset.defaultCharset()));
                            super.write(ctx, msg, promise);
                        }
                    });
                    pipeline.addLast("h6", new ChannelOutboundHandlerAdapter() {
                        @Override
                        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
                            throws Exception {
                            ByteBuf buf = (ByteBuf) msg;
                            log.debug("6: {}", buf.toString(Charset.defaultCharset()));
                            super.write(ctx, msg, promise);
                        }
                    });
                }
            })
            .bind(8080);
    }
}
