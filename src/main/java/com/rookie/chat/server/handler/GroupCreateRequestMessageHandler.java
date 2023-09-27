package com.rookie.chat.server.handler;

import com.rookie.chat.message.GroupCreateRequestMessage;
import com.rookie.chat.message.GroupCreateResponseMessage;
import com.rookie.chat.server.session.Group;
import com.rookie.chat.server.session.GroupSession;
import com.rookie.chat.server.session.GroupSessionFactory;
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
        if (group == null) {
            // 发生成功消息
            ctx.writeAndFlush(new GroupCreateResponseMessage(true, "群聊[" + groupName + "]创建成功"));
            // 发送拉群消息
            List<Channel> channels = groupSession.getMembersChannel(groupName);
            for (Channel ch : channels) {
                ch.writeAndFlush(new GroupCreateResponseMessage(true, "您已被拉入群聊[" + groupName + "]"));
            }
        }
        // 发生失败消息
        else {
            ctx.writeAndFlush(new GroupCreateResponseMessage(false, "群聊[" + groupName + "]已存在"));
        }
    }
}
