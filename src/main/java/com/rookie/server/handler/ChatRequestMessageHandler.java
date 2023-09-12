package com.rookie.server.handler;

import com.rookie.message.ChatRequestMessage;
import com.rookie.message.ChatResponseMessage;
import com.rookie.server.session.SessionFactory;
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
        ChatResponseMessage message;
        // 不在线
        if (channel == null) {
            message = new ChatResponseMessage(false, "对方用户不存在或者不在线");
            ctx.writeAndFlush(message);
        }
        // 在线
        else {
            message = new ChatResponseMessage(msg.getFrom(), msg.getContent());
            channel.writeAndFlush(message);
        }
    }
}
