package com.rookie.server.handler;

import com.rookie.message.GroupJoinRequestMessage;
import com.rookie.message.GroupJoinResponseMessage;
import com.rookie.server.session.Group;
import com.rookie.server.session.GroupSessionFactory;
import com.rookie.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author yayee
 */
@ChannelHandler.Sharable
public class GroupJoinRequestMessageHandler extends SimpleChannelInboundHandler<GroupJoinRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        String username = msg.getUsername();
        Group group = GroupSessionFactory.getGroupSession().joinMember(groupName, username);
        Channel channel = SessionFactory.getSession().getChannel(username);
        // 成功加入群聊
        if (group != null) {
            channel.writeAndFlush(new GroupJoinResponseMessage(true, "加入群聊" + groupName + "成功"));
        }
        // 加入群聊失败 群聊不存在
        else {
            channel.writeAndFlush(new GroupJoinResponseMessage(false, "加入群聊" + groupName + "失败"));
        }
    }
}
