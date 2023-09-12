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
        Channel fromChannel = SessionFactory.getSession().getChannel(from);
        List<Channel> channels = GroupSessionFactory.getGroupSession().getMembersChannel(msg.getGroupName());
        GroupChatResponseMessage message = new GroupChatResponseMessage(from, msg.getContent());
        for (Channel ch : channels) {
            if (ch != fromChannel) {
                ch.writeAndFlush(message);
            }
        }
    }
}
