package com.rookie.netty.c3;

import com.rookie.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yayee
 */
@Slf4j
public class ByteBufStudy {

    public static void main(String[] args) {
        //extracted1();
        //extracted2();
        extracted3();
    }

    /**
     * 读取
     */
    private static void extracted3() {
        // 创建ByteBuf
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(16, 20);

        // 向buffer中写入数据
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        buffer.writeInt(5);
        ByteBufUtil.log(buffer);

        // 读取4个字节
        log.debug("{}", buffer.readByte());
        log.debug("{}", buffer.readByte());
        log.debug("{}", buffer.readByte());
        log.debug("{}", buffer.readByte());
        ByteBufUtil.log(buffer);

        // 通过mark与reset实现重复读取
        buffer.markReaderIndex();
        log.debug("{}", buffer.readInt());
        ByteBufUtil.log(buffer);

        // 恢复到mark标记处
        buffer.resetReaderIndex();
        ByteBufUtil.log(buffer);
    }

    /**
     * 写入与扩容
     */
    private static void extracted2() {
        // 创建ByteBuf
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(16, 20);
        ByteBufUtil.log(buffer);

        // 向buffer中写入数据
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        ByteBufUtil.log(buffer);

        /*
         网络传输中 默认习惯是 Big Endian 使用 writeInt(int value)
         Big Endian（大端写入），即 0x250，写入后 00 00 02 50
         */
        buffer.writeInt(5);
        ByteBufUtil.log(buffer);

        /*
         Little Endian（小端写入），即 0x250，写入后 50 02 00 00
         */
        buffer.writeIntLE(6);
        ByteBufUtil.log(buffer);

        /*
         - 扩容规则
           - 如何写入后数据大小未超过 512 字节，则选择下一个 16 的整数倍进行扩容
             - 例如写入后大小为 12 字节，则扩容后 capacity 是 16 字节
           - 如果写入后数据大小超过 512 字节，则选择下一个 2^n
             - 例如写入后大小为 513 字节，则扩容后 capacity 是 2^10=1024 字节（2^9=512 已经不够了）
           - 扩容不能超过 maxCapacity，否则会抛出java.lang.IndexOutOfBoundsException异常
         */
        buffer.writeLong(7);
        ByteBufUtil.log(buffer);
    }

    /**
     * 直接内存与堆内存
     */
    private static void extracted1() {
        // 创建ByteBuf
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(16);
        log.debug("{}", buffer.getClass());

        buffer = ByteBufAllocator.DEFAULT.heapBuffer(16);
        log.debug("{}", buffer.getClass());

        buffer = ByteBufAllocator.DEFAULT.directBuffer(16);
        log.debug("{}", buffer.getClass());

        ByteBufUtil.log(buffer);

        // 向buffer中写入数据
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append("a");
        }
        buffer.writeBytes(sb.toString().getBytes(StandardCharsets.UTF_8));

        // 查看写入结果
        ByteBufUtil.log(buffer);
    }
}
