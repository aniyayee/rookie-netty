package com.rookie.chat.server.handler;

import com.rookie.chat.message.ChatRequestMessage;
import com.rookie.chat.message.ChatResponseMessage;
import com.rookie.chat.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author yayee
 */
@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String to = msg.getTo();
        // 接收方 channel
        Channel channel = SessionFactory.getSession().getChannel(to);
        // 不在线
        if (channel == null) {
            ctx.writeAndFlush(new ChatResponseMessage(false, "对方用户不存在或者不在线"));
        }
        // 在线
        else {
            channel.writeAndFlush(new ChatResponseMessage(msg.getFrom(), msg.getContent()));
            ctx.writeAndFlush(new ChatResponseMessage(true, "发送成功"));
        }
    }
}
