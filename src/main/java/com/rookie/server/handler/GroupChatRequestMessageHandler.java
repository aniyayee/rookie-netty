package com.rookie.server.handler;

import com.rookie.message.GroupChatRequestMessage;
import com.rookie.message.GroupChatResponseMessage;
import com.rookie.server.session.GroupSessionFactory;
import com.rookie.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.List;

/**
 * @author yayee
 */
@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        String from = msg.getFrom();
        String groupName = msg.getGroupName();
        Channel fromChannel = SessionFactory.getSession().getChannel(from);
        List<Channel> channels = GroupSessionFactory.getGroupSession().getMembersChannel(groupName);
        // 禁止非群成员发送消息
        if (!channels.contains(fromChannel)) {
            ctx.writeAndFlush(new GroupChatResponseMessage(false, "发送失败，您未在群聊[" + groupName + "]"));
            return;
        }
        for (Channel ch : channels) {
            if (ch != fromChannel) {
                ch.writeAndFlush(new GroupChatResponseMessage(from, msg.getContent()));
            }
        }
        ctx.writeAndFlush(new GroupChatResponseMessage(true, "发送成功"));
    }
}
