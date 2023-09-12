package com.rookie.server.handler;

import com.rookie.message.GroupCreateRequestMessage;
import com.rookie.message.GroupCreateResponseMessage;
import com.rookie.server.session.Group;
import com.rookie.server.session.GroupSession;
import com.rookie.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.List;
import java.util.Set;

/**
 * @author yayee
 */
@ChannelHandler.Sharable
public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Group group = groupSession.createGroup(groupName, members);
        GroupCreateResponseMessage message;
        if (group == null) {
            // 发生成功消息
            ctx.writeAndFlush(new GroupCreateResponseMessage(true, groupName + "创建成功"));
            // 发送拉群消息
            message = new GroupCreateResponseMessage(true, "您已被拉入" + groupName);
            List<Channel> channels = groupSession.getMembersChannel(groupName);
            for (Channel ch : channels) {
                ch.writeAndFlush(message);
            }
        } else {
            message = new GroupCreateResponseMessage(false, groupName + "群聊已存在");
            ctx.writeAndFlush(message);
        }
    }
}