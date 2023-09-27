package com.rookie.rpc.client;

import com.rookie.protocol.MessageCodecSharable;
import com.rookie.protocol.ProtocolFrameDecoder;
import com.rookie.protocol.SequenceIdGenerator;
import com.rookie.rpc.client.handler.RpcResponseMessageHandler;
import com.rookie.rpc.message.RpcRequestMessage;
import com.rookie.rpc.server.service.HelloService;
import com.rookie.rpc.server.service.MathService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultPromise;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yayee
 */
@Slf4j
public class RpcClientManager {

    private static final Object LOCK = new Object();
    private static final String DEFAULT_HOST = "8.136.199.219";
    // private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 8080;
    private static volatile Channel channel = null;

    public static void main(String[] args) {
        launch1();
        launch2();
    }

    private static void launch1() {
        // 创建代理对象
        HelloService service = getProxyService(HelloService.class);
        // 通过代理对象执行方法
        System.out.println("HelloService.sayHello: " + service.sayHello("Hulu"));
        System.out.println("HelloService.sayHello: " + service.sayHello("aniya"));
    }

    private static void launch2() {
        MathService service = getProxyService(MathService.class);
        int a = 1;
        int b = 2;
        System.out.println("MathService.add: a + b = " + service.add(a, b));
    }

    /**
     * 单例模式创建 channel
     */
    public static Channel getChannel() {
        if (channel == null) {
            synchronized (LOCK) {
                if (channel == null) {
                    initChannel();
                }
            }
        }
        return channel;
    }

    /**
     * 使用代理模式，帮助我们创建请求消息并发送
     */
    public static <T> T getProxyService(Class<T> serviceClass) {
        ClassLoader classLoader = serviceClass.getClassLoader();
        Class<?>[] interfaces = new Class<?>[]{serviceClass};
        // 使用JDK代理，创建代理对象
        Object o = Proxy.newProxyInstance(classLoader, interfaces, new InvocationHandler() {
            @Override   //                           sayHello  "张三"
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 1. 将方法调用转换为 消息对象
                int sequenceId = SequenceIdGenerator.nextId();
                RpcRequestMessage msg = new RpcRequestMessage(
                    sequenceId,
                    serviceClass.getName(),
                    method.getName(),
                    method.getReturnType(),
                    method.getParameterTypes(),
                    args
                );
                // 2. 将消息对象发送出去
                getChannel().writeAndFlush(msg);

                // 3. 准备一个空 promise 对象，来接收结果             指定 promise 对象异步接收结果线程
                DefaultPromise<Object> promise = new DefaultPromise<>(getChannel().eventLoop());
                RpcResponseMessageHandler.PROMISES.put(sequenceId, promise);
                // 4. 等待 promise 结果
                promise.await();
                if (promise.isSuccess()) {
                    // 调用方法成功，返回方法执行结果
                    return promise.getNow();
                } else {
                    // 调用方法失败，抛出异常
                    throw new RuntimeException(promise.cause());
                }
            }
        });
        return (T) o;
    }

    private static void initChannel() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();

        // RPC 请求消息处理器
        RpcResponseMessageHandler RPC_HANDLER = new RpcResponseMessageHandler();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProtocolFrameDecoder());
                ch.pipeline().addLast(LOGGING_HANDLER);
                ch.pipeline().addLast(MESSAGE_CODEC);
                ch.pipeline().addLast(RPC_HANDLER);
            }
        });
        try {
            channel = bootstrap.connect(new InetSocketAddress(DEFAULT_HOST, DEFAULT_PORT)).sync().channel();
            // 异步关闭 group，避免 channel 被阻塞
            channel.closeFuture().addListener(future -> {
                group.shutdownGracefully();
            });
        } catch (InterruptedException e) {
            log.error("client error", e);
        }
    }
}
