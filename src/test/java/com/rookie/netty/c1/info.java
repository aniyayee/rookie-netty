/**
 * - channel 可以理解为数据的通道
 *
 * - msg 理解为流动的数据，最开始输入是 ByteBuf，经过 pipeline 中的各个 handler 加工，会变成其它类型对象，最后输出又变成 ByteBuf
 *
 * - handler 可以理解为数据的处理工序
 *   - 工序有多道，合在一起就是 pipeline（传递途径），pipeline 负责发布事件（读、读取完成…）传播给每个 handler，handler 对自己感兴趣的事件进行处理（重写了相应事件处理方法）
 *     - pipeline 中有多个 handler，处理时会依次调用其中的 handler
 *   - handler 分 Inbound 和 Outbound 两类
 *     - Inbound 入站
 *     - Outbound 出站
 *
 * - eventLoop 可以理解为处理数据的工人
 *   - eventLoop 可以管理多个 channel 的 io 操作，并一旦 eventLoop 负责了某个 channel，就会将其与channel进行绑定，以后该 channel 中的 io 操作都由该 eventLoop 负责
 *   - eventLoop 既可以执行 io 操作，也可以进行任务处理，每个 eventLoop 有自己的任务队列，队列里可以堆放多个 channel 的待处理任务，任务分为普通任务、定时任务
 *   - eventLoop 按照 pipeline 顺序，依次按照 handler 的规划（代码）处理数据，可以为每个 handler 指定不同的 eventLoop
 */
