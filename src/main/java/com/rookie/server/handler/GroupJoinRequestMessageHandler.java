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
import java.util.List;

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
        Channel fromChannel = SessionFactory.getSession().getChannel(username);
        // 成功加入群聊
        if (group != null) {
            fromChannel.writeAndFlush(new GroupJoinResponseMessage(true, "加入群聊[" + groupName + "]成功"));
            // 通知群内其他人
            List<Channel> channels = GroupSessionFactory.getGroupSession().getMembersChannel(groupName);
            for (Channel ch : channels) {
                if (ch != fromChannel) {
                    ch.writeAndFlush(
                        new GroupJoinResponseMessage(true, "\"" + username + "\"加入了群聊[" + groupName + "]"));
                }
            }
        }
        // 加入群聊失败 群聊不存在
        else {
            fromChannel.writeAndFlush(new GroupJoinResponseMessage(false, "群聊[" + groupName + "]不存在"));
        }
    }
}
