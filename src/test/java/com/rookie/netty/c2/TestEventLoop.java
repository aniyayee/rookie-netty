package com.rookie.netty.c2;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yayee
 */
@Slf4j
public class TestEventLoop {

    public static void main(String[] args) throws InterruptedException {
        // 创建拥有两个 EventLoop 的 NioEventLoopGroup 对应两个线程
        EventLoopGroup group = new NioEventLoopGroup(2);
        // 通过 next 方法可以获得下一个 EventLoop
        System.out.println(group.next());
        System.out.println(group.next());

        // 执行普通任务
        group.next().execute(() -> {
            log.info("hey!");
        });

        // 执行定时任务
        group.next().scheduleAtFixedRate(() -> {
            log.info("hi~");
        }, 0, 1, TimeUnit.SECONDS);

        log.info("main");
        TimeUnit.SECONDS.sleep(1);
        // 优雅的关闭
        group.shutdownGracefully();
    }
}
