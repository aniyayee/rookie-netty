/**
 * - 基本规则是 **谁是最后使用者 谁负责 release**
 *
 * - 详细分析如下:
 *
 * - 起点，对于 NIO 实现来讲，在 io.netty.channel.nio.AbstractNioByteChannel.NioByteUnsafe#read 方法中首次创建 ByteBuf
 *   放入 pipeline（pipeline.fireChannelRead(byteBuf)）
 *
 * - 入站 ByteBuf 处理原则
 *   - 对原始 ByteBuf 不做处理，调用 ctx.fireChannelRead(msg) 向后传递，这时无须 release
 *   - 将原始 ByteBuf 转换为其它类型的 Java 对象，这时 ByteBuf 就没用了，必须 release
 *   - 如果不调用 ctx.fireChannelRead(msg) 向后传递，那么也必须 release
 *   - 注意各种异常，如果 ByteBuf 没有成功传递到下一个 ChannelHandler，必须 release
 *   - 假设消息一直向后传，那么 TailContext 会负责释放未处理消息（原始的 ByteBuf）
 *
 * - 出站 ByteBuf 处理原则
 *   - 出站消息最终都会转为 ByteBuf 输出，一直向前传，由 HeadContext flush 后 release
 *
 * - 异常处理原则
 *   - 有时候不清楚 ByteBuf 被引用了多少次，但又必须彻底释放，可以循环调用 release 直到返回 true
 */
