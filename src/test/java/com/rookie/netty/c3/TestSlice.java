package com.rookie.netty.c3;

import com.rookie.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author yayee
 */
public class TestSlice {

    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        // 向buffer中写入数据
        buf.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        ByteBufUtil.log(buf);

        // 在切片过程中，没有发生数据复制
        ByteBuf f1 = buf.slice(0, 5);
        ByteBuf f2 = buf.slice(5, 5);
        // 需要让分片的 buffer 引用计数加一
        f1.retain();
        f2.retain();
        ByteBufUtil.log(f1);
        ByteBufUtil.log(f2);

        System.out.println("释放原byteBuf内存:");
        buf.release();
        ByteBufUtil.log(f1);

        // 更改原始 buffer 中的值
        System.out.println("修改原byteBuf中的值:");
        buf.setByte(0, 'b');
        ByteBufUtil.log(buf);

        System.out.println("打印f1:");
        ByteBufUtil.log(f1);
    }
}
