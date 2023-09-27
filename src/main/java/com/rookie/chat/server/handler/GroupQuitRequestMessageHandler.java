package com.rookie.chat.server.handler;

import com.rookie.chat.message.GroupQuitRequestMessage;
import com.rookie.chat.message.GroupQuitResponseMessage;
import com.rookie.chat.server.session.Group;
import com.rookie.chat.server.session.GroupSessionFactory;
import com.rookie.chat.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.List;

/**
 * @author yayee
 */
@ChannelHandler.Sharable
public class GroupQuitRequestMessageHandler extends SimpleChannelInboundHandler<GroupQuitRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupQuitRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        String username = msg.getUsername();
        Group group = GroupSessionFactory.getGroupSession().removeMember(groupName, username);
        Channel fromChannel = SessionFactory.getSession().getChannel(username);
        if (group != null) {
            // 提示退出成功
            ctx.writeAndFlush(new GroupQuitResponseMessage(true, "退出群聊[" + groupName + "]成功"));
            // 通知其他群成员
            List<Channel> channels = GroupSessionFactory.getGroupSession().getMembersChannel(groupName);
            for (Channel ch : channels) {
                if (ch != fromChannel) {
                    ch.writeAndFlush(
                        new GroupQuitResponseMessage(true, "\"" + username + "\"退出了群聊[" + groupName + "]"));
                }
            }
        } else {
            ctx.writeAndFlush(new GroupQuitResponseMessage(false, "群聊[" + groupName + "]不存在"));
        }
    }
}
