package com.rookie.message;

import com.rookie.chat.message.ChatRequestMessage;
import com.rookie.chat.message.ChatResponseMessage;
import com.rookie.chat.message.GroupChatRequestMessage;
import com.rookie.chat.message.GroupChatResponseMessage;
import com.rookie.chat.message.GroupCreateRequestMessage;
import com.rookie.chat.message.GroupCreateResponseMessage;
import com.rookie.chat.message.GroupJoinRequestMessage;
import com.rookie.chat.message.GroupJoinResponseMessage;
import com.rookie.chat.message.GroupMembersRequestMessage;
import com.rookie.chat.message.GroupMembersResponseMessage;
import com.rookie.chat.message.GroupQuitRequestMessage;
import com.rookie.chat.message.GroupQuitResponseMessage;
import com.rookie.chat.message.LoginRequestMessage;
import com.rookie.chat.message.LoginResponseMessage;
import com.rookie.rpc.message.RpcRequestMessage;
import com.rookie.rpc.message.RpcResponseMessage;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * @author yayee
 */
@Data
public abstract class Message implements Serializable {

    public static final int LoginRequestMessage = 0;
    public static final int LoginResponseMessage = 1;
    public static final int ChatRequestMessage = 2;
    public static final int ChatResponseMessage = 3;
    public static final int GroupCreateRequestMessage = 4;
    public static final int GroupCreateResponseMessage = 5;
    public static final int GroupJoinRequestMessage = 6;
    public static final int GroupJoinResponseMessage = 7;
    public static final int GroupQuitRequestMessage = 8;
    public static final int GroupQuitResponseMessage = 9;
    public static final int GroupChatRequestMessage = 10;
    public static final int GroupChatResponseMessage = 11;
    public static final int GroupMembersRequestMessage = 12;
    public static final int GroupMembersResponseMessage = 13;
    public static final int PingMessage = 14;
    public static final int PongMessage = 15;
    // 添加RPC消息类型
    public static final int RPC_MESSAGE_TYPE_REQUEST = 101;
    public static final int RPC_MESSAGE_TYPE_RESPONSE = 102;

    private static final Map<Integer, Class<? extends Message>> messageClasses = new HashMap<>();

    static {
        messageClasses.put(LoginRequestMessage, LoginRequestMessage.class);
        messageClasses.put(LoginResponseMessage, LoginResponseMessage.class);
        messageClasses.put(ChatRequestMessage, ChatRequestMessage.class);
        messageClasses.put(ChatResponseMessage, ChatResponseMessage.class);
        messageClasses.put(GroupCreateRequestMessage, GroupCreateRequestMessage.class);
        messageClasses.put(GroupCreateResponseMessage, GroupCreateResponseMessage.class);
        messageClasses.put(GroupJoinRequestMessage, GroupJoinRequestMessage.class);
        messageClasses.put(GroupJoinResponseMessage, GroupJoinResponseMessage.class);
        messageClasses.put(GroupQuitRequestMessage, GroupQuitRequestMessage.class);
        messageClasses.put(GroupQuitResponseMessage, GroupQuitResponseMessage.class);
        messageClasses.put(GroupChatRequestMessage, GroupChatRequestMessage.class);
        messageClasses.put(GroupChatResponseMessage, GroupChatResponseMessage.class);
        messageClasses.put(GroupMembersRequestMessage, GroupMembersRequestMessage.class);
        messageClasses.put(GroupMembersResponseMessage, GroupMembersResponseMessage.class);
        // 将消息类型放入消息类对象Map中
        messageClasses.put(RPC_MESSAGE_TYPE_REQUEST, RpcRequestMessage.class);
        messageClasses.put(RPC_MESSAGE_TYPE_RESPONSE, RpcResponseMessage.class);
    }

    private int sequenceId;
    private int messageType;

    public static Class<? extends Message> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }

    public abstract int getMessageType();
}

